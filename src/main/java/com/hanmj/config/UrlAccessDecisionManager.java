package com.hanmj.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * 将CustomMetadataSource传递过来的角色信息判断，当前用户是否有权限访问该链接
 */
@Component
public class UrlAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        System.out.println("UrlAccessDecisionManager执行");
        Iterator<ConfigAttribute> iterator = collection.iterator();//有访问该菜单的角色信息
        while(iterator.hasNext()){
            ConfigAttribute ca = iterator.next();
            String needRole = ca.getAttribute();
            if("ROLE_LOGIN".equals(needRole)){
                if(authentication instanceof AnonymousAuthenticationToken){//如果是匿名访问用户
                    throw new BadCredentialsException("未登录");
                }else{
                    return;
                }
            }
            //获取当前用户角色信息，实现的UserDetail的实例处获取
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority:authorities) {
                if(authority.getAuthority().equals(needRole)){//当前用户有权限则直接返回
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
