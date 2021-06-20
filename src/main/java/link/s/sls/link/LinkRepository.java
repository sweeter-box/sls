package link.s.sls.link;

import link.s.sls.common.jpa.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author sweeter
 * @date 2021/6/20
 */
public interface LinkRepository extends BaseJpaRepository<LinkEntity, Long> {

    Optional<LinkEntity> findByCodeAndGroupId(Long code, Long groupId);


    /**
     * 查询最大编码值
     * @param groupId
     * @return
     */
    @Query(value = "SELECT MAX(l.code) FROM LinkEntity l WHERE l.groupId= :groupId")
    Optional<Long> findMaxCodeByGroupId(Long groupId);


}
