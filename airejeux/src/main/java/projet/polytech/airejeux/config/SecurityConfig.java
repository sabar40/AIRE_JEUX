package projet.polytech.airejeux.config;

import projet.polytech.airejeux.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.http.SessionCreationPolicy;
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
        http
            // 1. Désactiver CSRF (la nouvelle façon)
            .csrf(csrf -> csrf.disable())
            
            // 2. Définir la gestion de session sur STATELESS (crucial pour le JWT)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 3. Remplacer "authorizeRequests()" par "authorizeHttpRequests()"
            .authorizeHttpRequests(auth -> auth
                // Vos règles (inchangées)
                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll() 
                .anyRequest().authenticated()
            )
            
            // 4. Ajouter votre filtre (inchangé)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
