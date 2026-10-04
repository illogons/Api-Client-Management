package com.gonzalovega.clientmanagement.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import com.gonzalovega.clientmanagement.models.AuditLogModel;
import com.gonzalovega.clientmanagement.repository.AuditLogRepository;
import com.gonzalovega.clientmanagement.security.SecurityUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditLogAspect {

    private final AuditLogRepository auditRepository;
    private final SecurityUtils securityUtils;

    /**
     * Pointcut that matches all create, update, and delete methods in service implementations.
     * This pointcut will be used to apply the audit logging around these methods.
     */
    @Pointcut("execution(* com.gonzalovega.clientmanagement.services.implementations.*.create*(..)) || " +
            "execution(* com.gonzalovega.clientmanagement.services.implementations.*.update*(..)) || " +
            "execution(* com.gonzalovega.clientmanagement.services.implementations.*.delete*(..))")
    public void auditMethods() {}

    /**
     * Around advice that profiles the execution of service methods and logs audit information.
     * - Captures the start time, user information, HTTP method, URI, and method details.
     * - Proceeds with the method execution and captures the result or any exceptions thrown.
     * - Asynchronously saves an audit log entry to the database with the captured information.
     *
     * @param joinPoint the join point representing the method being audited
     * @return the result of the method execution
     * @throws Throwable if the method execution throws an exception
     */
    @Around("auditMethods()")
    public Object profileAndAudit(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String user = securityUtils.getCurrentUsername();

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String httpMethod = (attr != null) ? attr.getRequest().getMethod() : "N/A";
        String uri = (attr != null) ? attr.getRequest().getRequestURI() : "N/A";
        String method = joinPoint.getSignature().getName();
        String entity = joinPoint.getTarget().getClass().getSimpleName().replace("ServiceImpl", "");

        try {
            Object result = joinPoint.proceed();
            saveLogAsync(httpMethod, method, entity, uri, user, System.currentTimeMillis() - start, "SUCCESS");
            return result;
        } catch (Throwable e) {
            saveLogAsync(httpMethod, method, entity, uri, user, System.currentTimeMillis() - start, "FAILED | " + e.getMessage());
            throw e;
        }
    }

    /**
     * Asynchronously saves an audit log entry to the database.
     *
     * @param httpMethod the HTTP method of the request
     * @param method the name of the method being audited
     * @param entity the name of the entity being acted upon
     * @param endpoint the URI of the endpoint being accessed
     * @param username the username of the authenticated user performing the action
     * @param executionTime the time taken to execute the method in milliseconds
     * @param details additional details about the execution (e.g., success or failure)
     */
    @Async
    protected void saveLogAsync(String httpMethod, String method, String entity, String endpoint, String username, Long executionTime, String details) {
        AuditLogModel audit = AuditLogModel.builder()
                .httpMethod(httpMethod).method(method).entityName(entity).endpoint(endpoint)
                .username(username)
                .executionTime(executionTime).details(details).timestamp(LocalDateTime.now()).build();
        auditRepository.save(audit);
    }
}