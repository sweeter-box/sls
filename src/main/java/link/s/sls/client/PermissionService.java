package link.s.sls.client;

import link.s.sls.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author sweeter
 * @date 2021/11/6
 */
@Slf4j
@Component
public class PermissionService {

    @Autowired
    private ClientService clientService;

    /**
     * 权限验证
     *
     * @param appKey
     * @param sign
     * @param ts
     * @return
     */
    public boolean hasPermission(String appKey, String sign, String ts) {
        Optional<ClientEntity> clientOpt = clientService.findClientByAppKey(appKey);
        if (clientOpt.isPresent()) {
            String key = String.format("%s%s%s", appKey, ts, clientOpt.get().getAppSecret());
            return sign.equals(MD5Utils.getMD5(key));
        }
        return false;
    }
}
