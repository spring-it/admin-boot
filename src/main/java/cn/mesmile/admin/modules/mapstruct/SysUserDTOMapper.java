package cn.mesmile.admin.modules.mapstruct;

import cn.mesmile.admin.modules.system.domain.dto.SysUserDTO;
import cn.mesmile.admin.modules.system.entity.SysUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author zb
 * @Description
 */
@Mapper
public interface SysUserDTOMapper {

    SysUserDTOMapper MAPPER = Mappers.getMapper(SysUserDTOMapper.class );

    /*
    属性类型相同，名称不同的时候，使用@Mapping注解指定source和target字段名称对应关系， 如果有多个这种属性，那就指定多个@Mapping注解。
    忽略某个字段，在@Mapping的时候，加上ignore = true
    转化日期格式，字符串到数字的格式，可以使用dateFormat，numberFormat
    如果有自定义转换的需求，写一个简单的Java类即可，然后在方法上打上Mapstruct的注解@Named，在在@Mapper(uses = 自定义的类)，然后@Mapping中用上qualifiedByName。
    @Mapping(target = "userNick1", source = "userNick")
    @Mapping(target = "createTime", source = "createTime", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "age", source = "age", numberFormat = "#0.00")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userVerified", defaultValue = "defaultValue-2")
     */

    /**
     * 将 do 转 dto
     * @param sysUser
     * @return
     */
//    @Mapping(target = "userNick1", source = "userNick")
//    @Mapping(target = "createTime", source = "createTime", dateFormat = "yyyy-MM-dd")
//    @Mapping(target = "age", source = "age", numberFormat = "#0.00")
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "userVerified", defaultValue = "defaultValue-2")
    SysUserDTO toTarget(SysUser sysUser);

    /*
     用法：
     将 do 转换为 dto
     SysUserDTO sysUserDTO = SysUserDTOMapper.MAPPER.toTarget(sysUser);
     */
}
