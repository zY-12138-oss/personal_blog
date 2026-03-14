package com.blog.personal_blog.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CspFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        String cspPolicy = "default-src 'self'; " +
                "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                "style-src 'self' 'unsafe-inline'; " +
                "img-src 'self' data: blob: http: https:; " +
                "font-src 'self' data:; " +
                "connect-src 'self' http: https:; " +
                "frame-ancestors 'self'; " +
                "form-action 'self';";
        
        response.setHeader("Content-Security-Policy", cspPolicy);
        
        filterChain.doFilter(request, response);
    }
}
