package com.admission.security.security;

import com.admission.security.entity.SysPermission;
import com.admission.security.entity.SysUser;
import com.admission.security.service.SysPermissionService;
import com.admission.security.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户登录认证信息查询
 *
 * @author Louis
 * @date Jun 29, 2019
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.selectByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user != null) {
            //获取该用户所拥有的权限
            List<SysPermission> sysPermissions = sysPermissionService.selectListByUser(user.getId());
            if (sysPermissions.size() > 1) {
                // 声明用户授权
                sysPermissions.forEach(sysPermission -> {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(sysPermission.getPermissionCode());
                    grantedAuthorities.add(grantedAuthority);
                });
            }
        }

        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
//        Set<String> permissions = userService.findPermissions(username);
//        List<GrantedAuthority> grantedAuthorities = permissions.stream().map(GrantedAuthorityImpl::new).collect(Collectors.toList());
        return new JwtUserDetails(username, user.getPassword(), grantedAuthorities);
    }
}