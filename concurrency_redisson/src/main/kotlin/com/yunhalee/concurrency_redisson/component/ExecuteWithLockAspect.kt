package com.yunhalee.concurrency_redisson.component

import com.yunhalee.concurrency_redisson.infrastructure.annotation.ExecuteWithLock
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Aspect
@Component
private class ExecuteWithLockAspect(
    private val redissonClient: RedissonClient
) {

    private val parser: ExpressionParser = SpelExpressionParser()

    @Around("")
    @Transactional(propagation = Propagation.NEVER)
    fun executeWithRedissonLock(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        val annotation = getAnnotation(proceedingJoinPoint)
        val key = getKey(proceedingJoinPoint, annotation)
        println("------------------------------$key -------------------------------")
        val lock = redissonClient.getLock(key.toString())
        try {
            if (!lock.tryLock(annotation.waitTimeAmount, annotation.leaseTimeAmount, annotation.timeUnit)) {
                println("Lock 획득 실패")
                throw RuntimeException("Lock 획득에 실패하여 삭제를 중단합니다.")
            }
            println("----------------lock 획득 ----------------------------")
            return proceedingJoinPoint.proceed() // 함수 실행
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            throw RuntimeException("Lock 획득 중 InterruptedException이 발생하여 삭제를 중단합니다.", e)
        } finally {
            lock.unlock()
            println("--------------lock 해제됨 --------------------------")
        }
    }

    private fun getKey(proceedingJoinPoint: ProceedingJoinPoint, annotation: ExecuteWithLock): Any {
        val methodSignature: MethodSignature = proceedingJoinPoint.signature as MethodSignature
        val keyExpression = annotation.key
        val args = proceedingJoinPoint.args
        val keyParameterName = getKeyParameterName(keyExpression)
        val idx = methodSignature.parameterNames.indexOf(keyParameterName)

        if (args.size < idx || idx < 0) {
            throw RuntimeException("올바르지 않은 키 값입니다. 파라미터와 키를 확인해주세요.")
        }

        val expression = parser.parseExpression(keyExpression)
        val context = StandardEvaluationContext()
        context.setVariable(keyParameterName, args[idx])
        return expression.getValue(context) ?: throw RuntimeException("올바르지 않은 키값입니다.")
    }

    private fun getAnnotation(proceedingJoinPoint: ProceedingJoinPoint): ExecuteWithLock {
        val methodSignature: MethodSignature = proceedingJoinPoint.signature as MethodSignature
        return methodSignature.method.getAnnotation(ExecuteWithLock::class.java)
    }

    private fun acquireLock(lock: RLock, executeWithLockAnnotation: ExecuteWithLock) {
        if (!lock.tryLock(executeWithLockAnnotation.waitTimeAmount, executeWithLockAnnotation.leaseTimeAmount, executeWithLockAnnotation.timeUnit)) {
            println("Lock 획득 실패")
            throw RuntimeException("Lock 획득에 실패하여 삭제를 중단합니다.")
        }
    }

    private fun getKeyParameterName(keyExpression: String): String {
        val prefix = "#"
        val appender = "."
        return keyExpression.split(prefix)
            .getOrNull(1)
            ?.substringBefore(appender) ?: throw RuntimeException("Invalid key expression")
    }
}
