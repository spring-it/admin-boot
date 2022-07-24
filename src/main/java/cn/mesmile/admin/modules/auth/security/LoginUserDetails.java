package cn.mesmile.admin.modules.auth.security;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.mesmile.admin.modules.system.entity.SysUser;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zb
 * @Description
 */
@Data
public class LoginUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private SysUser sysUser;

    private List<String> permissionList;

    private String uuid;

    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> grantedAuthorities;

    public LoginUserDetails(){}

    public LoginUserDetails(SysUser sysUser, List<String> permissionList){
        this.sysUser = sysUser;
        this.permissionList = permissionList;
    }

    /**
     * 返回用户 授权信息
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollUtil.isNotEmpty(grantedAuthorities)){
            return grantedAuthorities;
        }
        if (CollUtil.isNotEmpty(permissionList)){
            grantedAuthorities = permissionList.stream()
                    .filter(StrUtil::isNotEmpty)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return grantedAuthorities;
    }


    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        if (sysUser != null){
            return sysUser.getPassword();
        }
        return "";
    }

    @Override
    public String getUsername() {
        if (sysUser != null){
            return sysUser.getUsername();
        }
        return "";
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isEnabled() {
        return true;
    }

}
