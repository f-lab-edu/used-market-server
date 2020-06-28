package com.market.server.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.market.server")
@EnableTransactionManagement // DataSourceTransactionManager Bean을 Transaction Manager로 사용
public class DatabaseConfig {

    @Bean
    // DataSource class: 커넥션풀에는 여러개의 Connection 객체가 생성되어 운용되는데, 이를 직접 웹 애플리케이션에서 다루기 힘들기 때문에 DataSource라는 개념을 도입하여 사용합니다.
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // TypeAlias로 설정할 클래스들이 있는 패키지를 설정하면 DTO에 @Alias("aliasName")으로 typeAlias를 설정 가능
        sessionFactory.setTypeAliasesPackage("com.market.server.dto.");

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/*.xml"));
        return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        return sqlSessionTemplate;
    }


}


