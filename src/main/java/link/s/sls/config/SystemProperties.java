package link.s.sls.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Configuration
@ConfigurationProperties(prefix = "system")
public class SystemProperties {

    private Long groupId;

    private String redirectHost;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getRedirectHost() {
        return redirectHost;
    }

    public void setRedirectHost(String redirectHost) {
        this.redirectHost = redirectHost;
    }
}
