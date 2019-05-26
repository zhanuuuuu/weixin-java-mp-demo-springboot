package com.github.binarywang.demo.wx.mp.config;


import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;


@Configuration
@EnableConfigurationProperties(PostMainProperties.class)
@MapperScan(basePackages = PostMainDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "postmainSqlSessionFactory")
public class PostMainDataSourceConfig {

    static final String PACKAGE = "com.github.binarywang.demo.wx.mp.dao";
    static final String MAPPER_LOCATION = "classpath:mapper/postmain/*.xml";

    @Autowired
    private PostMainProperties properties;

    @Bean(name = "postmainDataSource")
    public DataSource postmainDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(this.properties.getDriverClassName());
        dataSource.setUrl(this.properties.getUrl());
        dataSource.setUsername(this.properties.getUsername());
        dataSource.setPassword(this.properties.getPassword());
        dataSource.setTestWhileIdle(this.properties.isTestWhileIdle());
        dataSource.setValidationQuery(this.properties.getValidationQuery());
        return dataSource;
    }

    @Bean(name = "postmainTransactionManager")
    public DataSourceTransactionManager postmainTransactionManager() {
        return new DataSourceTransactionManager(postmainDataSource());
    }

    @Bean(name = "postmainSqlSessionFactory")
    public SqlSessionFactory postmainSqlSessionFactory(@Qualifier("postmainDataSource") DataSource postmainDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(postmainDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(PostMainDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

}
