package link.s.sls.code;

/**
 * @author sweeter
 * @date 2021/6/20
 */
public interface CodeDao {
    /**
     * 获取指定code组的code值,code值连续,最小值为1
     * @param groupId
     * @return
     */
    Long next(Long groupId);
}
