package com.crud.a4.boot.hibernate.config;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class HibernateConfig {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	private static final Log log = LogFactory.getLog(HibernateConfig.class);
	
	@Bean
	public SessionFactory getSessionFactory() {
		log.info("***************getSessionFactory**********************");
		if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		} else {
			return entityManagerFactory.unwrap(SessionFactory.class);
		}
	}

}