package com.ry.sskt.core.config;

import com.alibaba.fastjson.serializer.PascalNameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.util.TypeUtils;
import com.ry.sskt.core.filter.AllowUserTypeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;

/**
 * @description: weburl映射配置
 * @author: 幸仁强
 * @create: 2019-06-10 10:21
 **/
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    /**
     * token检验拦截器
     *
     * @return
     */
    @Bean
    AllowUserTypeInterceptor allowUserTypeInterceptor() {
        return new AllowUserTypeInterceptor();
    }

    /**
     * 静态资源注册
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射swagger-ui, 否则将无法访问
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/");
    }


    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        stringHttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        return stringHttpMessageConverter;
    }

    /**
     * 自定义HTTP消息转换器
     *
     * @return
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        FastJsonHttpMessageConverter fastConvert = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializeFilters(new PascalNameFilter());
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteDateUseDateFormat);
        fastConvert.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConvert);
        converters.add(responseBodyConverter());
    }

    /**
     * 拦截器注册
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(allowUserTypeInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(filterWhiteList());
        super.addInterceptors(registry);
    }

    /**
     * 白名单
     *
     * @return
     */
    private List<String> filterWhiteList() {
        List<String> whiteList = new LinkedList<String>();

        whiteList.add("/info/*");
        whiteList.add("/swagger*");
        whiteList.add("/webjars/**");
        whiteList.add("/swagger-resources/**");
        whiteList.add("/v2/**");
        whiteList.add("/swagger-ui.html/**");
        whiteList.add("/images/favicon-*");
        whiteList.add("/druid/**");
        whiteList.add("/favicon.ico");
        return whiteList;
    }

}
