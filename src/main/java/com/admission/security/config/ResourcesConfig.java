package com.admission.security.config;

import com.admission.security.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * 通用配置
 *
 * @author ruoyi
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    @Value("${cors.allowedOrigin}")
    private String allowedOrigin;

    @Value("${cors.allowedHeaders}")
    private String allowedHeaders;

    @Value("${cors.allowedMethods}")
    private String allowedMethods;

//    @Autowired
//    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    /**
     * 自定义拦截规则
     */
/*    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 重入拦截器
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
    }*/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 本地文件上传路径 */
        //registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**").addResourceLocations("file:" + ImosConfig.getProfile() + "/");

        /** swagger配置 */
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        // 设置访问源地址
        if (StringUtils.isNotEmpty(allowedOrigin)) {
            config.setAllowedOrigins(Arrays.asList(allowedOrigin.split(",")));
        } else {
            config.addAllowedOrigin("*");
        }
        // 设置访问源请求头
        if (StringUtils.isNotEmpty(allowedHeaders)) {
            config.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
        } else {
            config.addAllowedHeader("*");
        }
        // 设置访问源请求方法
        if (StringUtils.isNotEmpty(allowedMethods)) {
            config.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
        } else {
            config.addAllowedMethod("*");
        }
        // 对接口配置跨域设置
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}