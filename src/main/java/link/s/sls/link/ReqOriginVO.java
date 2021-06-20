package link.s.sls.link;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Data
public class ReqOriginVO {

    @Pattern(regexp="(http|https):\\/\\/([\\w.]+\\/?)\\S*",message = "URL格式错误，必须以http或https开头")
    @Length(max = 300,message = "URL过长，必须在300个字符以内")
    @NotBlank
    private String origin;
}
