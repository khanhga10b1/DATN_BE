package hotel.booking.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hotel.booking.domain.CustomError;
import hotel.booking.domain.ResponseDataAPI;
import hotel.booking.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final OrRequestMatcher requestMatcher = new OrRequestMatcher(Arrays.stream(WebSecurity.PERMIT_URLS)
            .map(AntPathRequestMatcher::new).collect(Collectors.toList()));

    @Autowired
    private TokenProvider tokenProvider;

    /**
     * @param object
     * @return String
     * @throws JsonProcessingException
     * @description convert object to Json
     */
    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!requestMatcher.matches(request)) {
            String token = tokenProvider.resolveToken(request);
            try {
                if (token != null && tokenProvider.validateToken(token)) {
                    Authentication auth = tokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (CustomException ex) {
                SecurityContextHolder.clearContext();
                response.setStatus(ex.getHttpStatus().value());
                response.setContentType("application/json");
                response.getWriter().write(convertObjectToJson(ResponseDataAPI.builder()
                        .success(false)
                        .error(Collections.singletonList(new CustomError(null, ex.getCode(), ex.getMessage())))
                        .build()));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
