package link.s.sls.link;

import link.s.sls.code.CodeFactory;
import link.s.sls.code.CodeType;
import link.s.sls.config.SystemProperties;
import link.s.sls.exception.SlsException;
import link.s.sls.utils.MD5Utils;
import link.s.sls.utils.MappingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import static link.s.sls.link.Constant.FIRST_GROUP_ID;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class LinkServiceImpl implements LinkService {

    @Autowired
    private CodeFactory codeFactory;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private SystemProperties systemProperties;

    @Autowired
    private RedisLinkRepository redisLinkRepository;


    /**
     * 创建映射关系
     *
     * @param url
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createMapping(String url) {
        //查询缓存
        List<LinkRO> roList = redisLinkRepository.findByUrl(url);
        return Optional.ofNullable(roList)
                .filter(x -> !CollectionUtils.isEmpty(x))
                .flatMap(x -> x.stream().findAny())
                .map(LinkRO::getId)
                .orElseGet(() -> this.getMCode(url));
    }

    /**
     * 创建映射关系,并缓存到redis
     * @param url
     * @return
     */
    private String getMCode(String url) {
        Long groupId = Objects.nonNull(systemProperties.getGroupId()) ? systemProperties.getGroupId() : FIRST_GROUP_ID;
        Long code = codeFactory.getCodeDao(CodeType.REDIS.getValue()).next(groupId);
        String mCode = groupId.compareTo(FIRST_GROUP_ID) == 0 ? MappingUtils.encode(code) : String.format("%s.%s", MappingUtils.encode(groupId), MappingUtils.encode(code));
        LinkEntity en = new LinkEntity();
        en.setCode(code);
        en.setGroupId(groupId);
        en.setCreateTime(LocalDateTime.now());
        en.setUrl(url);
        en.setMd5Url(MD5Utils.getMD5(url));
        linkRepository.save(en);
        //保存缓存映射值
        redisLinkRepository.save(new LinkRO().apply(en));
        return mCode;
    }

    /**
     * 查询映射关系
     *
     * @param mCode 映射码
     * @return
     */
    @Override
    public String findMapping(String mCode) {
        Optional<LinkRO> roOpt = redisLinkRepository.findById(mCode);
       return roOpt.map(LinkRO::getUrl).orElseGet(() -> {
            long groupId,code;
            if (mCode.contains(".")) {
                String[] mArray = mCode.replaceAll("\\.", ",").split(",");
                if (mArray.length == 2) {
                    groupId = MappingUtils.decode(mArray[0]);
                    code = MappingUtils.decode(mArray[1]);
                }else {
                    throw new SlsException("非法参数");
                }
            } else {
                groupId = FIRST_GROUP_ID;
                code = MappingUtils.decode(mCode);
            }
            Optional<LinkEntity> linkOpt = linkRepository.findByCodeAndGroupId(code, groupId);
            if (linkOpt.isPresent()) {
                LinkRO linkRO = linkOpt.map(new LinkRO()).get();
                log.debug("缓存映射:{}", linkRO);
                redisLinkRepository.save(linkRO);
            }
            return linkOpt.map(LinkEntity::getUrl).orElse(null);
        });

    }

}
