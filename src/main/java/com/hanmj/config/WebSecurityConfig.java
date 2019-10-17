package com.hanmj.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanmj.common.RespBean;
import com.hanmj.common.UserUtils;
import com.hanmj.service.impl.HrServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * EnableGlobalMethodSecurity:开启基于方法的安全认证机制，也就是说在web层的controller启用注解机制的安全确认
 * 1、通过EnableGlobalMethodSecurity开启几区注解的安全配置，启用@PreAuthorize和@PostAuthorize
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private HrServiceImpl hrService;
    @Autowired
    private CustomMetadataSource customMetadataSource;
    @Autowired
    private UrlAccessDecisionManager urlAccessDecisionManager;
    @Autowired
    private AuthenticationAccessDeniedHandler deniedHandler;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       // System.out.println("1、AuthenticationManagerBuilder中配置userDetailsService和passwordEncoder");
        //AuthenticationManagerBuilder中配置userDetailsService和passwordEncoder
        auth.userDetailsService(hrService).passwordEncoder(new BCryptPasswordEncoder());//使用BCrypt加密
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
      //  System.out.println("2、配置忽略路径");
        web.ignoring().antMatchers("/index.html","/static/**","login_p");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       // System.out.println("3、配置拦截规则");
        //配置拦截规则，表单登录、登录成功或失败的响应等
        http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O o) {//配置拦截规则
               o.setSecurityMetadataSource(customMetadataSource);//判断当前访问的链接是否符合条件，如果符合则将其对应的角色信息返回
               o.setAccessDecisionManager(urlAccessDecisionManager); //将CustomMetadataSource传递过来的角色信息判断，当前用户是否有权限访问该链接
               return o;
            }
        }).and().formLogin().loginPage("/login_p").loginProcessingUrl("/login")
        .usernameParameter("username").passwordParameter("password")
        .failureHandler(new AuthenticationFailureHandler() {//失败后跳转页面
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                response.setContentType("application/json;charset=utf-8");
                RespBean respBean=null;
                if(e instanceof BadCredentialsException||e instanceof UsernameNotFoundException){
                    respBean=RespBean.error("账号名或密码错误！");
                }else if(e instanceof LockedException){
                    respBean=RespBean.error("账户被锁定，请联系管理员！");
                }else if(e instanceof CredentialsExpiredException){
                    respBean=RespBean.error("密码过期，请联系管理员");
                }else if(e instanceof AccountExpiredException){
                    respBean=RespBean.error("账户过期，请联系管理员");
                }else if(e instanceof DisabledException){
                    respBean=RespBean.error("账户被禁，请联系管理员");
                }else{
                    respBean=RespBean.error("登录失败！");
                }
                response.setStatus(401);
                ObjectMapper om=new ObjectMapper();
                PrintWriter out=response.getWriter();
                out.write(om.writeValueAsString(respBean));
                out.flush();
                out.close();
            }
        }).successHandler(new AuthenticationSuccessHandler() {//成功后跳转页面
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                //System.out.println("5、成功后跳转页面");
                response.setContentType("application/json;charset=utf-8");
                RespBean respBean=RespBean.ok("登录成功", UserUtils.getcurrentUser());
                ObjectMapper om=new ObjectMapper();
                PrintWriter out = response.getWriter();
                out.write(om.writeValueAsString(respBean));
                out.flush();
                out.close();
            }
        })
        .permitAll().and().logout().permitAll().and().csrf().disable().exceptionHandling().accessDeniedHandler(deniedHandler);//权限不足时处理
    }
}
