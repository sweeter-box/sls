package link.s.sls.link;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Data
@Table(name = "sls_link"
        ,indexes= {
          @Index(name = "sls_link_groupId_index", columnList = "groupId")
        , @Index(name = "sls_link_code_index", columnList = "code")
        , @Index(name = "sls_link_md5Url_index", columnList = "md5Url")
       // , @Index(name = "sls_link_url_index", columnList = "url")
}
        ,uniqueConstraints = {@UniqueConstraint(name = "sls_link_groupId_code_index", columnNames = {"groupId", "code"})}
)
@Entity
public class LinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition ="bigint COMMENT '10进制编码组'",nullable = false,updatable = false)
    private Long groupId;

    @Column(columnDefinition ="bigint COMMENT '10进制编码'",nullable = false,updatable = false)
    private Long code;

    @Column(columnDefinition ="varchar(300) COMMENT '源URL路径'",nullable = false,updatable = false)
    private String url;

    @Column(columnDefinition ="varchar(32) COMMENT '源MD5(URL)值,128 bit 32个字符'",nullable = false,updatable = false)
    private String md5Url;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime createTime;


}
