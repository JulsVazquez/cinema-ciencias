package mx.unam.fciencias.cinema_ciencias.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

/**
 * Implementa el patrón "Trust the Gateway".
 *
 * El Gateway valida la firma y la expiración del JWT y reenvía la identidad en
 * los headers {@code X-User-*}. Este filtro solo lee esos headers; no calcula
 * firmas ni consulta ninguna tabla de usuarios.
 *
 * Como cualquiera que llegue directo al puerto del monolito podría falsificar
 * esos headers, solo se confía en ellos si además viene el secreto compartido
 * {@code X-Gateway-Secret}. Sin el secreto correcto la petición queda anónima
 * y Spring Security responde 401.
 */
@Component
public class GatewayAuthenticationFilter extends OncePerRequestFilter {

    public static final String USER_ID_HEADER = "X-User-Id";
    public static final String USERNAME_HEADER = "X-User-Name";
    public static final String ROLES_HEADER = "X-User-Roles";
    public static final String GATEWAY_SECRET_HEADER = "X-Gateway-Secret";

    private final byte[] gatewaySecret;

    public GatewayAuthenticationFilter(@Value("${cinema.security.gatewaySecret}") String gatewaySecret) {
        this.gatewaySecret = gatewaySecret.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (comesFromGateway(request)) {
            String rawId = request.getHeader(USER_ID_HEADER);
            String username = request.getHeader(USERNAME_HEADER);

            if (StringUtils.hasText(rawId) && StringUtils.hasText(username)) {
                try {
                    AuthenticatedUser principal = new AuthenticatedUser(Long.valueOf(rawId.trim()), username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    principal, null, parseAuthorities(request.getHeader(ROLES_HEADER)));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (NumberFormatException e) {
                    logger.warn("Header " + USER_ID_HEADER + " con formato inválido; se ignora la identidad.");
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean comesFromGateway(HttpServletRequest request) {
        String provided = request.getHeader(GATEWAY_SECRET_HEADER);
        if (provided == null) {
            return false;
        }
        // Comparación en tiempo constante para no filtrar el secreto por timing.
        return MessageDigest.isEqual(gatewaySecret, provided.getBytes(StandardCharsets.UTF_8));
    }

    private List<GrantedAuthority> parseAuthorities(String rawRoles) {
        if (!StringUtils.hasText(rawRoles)) {
            return List.of();
        }
        return Arrays.stream(rawRoles.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                .toList();
    }
}
