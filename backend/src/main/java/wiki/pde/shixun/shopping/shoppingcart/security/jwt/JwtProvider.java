package wiki.pde.shixun.shopping.shoppingcart.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author john
 * JWT Provider
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProvider {

    /**
     * jwt secret
     */
    private String jwtSecret;
    /**
     * jwt token 默认过期时间
     */
    private int jwtExpiration;

    public String generate(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * 验证 jwt 签名
     * @param token 从 http 请求中提取出的 jwt token
     * @return true-签名为真, false-签名为假
     */
    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("JWT Authentication Failed");
        }
        return false;
    }

    /**
     * 根据 token 计算出用户 id 即
     * @param token jwt token
     * @return 返回 jwt payload 中包含的用户 id
     */
    public String getUserAccount(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
                .getBody().getSubject();
    }
}
