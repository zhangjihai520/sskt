package com.ry.sskt.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
@NacosConfigurationProperties(dataId = "${spring.application.name}.${spring.cloud.nacos.file-extension}", autoRefreshed = true)
public class MyBatisPlusConfig {


    @Bean("dataSource")
    @ConfigurationProperties(prefix ="spring.datasource.druid")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }
    @Bean
    @ConfigurationProperties(prefix ="mybatis-plus.configuration")
    public MybatisConfiguration mybatisConfiguration()
    {
        return new MybatisConfiguration();
    }
    @Bean
    @ConfigurationProperties(prefix ="mybatis-plus.global-config")
    public GlobalConfig globalConfig()
    {
        return new GlobalConfig();
    }
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
    /**@Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }**/
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        try {
            MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
            sqlSessionFactory.setDataSource(druidDataSource());
            sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/com/ry/sskt/mapper/xml/*.xml"));
            sqlSessionFactory.setVfs(SpringBootVFS.class);
            sqlSessionFactory.setConfiguration(mybatisConfiguration());
            //sqlSessionFactory.setPlugins(paginationInterceptor());
            sqlSessionFactory.setGlobalConfig(globalConfig());
            sqlSessionFactory.afterPropertiesSet();
            return sqlSessionFactory.getObject();
        } catch (Exception e) {
            log.error("sqlSessionFactory创建失败！");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Bean
    public DataSourceTransactionManager transationManager(DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
}
