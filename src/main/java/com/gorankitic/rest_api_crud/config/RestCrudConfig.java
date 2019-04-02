package com.gorankitic.rest_api_crud.config;

import java.beans.PropertyVetoException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages="com.gorankitic.rest_api_crud")
@PropertySource("classpath:persistence-mysql.properties")
public class RestCrudConfig {
	
	@Autowired
	private Environment env;	//variable to hold the properties
	
	//napravim logger cisto da mogu lakse da pratim situaciju
	private Logger logger = Logger.getLogger(getClass().getName());
	
	//define a bean for ViewResolver
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();	
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");		
		return viewResolver;
	}
	
	//define a bean for our security datasource
	@Bean
	public DataSource securityDataSource() {
		// create connection pool
		ComboPooledDataSource securityDataSource = new ComboPooledDataSource();
		// set the jdbc driver
		try {
			securityDataSource.setDriverClass("com.mysql.jdbc.Driver");		
		}catch(PropertyVetoException exc) {
			throw new RuntimeException(exc);
		}
		//ispisem url i korisnika, cisto da budem siguran da ispravno citam podatke 
		logger.info("jdbc.url=" + env.getProperty("jdbc.url"));
		logger.info("jdbc.user=" + env.getProperty("jdbc.user"));	
		//set database connection props
		securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		securityDataSource.setUser(env.getProperty("jdbc.user"));
		securityDataSource.setPassword(env.getProperty("jdbc.password"));	
		// set connection pool props
		securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
		securityDataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
		securityDataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
		securityDataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));
		
			return securityDataSource;
		}

	//pomocna metoda, procitam env property i konvertujem u integer
	private int getIntProperty(String propertyName) {
		String propVal = env.getProperty(propertyName);
		//konvertujem u int
		int intPropVal = Integer.parseInt(propVal);
		return intPropVal;
	}
	
	private Properties getHibernateProperties() {
		//set hibernate properties
		Properties props = new Properties();
		props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		return props;				
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory(){
		// create session factory
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		// set the properties
		sessionFactory.setDataSource(securityDataSource());
		sessionFactory.setPackagesToScan(env.getProperty("hiberante.packagesToScan"));
		sessionFactory.setHibernateProperties(getHibernateProperties());
		return sessionFactory;
	}
	
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		// setup transaction manager based on session factory
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		return txManager;
	}
}
