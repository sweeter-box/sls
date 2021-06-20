package link.s.sls.index;

import link.s.sls.access.AccessLog;
import link.s.sls.access.AccessLogUtils;
import link.s.sls.link.LinkService;
import link.s.sls.utils.IpUtils;
import link.s.sls.utils.URLUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
public class IndexController {

    @Autowired
    private LinkService linkService;

    /**
     * 主页
     * @param serverHttpRequest
     * @param serverHttpResponse
     */
    @GetMapping(value = {"/"})
    public void index(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        serverHttpResponse.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        serverHttpResponse.getHeaders().set("Location", "ui/index.html");
    }


    /**
     * 302 重定向处理
     * @param mCode
     * @return
     */
    @GetMapping(value = "/{mCode:[0-9a-zA-Z.]+$}")
    public void redirect(@PathVariable String mCode, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse){
        //查询映射
        String originUrl = linkService.findMapping(mCode);
        //记录日志
        CompletableFuture.runAsync(() -> {
            HttpHeaders httpHeaders = serverHttpRequest.getHeaders();
            String ip = IpUtils.getIp(serverHttpRequest);
            String geo = IpUtils.getGeoInfo(ip);
            String referer = httpHeaders.getFirst("Referer");
            String userAgent = httpHeaders.getFirst("User-Agent");
            String host = httpHeaders.getFirst("Host");
            log.info("ip:{} geo:{} redirect mCode:{}", ip, geo, mCode);
            AccessLog accessLog = AccessLog.builder()
                    .accessTime(LocalDateTime.now())
                    .code(mCode)
                    .ip(ip)
                    .ipGeo(geo)
                    .referer(referer)
                    .host(host)
                    .origin(originUrl)
                    .ua(userAgent)
                    .build();
            AccessLogUtils.saveLog(accessLog);
        });

        if (StringUtils.isBlank(originUrl)) {
            serverHttpResponse.setStatusCode(HttpStatus.NOT_FOUND);
        }else {
            serverHttpResponse.setStatusCode(HttpStatus.FOUND);
            serverHttpResponse.getHeaders().set("Location", URLUtils.decode(originUrl));
        }
    }

}
