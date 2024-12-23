package ${package}.common.security;

import ${package}.common.security.jwt.application.JwtGenerator;
import ${package}.common.security.jwt.infrastructure.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static void secureEndpoints(HttpSecurity http) throws Exception {
        secureAuthRequests(http);

        // Denegar peticiones no autorizadas
        http.authorizeHttpRequests(requests -> requests.anyRequest().denyAll()
        );
    }

    private static void secureAuthRequests(HttpSecurity http) throws Exception {
        // Permitir las peticiones que indiquemos
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(HttpMethod.POST, "/v1/auth/signup").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/auth/login", "/v1/auth/login/jwt").permitAll()
        );
    }


    protected JwtAuthenticationFilter jwtAuthenticationFilter(HttpSecurity http, JwtGenerator jwtGenerator) throws Exception {
        final AuthenticationManager authManager = http.getSharedObject(AuthenticationManager.class);

        return new JwtAuthenticationFilter(authManager, jwtGenerator);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtGenerator jwtGenerator) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = jwtAuthenticationFilter(http, jwtGenerator);

        http
                // Desactivar CSRF porque no usamos
                .csrf(csrf -> csrf.disable())
                // No guardar datos de la sesión del usuario
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Aplicar filtro creado para poder usar JWT
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        secureEndpoints(http);

        return http.build();
    }
}

