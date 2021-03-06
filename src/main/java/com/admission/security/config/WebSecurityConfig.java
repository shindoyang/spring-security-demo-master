package com.admission.security.config;

import com.admission.security.config.handler.CustomizeAccessDecisionManager;
import com.admission.security.config.handler.CustomizeAccessDeniedHandler;
import com.admission.security.config.handler.CustomizeAuthenticationEntryPoint;
import com.admission.security.config.handler.CustomizeFilterInvocationSecurityMetadataSource;
import com.admission.security.security.JwtAuthenticationProvider;
import com.admission.security.security.JwtLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    //?????????????????????????????????????????????
    @Autowired
    CustomizeAuthenticationEntryPoint authenticationEntryPoint;

    //????????????????????????
    @Autowired
    CustomizeAccessDeniedHandler accessDeniedHandler;

    //?????????????????????
    @Autowired
    CustomizeAccessDecisionManager accessDecisionManager;

    //??????????????????
    @Autowired
    CustomizeFilterInvocationSecurityMetadataSource securityMetadataSource;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // ?????????????????????????????????hash???????????????
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // ???????????????????????????????????????
        auth.authenticationProvider(new JwtAuthenticationProvider(userDetailsService));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // ?????? csrf, ??????????????????JWT????????????????????????csrf
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(accessDecisionManager);//???????????????
                        o.setSecurityMetadataSource(securityMetadataSource);//??????????????????
                        return o;
                    }
                });
        // ??????????????????
//                .antMatchers(HttpMethod.OPTIONS,
//                        "/**",
//                        "/school/login",
//                        "/swagger**/**",
//                        "/webjars/**",
//                        "/v2/**",
//                        "/school/user/getUserByCode"
//                ).permitAll()
        // ????????????????????????????????????
//                .anyRequest().authenticated();
        // ?????????????????????
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        // ?????????????????????????????????
        http.addFilterBefore(new JwtLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        // ??????????????????????????????????????????
//        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        //????????????(??????????????????????????????)
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint);
        // ??????CORS filter
//        http.addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /*@Bean
    public CorsFilter corsFilter() throws Exception {
        return new CorsFilter();
    }*/

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/school/user/getUserByCode");
    }


}