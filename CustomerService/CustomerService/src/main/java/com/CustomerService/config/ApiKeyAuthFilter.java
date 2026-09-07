package com.CustomerService.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    public static final String API_KEY_HEADER = "X-API-KEY";

    @Value("${api.security.key}")
    private String configuredApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String requestApiKey = request.getHeader(API_KEY_HEADER);

        if (requestApiKey == null
                || requestApiKey.isBlank()
                || configuredApiKey == null
                || configuredApiKey.isBlank()
                || !requestApiKey.equals(configuredApiKey)) {

            writeUnauthorizedResponse(response, request.getRequestURI());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeUnauthorizedResponse(
            HttpServletResponse response,
            String path) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put(
                "message",
                "Missing or invalid API key. Provide a valid '"
                        + API_KEY_HEADER + "' header."
        );
        body.put("path", path);

        response.getWriter().write(
                objectMapper.writeValueAsString(body)
        );
    }
}

