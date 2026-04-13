package ru.avito.support;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@UtilityClass
public class TestSecurityUtils {

    public RequestPostProcessor httpBasic(String username, String password) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(username, password);
    }

    public String basicAuthHeaderValue(String username, String password) {
        String token = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }

    public HttpHeaders basicAuthHeaders(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, basicAuthHeaderValue(username, password));
        return headers;
    }
}