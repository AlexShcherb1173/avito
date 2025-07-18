package ru.skypro.homework.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://host.docker.internal:3000",
                        "https://localhost:3000"  // На будущее, если перейдёте на HTTPS
                )
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public FilterRegistrationBean<InvalidRequestFilter> invalidRequestFilter() {
        FilterRegistrationBean<InvalidRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new InvalidRequestFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

    static class InvalidRequestFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain)
                throws ServletException, IOException {

            try {
                logRequestDetails(request);

                if (isInvalidRequest(request)) {
                    handleInvalidRequest(response, "Invalid HTTP request");
                    return;
                }

                filterChain.doFilter(request, response);

            } catch (IllegalArgumentException e) {
                handleInvalidRequest(response, "Invalid character in method name. "
                        + "Ensure you're using HTTP (not HTTPS) for local development");
            }
        }

        private void logRequestDetails(HttpServletRequest request) {
            System.out.printf("Request: %s %s %s%n",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getProtocol());
        }

        private boolean isInvalidRequest(HttpServletRequest request) {
            String method = request.getMethod();
            return method == null ||
                    !method.matches("GET|POST|PUT|DELETE|PATCH|OPTIONS");
        }

        private void handleInvalidRequest(HttpServletResponse response, String message)
                throws IOException {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.getWriter().write(message);
            System.err.println("Blocked invalid request: " + message);
        }
    }
}