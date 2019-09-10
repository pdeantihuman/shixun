package wiki.pde.shixun.shopping.shoppingcart.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author john
 * 配置 JWT 入口，重载 AuthenticationEntryPoint
 * 这个 class 的主要作用是提供 Error Handling
 * Error handling 特指 AuthenticationDenied Error
 */
@Slf4j
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
    /**
     * called if authentication failed
     * 当 authentication 失败时调用该方法进行错误处理，返回错误响应
     * @param request 请求体
     * @param response 响应体
     * @param e AuthenticationException, 收到该异常时触发该方法进行错误处理
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) {
        log.error("Unauthorized error. Message - {}", e.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
