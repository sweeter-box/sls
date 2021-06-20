package link.s.sls.access;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import link.s.sls.common.jpa.entity.IdEntity;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Data
@Table(name = "sls_access_log"
        ,indexes= {
        @Index(name = "sls_access_log_code_index",columnList = "code")
        ,@Index(name = "sls_access_log_ip_index",columnList = "ip")
        ,@Index(name = "sls_access_log_accessTime_index",columnList = "accessTime")
}
)
@Entity
public class AccessLogEntity extends IdEntity {

    @Column(columnDefinition ="varchar(16) COMMENT '访问ip'" )
    private String ip;

    @Column(columnDefinition ="varchar(7) COMMENT '映射码'" )
    private String code;

    @Column(columnDefinition ="varchar(32) COMMENT '访问ip位置'" )
    private String ipGeo;

    @Column(columnDefinition ="varchar(23) COMMENT '资源id'" )
    private String host;

    @Column(columnDefinition ="varchar(300) COMMENT '映射源URL'" )
    private String origin;

    @Column(columnDefinition ="varchar(300) COMMENT 'referer'" )
    private String referer;

    @Column(columnDefinition ="varchar(300) COMMENT 'userAgent'" )
    private String ua;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column
    private LocalDateTime accessTime;
}
