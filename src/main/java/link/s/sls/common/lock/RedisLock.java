package link.s.sls.common.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Component
@Slf4j
public class RedisLock implements Lock {

    private static final String SEPARATOR = ":";

    private static final String KEYREFIX = "redisLock:";

     @Resource
    private RedisTemplate<byte[],byte[]> redisTemplate;

    /**
     * 默认redis 锁过期时间 单位/毫秒
     */
    private static final Long DEFAULT_EXPIRE_TIME = 10 * 1000L;
    /**
     * 默认的取锁超时时间 单位/毫秒
     */
    private static final Long DEFAULT_TIME_OUT =  1000L;
    /**
     * 获取锁
     *
     * @param key
     */
    @Override
    public boolean lock(String key) {
        return lock(key,DEFAULT_TIME_OUT,DEFAULT_EXPIRE_TIME);
    }


    @Override
    public boolean lock(String key, Long expire) {
        return lock(key,DEFAULT_TIME_OUT,expire);
    }

    @Override
    public boolean lock(String key, Long timeOut, Long expire) {
        Assert.isTrue(StringUtils.isNotBlank(key),"lock key 不能为空");
        try {
            // 获取锁的超时时间，超过这个时间则放弃获取锁
            long reqLockEndTime = System.currentTimeMillis() + Math.max(timeOut,DEFAULT_TIME_OUT);
            while (System.currentTimeMillis() < reqLockEndTime) {
                long lockExpireTime = System.currentTimeMillis() + Math.max(expire,DEFAULT_TIME_OUT);
                Boolean result = redisTemplate.opsForValue().setIfAbsent(key.getBytes(), String.valueOf(lockExpireTime).getBytes());
                if (result) {
                    return Boolean.TRUE;
                }else {
                    byte[] oldExpireByte = redisTemplate.opsForValue().get(key.getBytes());
                    long oldExpireTime = Long.parseLong(new String(oldExpireByte));
                    //判断已存在的锁是否过期
                    if (oldExpireTime < System.currentTimeMillis()) {
                        byte[] oldValue = redisTemplate.opsForValue().getAndSet(key.getBytes(), String.valueOf(lockExpireTime).getBytes());
                        if (oldValue != null && oldExpireTime == Long.parseLong(new String(oldValue)) && oldExpireTime < System.currentTimeMillis()){
                            return Boolean.TRUE;
                        }
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            log.error("acquire lock due to error", e);
        }
        return Boolean.FALSE;
    }

    /**
     * 释放锁
     *
     * @param key
     */
    @Override
    public boolean unlock(String key) {
        Assert.isTrue(StringUtils.isNotBlank(key),"lock key 不能为空");
        return redisTemplate.delete(key.getBytes());
    }

    /**
     * 根据方法参数生成 key
     * @param params
     * @return
     */
    public String createKeyByParams(Object... params) {
        StringBuffer key = new StringBuffer(KEYREFIX);
        if (params != null && params.length > 0) {
            for (Object param : params) {
                key.append(SEPARATOR).append(param);
            }
        }
        return key.toString();
    }
}
