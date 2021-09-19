package link.s.sls.access;

import link.s.sls.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Transactional(rollbackFor = Exception.class, readOnly = true)
@Slf4j
@Service
public class AccessLogServiceImpl implements AccessLogService {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Transactional
    @Override
    public void save(AccessLog accessLog) {
        AccessLogEntity entity = new AccessLogEntity();
        BeanUtils.copyProperties(accessLog, entity);
        entity.setIp(IpUtils.ipv4ToLong(accessLog.getIp()));
        accessLogRepository.save(entity);
    }
}
