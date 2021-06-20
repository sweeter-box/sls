package link.s.sls.code;

import link.s.sls.common.lock.RedisLock;
import link.s.sls.link.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Component
public class CodeRedisDaoImpl implements CodeDao {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private RedisLock redisLock;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * id 前缀
     */
    private final static String ID_KEY_PREFIX = "idGroup:";

    private long generate(Long groupId) {
        String key = String.format("%s%d", ID_KEY_PREFIX, groupId);
        Boolean exist = redisTemplate.hasKey(key);
        RedisAtomicLong counter = new RedisAtomicLong(key, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        if (Objects.nonNull(exist) && exist) {

        } else {
            Optional<Long> maxCodeOpt = linkRepository.findMaxCodeByGroupId(groupId);
            counter.set(maxCodeOpt.orElse(0L));
        }
        return counter.incrementAndGet();
    }

    /**
     *  根据id组获取id值
     * @param groupId
     * @return 自增序列
     */
    public Long next(Long groupId) {
        //加锁
        String lockKey = redisLock.createKeyByParams(groupId);
        redisLock.lock(lockKey);
        Long code = generate(groupId);
        redisLock.unlock(lockKey);
        return code;
    }
}
