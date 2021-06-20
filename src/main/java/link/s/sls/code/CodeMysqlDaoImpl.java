package link.s.sls.code;

import link.s.sls.link.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Component
public class CodeMysqlDaoImpl implements CodeDao {

    @Autowired
    private LinkRepository linkRepository;
    /**
     * 获取指定code组的code值,code值连续
     *
     * @param groupId
     * @return
     */
    @Override
    public Long next(Long groupId) {
        Optional<Long> maxCodeOpt = linkRepository.findMaxCodeByGroupId(groupId);
        return maxCodeOpt.map(v -> ++v).orElse(1L);
    }
}
