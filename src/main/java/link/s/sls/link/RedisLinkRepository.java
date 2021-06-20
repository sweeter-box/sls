package link.s.sls.link;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Repository
public interface RedisLinkRepository extends PagingAndSortingRepository<LinkRO,String> {

    List<LinkRO> findByUrl(String url);

}
