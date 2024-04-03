package net.skhu.tastyinventory_be.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.skhu.tastyinventory_be.util.CookieUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
public class StatelessCSRFFilter extends OncePerRequestFilter {
    public static final String CSRF_TOKEN = "CSRF-TOKEN";
    public static final String X_CSRF   _TOKEN = "X-CSRF-TOKEN";
    private final RequestMatcher requireCsrfProtectionMatcher = new DefaultRequiresCsrfMatcher();
    private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        if (requireCsrfProtectionMatcher.matches(request)) {
            Optional<String> optCsrfToken = Optional.ofNullable(request.getHeader(X_CSRF_TOKEN));
            Optional<Cookie> optCsrfCookie = CookieUtils.getCookie(request, CSRF_TOKEN);

            log.debug(optCsrfToken.orElse("CSRF 토큰 헤더가 존재하지 않습니다."));
            log.debug(optCsrfCookie.isPresent() ? optCsrfCookie.get().getValue() : "CSRF 쿠키가 존재하지 않습니다.");

            if (optCsrfCookie.isEmpty() || optCsrfToken.isEmpty() || !optCsrfToken.get().equals(optCsrfCookie.get().getValue())) {
                accessDeniedHandler.handle(request, response, new AccessDeniedException("CSRF 토큰이 유효하지 않습니다."));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    public static final class DefaultRequiresCsrfMatcher implements RequestMatcher {
        private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

        @Override
        public boolean matches(HttpServletRequest request) {
            return !allowedMethods.matcher(request.getMethod()).matches();
        }
    }
}
