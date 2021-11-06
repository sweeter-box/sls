package link.s.sls.client;

import link.s.sls.common.jpa.repository.BaseJpaRepository;

import java.util.Optional;

/**
 * @author sweeter
 * @date 2021/11/6
 */
public interface ClientRepository extends BaseJpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByAppKey(String appKey);




}
