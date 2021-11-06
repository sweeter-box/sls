package link.s.sls.filter;

import link.s.sls.client.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

/**
 * @author sweeter
 * @date 2021/11/6
 */
@Slf4j
@Component
public class ClientFilter implements WebFilter {

    @Autowired
    private PermissionService permissionService;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        PathPattern pattern=new PathPatternParser().parse("/link/**");
        ServerHttpRequest request = serverWebExchange.getRequest();
        ServerHttpResponse response = serverWebExchange.getResponse();
        if (pattern.matches(request.getPath().pathWithinApplication())){
            HttpHeaders httpHeaders = request.getHeaders();
            String ts = httpHeaders.getFirst("ts");
            String appKey = httpHeaders.getFirst("appKey");
            String sign = httpHeaders.getFirst("sign");
            if (StringUtils.isNotBlank(ts)
                    && StringUtils.isNotBlank(appKey)
                    && StringUtils.isNotBlank(sign)
                    && permissionService.hasPermission(appKey, sign, ts)) {
                return webFilterChain.filter(serverWebExchange).doFinally(signalType -> {
                    log.info("appKey:{},sign:{},ts:{},验签通过", appKey, sign, ts);
                });
            }else {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return Mono.empty();
            }
        }
        return webFilterChain.filter(serverWebExchange);

    }
}
