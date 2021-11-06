package link.s.sls.client;

import java.util.Optional;

/**
 * @author sweeter
 * @date 2021/11/6
 */
public interface ClientService {

    Optional<ClientEntity> findClientByAppKey(String appKey);

    /**
     * 刷新客户端密匙
     * @param appKey
     * @param appSecret
     */
    void refresh(String appKey, String appSecret);
}
