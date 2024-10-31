package org.example.expert.aop;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.expert.aop.entity.AccessLog;
import org.example.expert.aop.repository.AccessLogRepository;
import org.example.expert.config.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j(topic = "AdminAccessLogAop")
@Aspect
@Component
@RequiredArgsConstructor
public class AdminAccessLogAop {

    private final JwtUtil jwtUtil;
    private final AccessLogRepository accessLogRepository;

    @Pointcut("execution(* org.example.expert.domain.comment.service.CommentAdminService*.*(..))")
    public void adminDeleteAccessLog() {}

    @Pointcut("execution(* org.example.expert.domain.user.service.UserAdminService*.*(..))")
    public void adminChangeAccessLog() {}

    @Before(" adminChangeAccessLog() || adminDeleteAccessLog() ")
    public void beforeAdminAccessLog(JoinPoint joinPoint) {
        // [로그 찍을 값 가공]
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object[] args = joinPoint.getArgs();
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        String requestId = request.getRequestId();
        String requestToken = request.getHeader("Authorization");
        String token = jwtUtil.substringToken(requestToken);
        Claims claims = jwtUtil.extractClaims(token);
        Long loginId = Long.parseLong(claims.getSubject());
        String loginEmail = (String) claims.get("email");
        String loginUserRole = (String) claims.get("userRole");
        Method method = aopMethod(joinPoint);

        // [로그 찍기]
        log.info("API MethodName : {}", method.getName());
        log.info("API requestUrl : {}", requestURI);
        log.info("API MethodType : {}", requestMethod);
        log.info("API requestId : {}", requestId);
        log.info("API jwtToken : {}", requestToken);
        log.info("[로그인정보] - 고유번호(ID) : {}", loginId);
        log.info("[로그인정보] - 이메일 : {}", loginEmail);
        log.info("[로그인정보] - 회원 권한 : {}", loginUserRole);
        log.info("API 요청 날짜 : {}", nowDate);
        log.info("API 요청 시간 : {}", nowTime);
        log.info("param(id) : {}", args[0]);

        // [최종 테이블에 저장]
        AccessLog accessLog = new AccessLog(method.getName(),loginId,loginEmail,loginUserRole,requestURI,requestMethod,requestToken);
        accessLogRepository.save(accessLog);

    }
    private Method aopMethod(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
