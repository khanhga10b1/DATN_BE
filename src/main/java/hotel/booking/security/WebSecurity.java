package hotel.booking.security;

import hotel.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;
import java.util.*;
import java.util.stream.Collectors;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    public static String[] PERMIT_URLS = {"/v2/api-docs", "/configuration/**", "/swagger*/**","/swagger-ui.html", "/v2/api-docs", "/swagger-resources/**", "/webjars/springfox-swagger-ui/**", "/webjars/**",
            "/auth/**", "/test/**", "/reservations/**", "/rating/**"};

    public static Map<HttpMethod, List<String>> PERMIT_URL_WITH_METHOD  = new HashMap<>();

    static  {
        PERMIT_URL_WITH_METHOD.put(HttpMethod.GET, Arrays.asList("/hotels/**", "/rooms/**"));
    }


    private final PasswordEncoder passwordEncoder;
    private final UserService userDetailService;
    private final JwtAuthenticationEntryPoint entryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public WebSecurity(PasswordEncoder passwordEncoder, UserService userDetailService, JwtAuthenticationEntryPoint entryPoint, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailService = userDetailService;
        this.entryPoint = entryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers(PERMIT_URLS).permitAll()
                .antMatchers(HttpMethod.GET, PERMIT_URL_WITH_METHOD.get(HttpMethod.GET).toArray(new String[0])).permitAll()
                .antMatchers().permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(entryPoint);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
