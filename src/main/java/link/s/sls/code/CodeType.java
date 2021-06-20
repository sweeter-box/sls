package link.s.sls.code;

import lombok.Getter;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Getter
public enum CodeType {
    /**
     * code值的生成类型
     */
    REDIS("redis"),

    MYSQL("mysql");

    private String value;
    CodeType(String value) {
        this.value = value;
    }

    public static CodeType of(String value){
        for (CodeType item :values()) {
            if(item.value.equals(value)){
                return item;
            }
        }
        throw new RuntimeException("枚举类型不存在");
    }
}
