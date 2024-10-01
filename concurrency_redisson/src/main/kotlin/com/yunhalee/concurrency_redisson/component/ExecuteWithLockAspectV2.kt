package com.yunhalee.concurrency_redisson.component

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.util.concurrent.TimeUnit

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ExecuteWithLockV2(
    val key: String,
    val waitTimeAmount: Long,
    val leaseTimeAmount: Long,
    val timeUnit: TimeUnit
)

@Aspect
@Component
class ExecuteWithLockAspectV2(
    private val redissonClient: RedissonClient,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    private val parser: ExpressionParser = SpelExpressionParser()

    @Around("@annotation(com.yunhalee.concurrency_redisson.component.ExecuteWithLockV2)")
    fun executeWithRedissonLock(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        val annotation = getAnnotation(proceedingJoinPoint)
        val key = getKey(proceedingJoinPoint, annotation)
        val lock = redissonClient.getLock(key.toString())
        try {
            acquireLock(lock, annotation)
            return proceedingJoinPoint.proceed()
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            throw RuntimeException("Lock 획득 중 InterruptedException이 발생하여 삭제를 중단합니다.", e)
        } finally {
            if (lock.isLocked && lock.isHeldByCurrentThread) {
                applicationEventPublisher.publishEvent(lock)
            }
        }
    }

    private fun getKey(proceedingJoinPoint: ProceedingJoinPoint, annotation: ExecuteWithLockV2): Any {
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

    private fun getAnnotation(proceedingJoinPoint: ProceedingJoinPoint): ExecuteWithLockV2 {
        val methodSignature: MethodSignature = proceedingJoinPoint.signature as MethodSignature
        return methodSignature.method.getAnnotation(ExecuteWithLockV2::class.java)
    }

    private fun acquireLock(lock: RLock, executeWithLockAnnotation: ExecuteWithLockV2) {
        if (!lock.tryLock(executeWithLockAnnotation.waitTimeAmount, executeWithLockAnnotation.leaseTimeAmount, executeWithLockAnnotation.timeUnit)) {
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

    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun unLockRedissonLock(rLock: RLock) {
        rLock.unlock()
    }
}
