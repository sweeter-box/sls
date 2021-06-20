package link.s.sls.link;

/**
 * @author sweeter
 * @date 2021/6/20
 */
public interface LinkService {
    /**
     * 创建映射关系
     * @param url
     * @return
     */
    String createMapping(String url);

    /**
     * 查询映射关系
     * @param mCode
     * @return
     */
    String findMapping(String mCode);
}
