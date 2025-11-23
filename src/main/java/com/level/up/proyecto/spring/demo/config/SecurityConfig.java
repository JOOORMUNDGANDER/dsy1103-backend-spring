package com.level.up.proyecto.spring.demo.config;

import com.level.up.proyecto.spring.demo.security.jwt.JwtAuthEntryPoint;
import com.level.up.proyecto.spring.demo.security.jwt.JwtAuthenticationFilter;
import com.level.up.proyecto.spring.demo.security.services.UsuarioDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioDetailsServiceImpl usuarioDetailsService;

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configure(http)) // Habilitar CORS
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // Para H2 Console
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Permitir todas las peticiones OPTIONS (preflight CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Rutas públicas de autenticación
                        .requestMatchers("/api/auth/**").permitAll()

                        // Swagger/OpenAPI - público
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // H2 Console - solo en desarrollo
                        .requestMatchers("/h2-console/**").permitAll()

                        // Productos - lectura pública, escritura requiere autenticación
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/productos/**").hasAnyAuthority("ROL_ADMIN", "ROL_MODERADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAnyAuthority("ROL_ADMIN", "ROL_MODERADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/productos/**").hasAnyAuthority("ROL_ADMIN", "ROL_MODERADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasAuthority("ROL_ADMIN")

                        // Carrito - requiere autenticación
                        .requestMatchers("/api/carrito/**").authenticated()

                        // Pedidos - requiere autenticación
                        .requestMatchers("/api/pedidos/**").authenticated()

                        // Usuarios - administración requiere rol ADMIN
                        .requestMatchers("/api/usuarios/**").hasAuthority("ROL_ADMIN")

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
