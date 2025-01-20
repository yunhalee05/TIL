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
    basePackages = ["com.yunhalee.database.multidatabase"],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.REGEX,
            pattern = ["com.yunhalee.database.multidatabase.team.*"],
        ),
    ],
    entityManagerFactoryRef = "defaultEntityManagerFactory",
    transactionManagerRef = "defaultTransactionManager",
)
class DefaultDatabaseJpaConfig {

    @Primary
    @Bean("defaultEntityManagerFactory")
    fun defaultEntityManagerFactory(
        @Qualifier("defaultDataSource") dataSource: DataSource,
        jpaProperties: JpaProperties,
        hibernateProperties: HibernateProperties,
        builder: EntityManagerFactoryBuilder,
        beanFactory: ConfigurableListableBeanFactory,
    ): LocalContainerEntityManagerFactoryBean {
        val build = builder
            .dataSource(dataSource)
            .packages(
                "com.yunhalee.database.multidatabase.user"
            )
            .persistenceUnit("default")
            // 이때 jpaProperties의 경우 ddl auto 설정과 같은 hibernate 설정은 가지고 있지 않기 때문에 해당 설정의 사용을 원한다면, hibernate 설정으로 감싸서 설정해야한다.
            .properties(hibernateProperties.determineHibernateProperties(jpaProperties.properties, HibernateSettings()))
            .build()

        // AttributeConverter와 같은 등록된 빈을 가져오기 위해 SpringBeanContainer를 설정
        // 설정하지 않으면 AttributeConverter를 찾지 못해 NoSuchMethodException가 발생한다.
        build.jpaPropertyMap[AvailableSettings.BEAN_CONTAINER] = SpringBeanContainer(beanFactory)
        return build
    }

    @Primary
    @Bean("defaultTransactionManager")
    fun defaultTransactionManager(
        @Qualifier("defaultEntityManagerFactory") entityManagerFactory: EntityManagerFactory,
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}
