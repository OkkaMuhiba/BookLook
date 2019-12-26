package com.future.booklook.configuration;

import com.future.booklook.security.CustomUserDetailsService;
import com.future.booklook.security.JwtAuthenticationEntryPoint;
import com.future.booklook.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
   @Autowired
   private CustomUserDetailsService userDetailsService;

   @Autowired
   private JwtAuthenticationEntryPoint unauthorizedHandler;

   @Bean
   public JwtAuthenticationFilter jwtAuthenticationFilter(){
       return new JwtAuthenticationFilter();
   }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/api/admin/**"
                )
                .hasRole("ADMIN")
                .antMatchers(
                        "/api/transactions/market/**",
                        "/api/products/edit/**",
                        "/api/markets/edit/**",
                        "/api/products/market/auth/all")
                .hasRole("MARKET")
                .antMatchers(
                        "/api/users/**",
                        "/api/buckets/**",
                        "/api/wishlists/**",
                        "/api/transactions/user/**"
                ).hasRole("USER")
                .antMatchers(
                        "/api/files/books/**"
                ).authenticated()
                .anyRequest().permitAll();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
