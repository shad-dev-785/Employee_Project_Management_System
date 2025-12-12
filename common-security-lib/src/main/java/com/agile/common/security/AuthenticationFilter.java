package com.agile.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        String role = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try{
                token = authHeader.substring(7);
                username = jwtUtil.extractUsername(token);
            }catch (Exception e){
                e.getMessage();
                e.printStackTrace();
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if(jwtUtil.validateToken(token)) {
                role = jwtUtil.extractRole(token);
                // You can create an Authentication object here based on username and role
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role);
                List<SimpleGrantedAuthority> updatedAuthorities = List.of(authority);
                System.out.println(">>> DEBUG SECURITY CONTEXT <<<");
                System.out.println("Token Role Raw: " + role);
                System.out.println("Spring Authority Generated: " + authority.getAuthority());
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, updatedAuthorities);
                auth.setDetails(request);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            // Set authentication in the context if needed
        }

        filterChain.doFilter(request, response);
    }
}
