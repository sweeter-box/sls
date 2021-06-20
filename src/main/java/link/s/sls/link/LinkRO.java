package link.s.sls.link;

import com.alibaba.fastjson.JSON;
import link.s.sls.utils.MappingUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Function;

import static link.s.sls.link.Constant.FIRST_GROUP_ID;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@RedisHash("link")
public class LinkRO implements Serializable, Function<LinkEntity,LinkRO> {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Id
    private String id;

    @Indexed
    private String url;

    @Transient
    private Long expire = -1L;

    private LocalDateTime createTime = LocalDateTime.now();

    /**
     * 过期时间
     *
     * @return Long
     */
    @TimeToLive
    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    /**
     * Applies this function to the given argument.
     *
     * @param entity the function argument
     * @return the function result
     */
    @Override
    public LinkRO apply(LinkEntity entity) {
        Long code = entity.getCode();
        Long groupId = entity.getGroupId();
        String mCode = groupId.compareTo(FIRST_GROUP_ID) == 0 ? MappingUtils.encode(code) : String.format("%s.%s", MappingUtils.encode(groupId), MappingUtils.encode(code));
        LinkRO ro = new LinkRO();
        ro.setCreateTime(entity.getCreateTime());
        ro.setExpire(Duration.ofDays(7L).getSeconds());
        ro.setId(mCode);
        ro.setUrl(entity.getUrl());
        return ro;
    }
}
