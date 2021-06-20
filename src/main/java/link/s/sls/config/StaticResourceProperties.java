package link.s.sls.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@ConfigurationProperties("static-proxy")
public class StaticResourceProperties {

        /**
         * 静态资源配置，key 表示路径规则，value 表示转发地址
         */
    private Map<String, String> resources = new LinkedHashMap<>();

    public Map<String, String> getResources() {
        return resources;
    }

    public void setResources(Map<String, String> resources) {
        this.resources = resources;
    }
}
