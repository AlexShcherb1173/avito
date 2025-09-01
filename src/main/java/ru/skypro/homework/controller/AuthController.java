package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.responseDto.JwtResponse;
import ru.skypro.homework.security.JwtTokenUtil;
import ru.skypro.homework.service.AuthService;

@Slf4j
@RestController

public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        try {
            System.out.println("🔐 Попытка входа: " + login.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login.getUsername(),
                            login.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);

            System.out.println("✅ Успешная аутентификация, токен сгенерирован");
            return ResponseEntity.ok(new JwtResponse(token));

        } catch (BadCredentialsException e) {
            System.out.println("❌ Неверные учётные данные");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (DisabledException e) {
            System.out.println("❌ Пользователь отключён");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is disabled");
        } catch (Exception e) {
            System.out.println("❌ Ошибка аутентификации: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {
        try {
            authService.register(register);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
