package link.s.sls.link;

import link.s.sls.config.SystemProperties;
import link.s.sls.utils.URLUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Slf4j
@RestController
public class LinkController {

    @Autowired
    private LinkService linkService;

    @Autowired
    private SystemProperties systemProperties;

    /**
     * url转换
     * 过期时间
     */
    @PostMapping("link/convert")
    public ResponseEntity<Object> convert(@RequestBody @Validated ReqOriginVO req) {
        String origin = req.getOrigin();
        //参数验证 1.验证是否是http url 2.验证url长度 300个字符
        //记录日志
        origin = URLUtils.encode(origin);
        try {
            String code = linkService.createMapping(origin);
            String redirectUrl = UriComponentsBuilder.fromHttpUrl(systemProperties.getRedirectHost())
                    .path(code)
                    .build()
                    .toUriString();
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("redirectUrl", redirectUrl);
            return ResponseEntity.ok(resMap);
        } catch (ConstraintViolationException e) {
            log.error("唯一索引数据重复：{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
