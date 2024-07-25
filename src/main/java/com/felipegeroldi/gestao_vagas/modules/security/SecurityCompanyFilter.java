package com.felipegeroldi.gestao_vagas.modules.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.felipegeroldi.gestao_vagas.providers.JWTProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityCompanyFilter extends OncePerRequestFilter {

    private JWTProvider jwtProvider;

    public SecurityCompanyFilter(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/company")) {
            SecurityContextHolder.getContext().setAuthentication(null);
            String header = request.getHeader("Authorization");
    
            if(header != null) {
                var token = jwtProvider.validateToken(header);
                if(token == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
    
                request.setAttribute("company_id", token.getSubject());

                var roles = token.getClaim("roles").asList(Object.class);

                var grants = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
                    .toList();

                var auth = new UsernamePasswordAuthenticationToken(token.getSubject(), null, grants);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
    
        }
        
        filterChain.doFilter(request, response);
    }
    
}
