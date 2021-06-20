package link.s.sls.access;

import link.s.sls.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CompletableFuture;


/**
 * @author sweeter
 * @date 2021/6/20
 */
@Slf4j
public class AccessLogUtils {

    private static AccessLogService logService  = SpringContextUtils.getBean(AccessLogService.class);

    private AccessLogUtils() {

    }

    public static void saveLog(AccessLog accessLog) {
        try {
            CompletableFuture.runAsync(() -> {
                logService.save(accessLog);
            });

        } catch (Exception e) {
            log.error("日志入库失败:{}", e.getMessage());
        }
    }

}
