package link.s.sls.config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Configuration
@EnableConfigurationProperties(StaticResourceProperties.class)
public class StaticResourceAutoConfiguration {

    private StaticResourceProperties properties;

    public StaticResourceAutoConfiguration(StaticResourceProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(name = "staticResources")
    public Map<String, String> staticResources() {
        return properties.getResources();
    }

   // @ConditionalOnExpression("!'${staticProxy.resources}'.isEmpty()")
    @Bean
    @ConditionalOnBean(name = "staticResources", value = Map.class)
    public RouterFunction<ServerResponse> staticResourceLocator(ResourceLoader resourceLoader, Map<String, String> staticResources) {
        //空 `Map` 其实不需要启用配置，但没有 `@ConditionalOnNotEmptyBean` 这种注解，
        // https://stackoverflow.com/questions/62734544/spring-conditionalonproperty-for-bean[此问题^] 待优化
        if (staticResources.isEmpty()) {
            return null;
        }
        RouterFunctions.Builder builder = RouterFunctions.route();
        staticResources.forEach((key, value) -> builder.add(RouterFunctions.resources(key, resourceLoader.getResource(value))));
        return builder.build();
    }
}
