package cn.mesmile.admin.common.mongo.entity;

import cn.mesmile.admin.common.desensitization.PrivacyEncrypt;
import cn.mesmile.admin.common.desensitization.PrivacyTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author zb
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class Student implements Serializable {

    @Id
    private String id;

    /***
     *  测试数据脱敏
     */
    @PrivacyEncrypt(type = PrivacyTypeEnum.NAME)
    private String name;

    private Integer age;

    private Double score;

    private LocalDate birthday;
}
