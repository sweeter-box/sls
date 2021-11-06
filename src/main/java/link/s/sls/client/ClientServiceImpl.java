package link.s.sls.client;

import link.s.sls.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * @author sweeter
 * @date 2021/11/6
 */
@CacheConfig(cacheNames = "client")
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Cacheable(unless="#result == null")
    @Override
    public Optional<ClientEntity> findClientByAppKey(String appKey) {
        return clientRepository.findByAppKey(appKey);
    }

    /**
     * 刷新客户端密匙
     *
     * @param appKey
     * @param appSecret
     */
    @CacheEvict(allEntries = true)
    @Transactional(readOnly = false)
    @Override
    public void refresh(String appKey, String appSecret) {
        Optional<ClientEntity> clientOpt = clientRepository.findByAppKey(appKey);
        if (clientOpt.isPresent()) {
            ClientEntity client = clientOpt.get();
            if (StringUtils.equals(client.getAppSecret(), appSecret)) {
                client.setAppSecret(TokenUtils.generateToken(64));
                clientRepository.save(client);
            }else {
                throw new RuntimeException("密匙不匹配");
            }
        }else {
            throw new RuntimeException("该appKey不存在");
        }

    }
}
