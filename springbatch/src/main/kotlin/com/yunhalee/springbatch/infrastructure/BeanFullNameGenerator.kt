package com.yunhalee.springbatch.infrastructure

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanNameGenerator
import org.springframework.context.annotation.AnnotationBeanNameGenerator
import org.springframework.util.StringUtils

/**
 * Spring Bean 생성시 기본전략은 ClassName 을 활용한다.
 * 따라서 ClassName 이 중복되면 Bean 이 생성되지 않는 이슈있다.
 * 이는 프로젝트 규모가 커짐에 따라 package 로 version 관리를 할때 ClassName 에 Version 을 지정하거나 BeanName 을 일일이 커스텀 해줘야 하는 이슈로 연결된다.
 * 이 Generator 는 아래와 같이 동작해 위 문제를 해결한다.
 * 기본 BeanName 생성 정책은 packageName + className
 * Annotation 으로 재정의 했다면 재정의한 이름을 사용
 */
class FullBeanNameGenerator : BeanNameGenerator, AnnotationBeanNameGenerator() {

    override fun generateBeanName(definition: BeanDefinition, registry: BeanDefinitionRegistry): String {
        if (definition is AnnotatedBeanDefinition) {
            val beanName = super.determineBeanNameFromAnnotation(definition)
            if (StringUtils.hasText(beanName)) {
                return beanName!!
            }
        }

        return definition.beanClassName!!
    }
}