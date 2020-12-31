package com.sun.autoconfigure;

import com.sun.condition.SqlSyncSendCondition;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


/**
 * 自定注入mybatis sql 打印插件
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class MybatisAutoSqlConfiguration {
	@Bean
//	@Profile({"dev"})// 设置 dev  环境开启
	@Conditional(value = SqlSyncSendCondition.class)	// 验证sql同步是否开启
	public Interceptor getInterceptor(){ 
		return new MybatisAutoSql();
	} 
    
}