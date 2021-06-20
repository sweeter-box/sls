package link.s.sls.common.lock;

/**
 * @author sweeter
 * @date 2021/6/20
 */
public interface Lock {

    /**
     * 获取锁
     * @param key lock key
     * @return boolean
     */
    boolean lock(String key);

    /**
     * 获取锁
     * @param key lock key
     * @param expire lock 超时时间 单位/毫秒 注：该时间应该大于业务处理的时间
     * @return boolean
     */
    boolean lock(String key, Long expire);

    /**
     *  获取锁
     * @param key lock key
     * @param timeOut 取lock 超时时间 单位/毫秒
     * @param expire lock 超时时间 单位/毫秒 注：该时间应该大于业务处理的时间
     * @return boolean
     */
    boolean lock(String key, Long timeOut, Long expire) throws InterruptedException;


    /**
     * 释放锁
     * @param key lock key
     * @return
     */
    boolean unlock(String key);
}
