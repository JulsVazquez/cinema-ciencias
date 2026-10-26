package mx.unam.fciencias.cinema_ciencias.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import static mx.unam.fciencias.cinema_ciencias.security.GatewayAuthenticationFilter.GATEWAY_SECRET_HEADER;
import static mx.unam.fciencias.cinema_ciencias.security.GatewayAuthenticationFilter.ROLES_HEADER;
import static mx.unam.fciencias.cinema_ciencias.security.GatewayAuthenticationFilter.USERNAME_HEADER;
import static mx.unam.fciencias.cinema_ciencias.security.GatewayAuthenticationFilter.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;

class GatewayAuthenticationFilterTest {

    private static final String SECRET = "secreto-compartido-de-prueba";

    private final GatewayAuthenticationFilter filter = new GatewayAuthenticationFilter(SECRET);

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticatesUserWhenGatewaySecretMatches() throws Exception {
        MockHttpServletRequest request = requestWithSecret(SECRET);
        request.addHeader(USER_ID_HEADER, "7");
        request.addHeader(USERNAME_HEADER, "pepe");
        request.addHeader(ROLES_HEADER, "ROLE_REGULAR_USER, ROLE_ADMIN");
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, new MockHttpServletResponse(), chain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(new AuthenticatedUser(7L, "pepe"));
        assertThat(authentication.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_REGULAR_USER", "ROLE_ADMIN");
        assertThat(chain.getRequest()).isNotNull();
    }

    @Test
    void ignoresIdentityHeadersWhenSecretIsMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(USER_ID_HEADER, "7");
        request.addHeader(USERNAME_HEADER, "pepe");
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, new MockHttpServletResponse(), chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(chain.getRequest()).isNotNull();
    }

    @Test
    void ignoresIdentityHeadersWhenSecretIsWrong() throws Exception {
        MockHttpServletRequest request = requestWithSecret("otro-secreto");
        request.addHeader(USER_ID_HEADER, "7");
        request.addHeader(USERNAME_HEADER, "pepe");

        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void ignoresRequestWhenUserIdIsNotNumeric() throws Exception {
        MockHttpServletRequest request = requestWithSecret(SECRET);
        request.addHeader(USER_ID_HEADER, "no-es-un-numero");
        request.addHeader(USERNAME_HEADER, "pepe");

        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void ignoresRequestWhenUsernameIsMissing() throws Exception {
        MockHttpServletRequest request = requestWithSecret(SECRET);
        request.addHeader(USER_ID_HEADER, "7");

        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    private MockHttpServletRequest requestWithSecret(String secret) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(GATEWAY_SECRET_HEADER, secret);
        return request;
    }
}
