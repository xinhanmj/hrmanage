package com.hanmj.config;

import com.hanmj.bean.Menu;
import com.hanmj.bean.Role;
import com.hanmj.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * 实现动态配置权限
 * Spring Security是通过SecurityMetadataSource来加载访问时所需要的具体权限
 *
 * 判断当前方位的链接是否符合条件，如果符合则将其对应的角色信息返回
 */
@Component
public class CustomMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private IMenuService menuService;
    AntPathMatcher antPathMatcher=new AntPathMatcher();
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        System.out.println("CustomMetadataSource执行");
        String requestUrl = ((FilterInvocation) o).getRequestUrl();//获取请求的url
        List<Menu> allMenu = this.menuService.getAllMenu();//获取所有的菜单及其对应角色，通过mybatis的collection将role信息对应的list中
        for(Menu menu:allMenu){
            if(antPathMatcher.match(menu.getUrl(),requestUrl)&&menu.getRoles().size()>0){//判断访问的路径是否在后台配置过，并且菜单是否给分配个角色
                List<Role> roles=menu.getRoles();
                int size = roles.size();
                String[] values=new String[size];
                for (int i=0;i<size;i++){
                    values[i]=roles.get(i).getName();
                }
                return SecurityConfig.createList(values);
            }
        }
        return SecurityConfig.createList("ROLE_LOGIN");

    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
