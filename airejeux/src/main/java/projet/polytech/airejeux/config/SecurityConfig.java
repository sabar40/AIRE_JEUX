package projet.polytech.airejeux.config;

import projet.polytech.airejeux.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Import de BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder; // Import du PasswordEncoder

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // Injection du JwtFilter via le constructeur
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Déclaration explicite du AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Bean PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Déclaration de BCryptPasswordEncoder
    }

    // Définir SecurityFilterChain au lieu de WebSecurityConfigurerAdapter
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Désactive CSRF
            .authorizeRequests()
            .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()  // Accès ouvert pour login et register
            .anyRequest().authenticated()  // Authentification requise pour toutes les autres requêtes
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  // Ajoute le filtre JWT

        return http.build();
    }
}
