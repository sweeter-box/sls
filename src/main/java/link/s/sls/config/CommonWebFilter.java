package link.s.sls.config;

import link.s.sls.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;
import java.util.Optional;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Component
public class CommonWebFilter implements WebFilter {

   private Logger log = LoggerFactory.getLogger("access");

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        PathPattern pattern=new PathPatternParser().parse("/**");

        ServerHttpRequest request = serverWebExchange.getRequest();
        ServerHttpResponse response = serverWebExchange.getResponse();

        HttpHeaders httpHeaders = request.getHeaders();
        String ip = IpUtils.getIp(request);
        String geo = IpUtils.getGeoInfo(ip);
        String referer = httpHeaders.getFirst("Referer");
        String userAgent = httpHeaders.getFirst("User-Agent");
        String host = httpHeaders.getFirst("Host");
        if (pattern.matches(request.getPath().pathWithinApplication())){
          // log.info("访问拦截匹配");
        }
        long startTime = System.currentTimeMillis();
        return webFilterChain.filter(serverWebExchange).doFinally(signalType -> {
            long totalTime = System.currentTimeMillis() - startTime;
            log.info("ip:{} ipGeo:{} referer:{} userAgent:{} host:{} path:{} time:{}", ip, geo, referer, userAgent, host, request.getPath().toString(), totalTime);
        });
    }
}
