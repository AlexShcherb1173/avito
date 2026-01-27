package ru.skypro.homework.config;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    // @Configuration: Указывает, что этот класс является конфигурационным классом Spring.
    // @EnableWebSecurity: Включает функции веб-безопасности Spring Security.
    // @EnableGlobalMethodSecurity(prePostEnabled = true) включает использование аннотаций @PreAuthorize и @PostAuthorize
    // в контроллерах.
    // С помощью аннотаций @PreAuthorize, @PostAuthorize и @Secured над методами контроллера можно определять более
    // детализированные правила контроля доступа.
    // Например, @PreAuthorize("hasRole('ADMIN')") - гарантирует, что только пользователи с ролью ADMIN могут получить
    // доступ к определенному методу. Проверка прав пользователя выполняется перед выполнением метода.
    // @PreAuthorize("hasRole('USER')") - гарантирует, что только пользователи с ролью USER могут получить доступ к
    // определенному методу.
    // В нашем приложении @PreAuthorize("@adServiceImpl.isAdCreatorOrAdmin(#id)") - гарантирует, что только пользователи,
    // создавшие объявление, или ADMIN-ы могут получить доступ к определенному методу.
    // Аналогично и с аннотацией @PreAuthorize("@commentsServiceImpl.isCommentCreatorOrAdmin(#commentId)")

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/*",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register",
    };

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return userService::loadByUserName;

        // UserDetailsService(): Бин, предоставляющий экземпляр UserDetailsService, необходимый для его интеграции с
        // конкретной моделью пользовательских данных и требованиями аутентификации конкретного приложения.
        // Данные пользователя получаем из базы данных.
    }

    // Этот метод определяет цепочку фильтров безопасности
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                // Включает разрешение для браузера получать веб-странице, загруженной с одного адреса (домена), данные
                // из нашего приложения, загруженного на другом домене. В данном случае такие запросы к нашему приложению
                // будет направлять фронтенд.
                .and()
                .csrf().disable()
                // Отключает встроенную защиту от CSRF, так как это RestAPI-приложение. А для таких приложений не
                // подразумевается, что злоумышленником может быть заполнена какая-то форма с вредоносным кодом и
                // redirect-ом отправлена на сайт, на котором пользователь аутентифицирован в настоящее время.
                .authorizeHttpRequests(authorization -> authorization
                        // Настраивает правила авторизации для различных шаблонов запросов
                                        .mvcMatchers(AUTH_WHITELIST).permitAll()
                                        // URL из данного массива доступны <permitAll()> не аутентифицированным
                                        // пользователям
                                        .mvcMatchers(HttpMethod.GET, "/ads").permitAll()
                                        // Оставляем открытым для не аутентифицированных пользователей <permitAll()>
                                        // доступ к URL "/ads" (метод getAds())
                                        .mvcMatchers("/ads/**", "/users/**").authenticated())
                                        // Закрываем доступ к URL "/ads/**", "/users/**" (методы указанных
                                        // контроллеров) не аутентифицированным пользователям <authenticated()>

                .httpBasic(withDefaults());
                // Включаем базовую HTTP-аутентификация для всех запросов с настройками по умолчанию. Любой запрос к
                // приложению на URL "/ads/**", "/users/**" потребует аутентификации с использованием базового HTTP-метода.
                // Браузер будет запрашивать у пользователя имя и пароль.
        return http.build();

        // Базовая аутентификация — это простая схема аутентификации, встроенная в HTTP-протокол. Клиент отправляет имя
        // пользователя и пароль в заголовке Authorization, закодированные с помощью Base64. Хотя реализация проста,
        // важно понимать её ограничения, особенно в плане безопасности.
        // Как работает базовая аутентификация:
        // 1. Запрос клиента: Клиент (например, веб-браузер или мобильное приложение) отправляет запрос к защищённому
        //    ресурсу на сервере.
        // 2. Ответ сервера (401 Unauthorized): Если клиент не аутентифицирован, сервер отвечает статусом HTTP 401
        //    Unauthorized и заголовком WWW-Authenticate. Этот заголовок сообщает клиенту, что требуется базовая
        //    аутентификация, и указывает realm (описательное имя защищённой области).
        // 3. Аутентификация клиента: Клиент запрашивает у пользователя имя и пароль (если они ещё не предоставлены).
        // 4. Заголовок Authorization: Клиент кодирует имя пользователя и пароль (разделённые двоеточием) с помощью
        //    Base64 и включает их в заголовок Authorization последующих запросов. Заголовок выглядит так:
        //    Authorization: Basic <Base64-кодированные имя:пароль>.
        // 5. Аутентификация сервера: Сервер декодирует Base64-строку, извлекает имя пользователя и пароль, и
        //    аутентифицирует пользователя в базе данных или другом механизме аутентификации.
        // 6. Ответ сервера (200 OK или 403 Forbidden): Если аутентификация успешна и у пользователя есть права доступа
        //    к ресурсу, сервер отвечает статусом 200 OK и запрошенным ресурсом. Если аутентификация не удалась или у
        //    пользователя нет необходимых ролей/прав, сервер отвечает статусом 403 Forbidden.
        // Вопросы безопасности базовой аутентификации
        // - Шифрование: Базовая аутентификация отправляет учётные данные в Base64, что не является шифрованием. Их
        //   легко декодировать. Поэтому всегда используйте базовую аутентификацию поверх HTTPS (TLS) для шифрования
        //   всего соединения, включая учётные данные.
        // - Хранение: Никогда не храните пароли в открытом виде. Используйте стойкие алгоритмы хеширования, такие
        //   как bcrypt, Argon2 или scrypt, для хранения хешей паролей.
        // - Realm: Атрибут realm в заголовке WWW-Authenticate — это описательное имя защищённой области. Выбирайте
        //   realm, который будет информативен для пользователя.
        // - Альтернативы: Для production-сред рассмотрите более безопасные механизмы аутентификации, такие как
        //   OAuth 2.0, OIDC или JWT.

        // CSRF (Cross-Site Request Forgery) — это атака, при которой вредоносный сайт заставляет браузер пользователя
        // выполнять запросы к другому сайту, где пользователь аутентифицирован. Spring Security по умолчанию
        // предоставляет защиту от CSRF.

        // В целях безопасности, браузеры запрещают запросы к другим доменам, используя технику «одного источника»
        // (same-origin policy). Это означает, что веб-страница, загруженная с одного домена, не может получить данные
        // с другого домена без явного разрешения сервера. CORS позволяет серверам указать, с каких доменов разрешено
        // получать данные, обеспечивая безопасность и контроль доступа.
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

        // PasswordEncoder(): Бин, предоставляющий экземпляр PasswordEncoder.
        // BCryptPasswordEncoder — это надежный алгоритм хеширования паролей. Важно использовать PasswordEncoder для
        // кодирования паролей перед их хранением.
    }

}
