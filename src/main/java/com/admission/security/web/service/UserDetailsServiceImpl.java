package com.admission.security.web.service;

import com.admission.security.entity.SysPermission;
import com.admission.security.entity.SysUser;
import com.admission.security.entity.model.LoginUser;
import com.admission.security.service.SysPermissionService;
import com.admission.security.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       /* SysUser user = userService.selectByName(userName);

        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", userName);
            throw new UsernameNotFoundException("登录用户：" + userName + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", userName);
            if ("客户".equals(user.getCspUserType())) {
                AsyncManager.me().execute(AsyncFactory.recordOper(userName, user.getEtprInfoId(), 1, "登录用户：{" + userName + "} 已被删除."));
            }
            throw new BaseException("对不起，您的账号：" + userName + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", userName);
            if ("客户".equals(user.getCspUserType())) {
                AsyncManager.me().execute(AsyncFactory.recordOper(userName, user.getEtprInfoId(), 1, "登录用户：{" + userName + "} 已被停用."));
            }
            throw new BaseException("对不起，您的账号：" + userName + " 已停用");
        }
        return createLoginUser(user);*/
//----------------------------------------------
        if (username == null || "".equals(username)) {
            throw new RuntimeException("用户不能为空");
        }
        //根据用户名查询用户
        SysUser sysUser = userService.selectByName(username);
        if (sysUser == null) {
            throw new RuntimeException("用户不存在");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (sysUser != null) {
            //获取该用户所拥有的权限
            List<SysPermission> sysPermissions = sysPermissionService.selectListByUser(sysUser.getId());
            // 声明用户授权
            if (sysPermissions.size() > 1) {
                sysPermissions.forEach(sysPermission -> {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(sysPermission.getPermissionCode());
                    grantedAuthorities.add(grantedAuthority);
                });
            }
        }
        return createLoginUser(sysUser);
//        return new User(sysUser.getAccount(), sysUser.getPassword(), sysUser.getEnabled(), sysUser.getAccountNonExpired(), sysUser.getCredentialsNonExpired(), sysUser.getAccountNonLocked(), grantedAuthorities);
    }

    private UserDetails createLoginUser(SysUser user) {
//        return new LoginUser(user, permissionService.getMenuPermission(user));
        return new LoginUser(user, null);
    }
}
