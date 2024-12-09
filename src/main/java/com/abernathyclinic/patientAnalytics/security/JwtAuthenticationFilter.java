package com.abernathyclinic.patientAnalytics.security;

import com.abernathyclinic.patientAnalytics.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * A filter that intercepts incoming HTTP requests to validate and process
 * JSON Web Tokens (JWT) for authentication.
 * <p>
 * This filter ensures that only requests with valid JWTs proceed to the application.
 * If the token is invalid, the response is set to 401 (Unauthorized).
 * </p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    /**
     * Constructs a new {@code JwtAuthenticationFilter} with the specified {@link JwtService}.
     *
     * @param jwtService the service used to validate and extract data from JWTs.
     */
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Filters incoming HTTP requests to validate the JWT from the `Authorization` header.
     * <p>
     * If the JWT is valid, it sets the authentication information in the {@link SecurityContextHolder}.
     * If the JWT is invalid or missing, the request is either rejected with a 401 status
     * or passed along the filter chain unmodified.
     * </p>
     *
     * @param request     the incoming HTTP request.
     * @param response    the outgoing HTTP response.
     * @param filterChain the filter chain for further processing the request.
     * @throws ServletException if an error occurs while processing the request.
     * @throws IOException      if an I/O error occurs while processing the request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        if (jwtService.isTokenValid(token)) {
            String username = jwtService.extractUsername(token);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username, null, List.of(new SimpleGrantedAuthority("ROLE_Organizer")) // Adjust roles if necessary
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
