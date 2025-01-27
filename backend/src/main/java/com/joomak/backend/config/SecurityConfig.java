package com.joomak.backend.config;

import com.joomak.backend.token.JwtAccessDeniedHandler;
import com.joomak.backend.token.JwtAuthenticationEntryPoint;
import com.joomak.backend.token.JwtAuthenticationEntryPointV2;
import com.joomak.backend.token.JwtAuthenticationFilter;
import com.joomak.backend.token.JwtSecurityConfig;
import com.joomak.backend.token.JwtTokenProviderV2;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProviderV2 jwtTokenProviderV2;
    private final JwtAuthenticationEntryPointV2 jwtAuthenticationEntryPointV2;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(JwtTokenProviderV2 jwtTokenProviderV2,
        JwtAuthenticationEntryPointV2 jwtAuthenticationEntryPointV2,
        JwtAccessDeniedHandler jwtAccessDeniedHandler) {

        this.jwtTokenProviderV2 = jwtTokenProviderV2;
        this.jwtAuthenticationEntryPointV2 = jwtAuthenticationEntryPointV2;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
                .antMatchers(
                    "/h2-console/**",
                    "/favicon.ico"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPointV2)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            .and()
                .headers()
                    .frameOptions()
                    .sameOrigin()
            .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                    .antMatchers("/swagger-ui/*").permitAll()
            .and()
                .apply(new JwtSecurityConfig(jwtTokenProviderV2));
    }

}
