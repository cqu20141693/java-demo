package com.gow.security.config;

import com.gow.security.authenticate.AccessDeniedAuthenticationHandler;
import com.gow.security.authenticate.AuthenticateFailureHandler;
import com.gow.security.authenticate.AuthenticateSuccessHandler;
import com.gow.security.jwt.JWTAuthenticationFilter;
import com.gow.security.jwt.JWTUser;
import com.gow.security.privisioning.CustomizeUserDetailsService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.util.Assert;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author gow
 * @date 2021/7/24
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticateSuccessHandler successHandler;

    @Autowired
    private CustomizeUserDetailsService userDetailsService;

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AccessDeniedAuthenticationHandler accessDeniedAuthenticationHandler;

    /**
     * config http
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http.authorizeRequests()
                // authorize config
                //无需权限访问
                .antMatchers("/allow/**", "/error404").permitAll()
                // need user role
                .antMatchers("/users/**").hasRole("user")
                //其他接口需要登录后才能访问
                .anyRequest().authenticated()
                // config access deny handler
//                .and().exceptionHandling().accessDeniedHandler(accessDeniedAuthenticationHandler)
                // config rememberMe
                .and()
                .rememberMe()
                .rememberMeServices(new NullRememberMeServices())
                // config session ,disable session
                .and().sessionManagement()
                .sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy())
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // login config
                .and()
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/myLogin.html")
                .loginProcessingUrl("/doLogin")
                .defaultSuccessUrl("/index.html")
//                .authenticationDetailsSource()
                // use restful
                .failureHandler(new AuthenticateFailureHandler())
                .successHandler(successHandler)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                // logout config
                .and()
                .logout()
                .clearAuthentication(true)
//                .logoutSuccessHandler()
                .logoutUrl("/logout")
                .deleteCookies("token")
                .logoutSuccessUrl("/homePage")
                // config http security
                .and()
                .csrf().disable()
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .headers().frameOptions().disable();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(Duration.ofHours(1));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * config authenticate manager
     *
     * @param builder
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {

        builder.userDetailsService(userDetailsService);
        // super.configure(builder);
        JWTUser gow = new JWTUser("00000001", "gow", "{noop}123", roles("admin", "user"));
        JWTUser sang = new JWTUser("00000002", "sang", "{noop}123", roles("user"));
        userDetailsService.addUser(gow);
        userDetailsService.addUser(sang);

//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(gow);
//        manager.createUser(sang);
//        // config UserDetailsManager
//        builder.userDetailsService(manager);
        // config AuthenticationProvider
        //  builder.authenticationProvider(new CustomAuthenticationProvider());
    }

    public List<GrantedAuthority> roles(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList(roles.length);
        String[] var3 = roles;
        int var4 = roles.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String role = var3[var5];
            Assert.isTrue(!role.startsWith("ROLE_"), () -> {
                return role + " cannot start with ROLE_ (it is automatically added)";
            });
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        return authorities;
    }


}
