package com.yunhalee.database.multidatabase.config

import jakarta.persistence.EntityManagerFactory
import org.hibernate.cfg.AvailableSettings
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.hibernate5.SpringBeanContainer
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    basePackages = ["com.yunhalee.database.multidatabase.team.*"],
    entityManagerFactoryRef = "defaultEntityManagerFactory",
    transactionManagerRef = "defaultTransactionManager",
)
class ADatabaseJpaConfig {

    @Primary
    @Bean("aEntityManagerFactory")
    fun defaultEntityManagerFactory(
        @Qualifier("aDataSource") dataSource: DataSource,
        jpaProperties: JpaProperties,
        hibernateProperties: HibernateProperties,
        builder: EntityManagerFactoryBuilder,
        beanFactory: ConfigurableListableBeanFactory,
    ): LocalContainerEntityManagerFactoryBean {
        val build = builder
            .dataSource(dataSource)
            .packages(
                "com.yunhalee.database.multidatabase.team"
            )
            .persistenceUnit("a")
            .properties(hibernateProperties.determineHibernateProperties(jpaProperties.properties, HibernateSettings()))
            .build()
        build.jpaPropertyMap[AvailableSettings.BEAN_CONTAINER] = SpringBeanContainer(beanFactory)
        return build
    }

    @Bean("aTransactionManager")
    fun defaultTransactionManager(
        @Qualifier("aEntityManagerFactory") entityManagerFactory: EntityManagerFactory,
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}
