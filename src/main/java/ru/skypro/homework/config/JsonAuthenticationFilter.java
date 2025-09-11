package ru.skypro.homework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.Map;

public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if ("application/json".equalsIgnoreCase(request.getContentType())) {
            try {
                // Парсим JSON из тела запроса
                Map<String, String> authData = objectMapper.readValue(
                        request.getInputStream(),
                        Map.class
                );

                String username = authData.get("username");
                String password = authData.get("password");

                // Создаем токен аутентификации
                UsernamePasswordAuthenticationToken authRequest =
                        new UsernamePasswordAuthenticationToken(username, password);

                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);

            } catch (IOException e) {
                throw new RuntimeException("Failed to parse authentication request", e);
            }
        } else {
            // Стандартная обработка формы
            return super.attemptAuthentication(request, response);
        }
    }
}