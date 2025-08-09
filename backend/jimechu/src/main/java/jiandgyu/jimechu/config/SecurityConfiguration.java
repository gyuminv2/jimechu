package jiandgyu.jimechu.config;

import jiandgyu.jimechu.config.security.service.CustomUserDetailService;
import jiandgyu.jimechu.config.security.jwt.JwtAuthenticationFilter;
import jiandgyu.jimechu.config.security.jwt.JwtTokenProvider;
import jiandgyu.jimechu.config.security.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement
                -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",           // 기존: Swagger UI
                                "/swagger-ui.html",         // 추가: Swagger UI HTML
                                "/v3/api-docs/**",          // 추가: OpenAPI 3.0 문서
                                "/api-docs/**",             // 기존: API 문서 (유지)
                                "/swagger-resources/**",    // 추가: Swagger 리소스
                                "/webjars/**",              // 추가: Swagger UI 정적 파일
                                "/api/topics",
                                "/api/topics/*/menus",
                                "/api/auth/refresh").permitAll()
//                        .requestMatchers("/api/members/**").hasRole("Member")
                        .anyRequest().authenticated()
//                        .anyRequest().permitAll()
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, refreshTokenService),
                UsernamePasswordAuthenticationFilter.class
                )
                .userDetailsService(userDetailsService);
        return http.build();
    }
}
