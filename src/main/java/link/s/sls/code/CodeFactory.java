package link.s.sls.code;

import link.s.sls.exception.SlsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Component("codeFactory")
public class CodeFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public CodeDao getCodeDao(String type){
        CodeType codeType = CodeType.of(type);
        switch(codeType){
            case REDIS:
                return applicationContext.getBean(CodeRedisDaoImpl.class);
            case MYSQL:
                return applicationContext.getBean(CodeMysqlDaoImpl.class);

            default: throw new SlsException("未找到对应类型");
        }
    }
}
