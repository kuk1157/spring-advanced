package org.example.expert.aop.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "accesslog")
public class AccessLog extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "로그 고유번호")
    private Long id;

    @Column(name="methodName")
    @Comment(value = "메서드 이름")
    private String methodName;

    @Column(name="loginId")
    @Comment(value = "[로그인정보] - 고유번호(id)")
    private Long loginId;

    @Column(name="loginEmail")
    @Comment(value = "[로그인정보] - 이메일")
    private String loginEmail;

    @Column(name="loginUserRole")
    @Comment(value = "[로그인정보] - 회원권한")
    private String loginUserRole;

    @Column(name="requestUrl")
    @Comment(value = "API 요청 URL")
    private String requestUrl;

    @Column(name="requestMethod")
    @Comment(value = "METHOD TYPE")
    private String requestMethod;

    @Column(name="jwtToken")
    @Comment(value = "jwt 토큰")
    private String jwtToken;


    public AccessLog(String methodName, Long loginId, String loginEmail, String loginUserRole, String requestUrl, String requestMethod, String jwtToken) {
        this.methodName = methodName;
        this.loginId = loginId;
        this.loginEmail = loginEmail;
        this.loginUserRole = loginUserRole;
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.jwtToken = jwtToken;
    }

}





