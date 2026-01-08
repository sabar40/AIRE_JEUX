package projet.polytech.airejeux.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import projet.polytech.airejeux.Service.JwtService;

@Component
@WebFilter("/*")
public class JwtFilter implements Filter {

    @Autowired
    private JwtService jwtService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            String header = httpRequest.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                String username = jwtService.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtService.validateToken(token, username)) {
                        
                        // --- MODIFICATION ICI ---
                        // 1. On extrait le rôle (ex: "ADMIN")
                        String role = jwtService.extractRole(token); 
                        
                        // 2. On ajoute le préfixe "ROLE_" indispensable pour hasRole('ADMIN')
                        String authorityName = "ROLE_" + role; 
                        
                        // 3. On crée l'autorité Spring Security
                        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                                new SimpleGrantedAuthority(authorityName)
                        );

                        // 4. On passe les autorités au token d'authentification
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                username, null, authorities);
                        
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        // ------------------------
                    }
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            // En cas d'erreur sur le token, on continue la chaîne sans authentifier
            chain.doFilter(request, response);
        }
    }
}