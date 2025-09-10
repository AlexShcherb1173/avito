package ru.skypro.homework.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Сервис по извлечению из контекста безопасности логина аутентифицированного пользователя
 */
@Service
public class SecurityServiceImpl {

    public String getAuthenticatedUserName() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    // SecurityContext — ключевой компонент Spring Security. Он хранит информацию об аутентификации текущего пользователя.
    // Эта информация обычно сохраняется в ThreadLocal, что делает ее доступной на протяжении всего жизненного цикла
    // обработки запроса.
    // SecurityContextHolder предоставляет доступ к SecurityContext.
    // Пример: Получение имени текущего аутентифицированного пользователя из SecurityContext.
    // Расширенный пример: Реализация пользовательского SecurityContextRepository для сохранения SecurityContext между
    // запросами.

    // Authentication:
    // Ключевые понятия
    // - Authentication (Аутентификация): Представляет пользователя, которого нужно аутентифицировать. Содержит
    //   информацию, такую как идентификатор пользователя (principal) и его учетные данные (credentials).
    // - AuthenticationManager: Центральный компонент, отвечающий за аутентификацию пользователя. Делегирует задачу
    //   одному или нескольким экземплярам AuthenticationProvider.
    // - AuthenticationProvider: Интерфейс, определяющий контракт для аутентификации объекта Authentication.
    // - AuthenticationException: Исключение времени выполнения, выбрасываемое при неудачной аутентификации.
    // - UserDetails: Интерфейс, представляющий основную информацию о пользователе. Обычно используется для загрузки
    //   пользовательских данных.
    // - UserDetailsService: Интерфейс, определяющий единственный метод loadUserByUsername, который загружает
    //   UserDetails для заданного имени пользователя.
    // Процесс аутентификации:
    // 1. Пользователь пытается войти в систему, предоставляя учетные данные (например, имя пользователя и пароль).
    // 2. Приложение создает объект Authentication (обычно UsernamePasswordAuthenticationToken), содержащий учетные
    //    данные пользователя.
    // 3. AuthenticationManager получает объект Authentication.
    // 4. AuthenticationManager перебирает свои настроенные экземпляры AuthenticationProvider.
    // 5. Каждый AuthenticationProvider проверяет, поддерживает ли он тип объекта Authentication.
    // 6. Если провайдер поддерживает тип Authentication, он пытается аутентифицировать пользователя.
    // 7. Если аутентификация успешна, провайдер возвращает полностью заполненный объект Authentication, включая
    //    предоставленные права (роли/разрешения).
    // 8. Если аутентификация не удалась, провайдер выбрасывает AuthenticationException.
    // 9. Если ни один провайдер не может аутентифицировать объект Authentication, AuthenticationManager может выбросить
    //    AuthenticationException.
    // 10. Аутентифицированный объект Authentication сохраняется в SecurityContext.

    // При создании пользовательских провайдеров аутентификации при неудачной аутентификации у вас есть два варианта:
    // 1. Вернуть null: Это указывает AuthenticationManager попробовать следующий AuthenticationProvider в цепочке.
    // 2. Выбросить AuthenticationException: Это немедленно сигнализирует о неудаче аутентификации и предотвращает
    //    попытки AuthenticationManager использовать другие провайдеры.
    // Выбор зависит от требований вашего приложения. Если у вас несколько провайдеров аутентификации и вы хотите
    // позволить им попытаться аутентифицировать пользователя, верните null. Если вы хотите немедленно отклонить попытку
    // аутентификации, выбросьте AuthenticationException.

    // AuthorizationManager в Spring Security отвечает за принятие решений о контроле доступа. Он определяет, следует ли
    // предоставлять доступ к определенному ресурсу для заданной аутентификации. Интерфейс AuthorizationManager
    // относительно прост: он принимает Supplier<Authentication> и объект Object, представляющий защищаемый ресурс
    // (например, HttpServletRequest, вызов метода), и возвращает AuthorizationDecision.
    // AuthorizationDecision — это простой объект, который указывает, предоставлен ли доступ, запрещен или воздержан.
    // Воздержание позволяет другим экземплярам AuthorizationManager принять решение.

    // Ключевые концепции:
    // - Supplier : Предоставляет доступ к текущему объекту Authentication. Это позволяет AuthorizationManager проверять
    //   роли пользователя, права доступа и другие атрибуты.
    // - Object : Представляет защищаемый объект. Это может быть HttpServletRequest для веб-запросов, вызов метода для
    //   защиты на уровне методов или любой другой ресурс, который вы хотите защитить.
    // - AuthorizationDecision: Результат проверки авторизации. Содержит логическое значение, указывающее, предоставлен
    //   ли доступ или запрещен.
    // - ReactiveAuthorizationManager : Специализированная версия AuthorizationManager для реактивных приложений,
    //   использующих Spring WebFlux. Возвращает Mono<AuthorizationDecision>.
    // Стандартные менеджеры авторизации:
    // Spring Security предоставляет несколько встроенных реализаций AuthorizationManager, таких как:
    // - PreAuthorize: Использует выражения Spring Expression Language (SpEL) для определения правил авторизации.
    // - AuthenticatedAuthorizationManager: Предоставляет доступ аутентифицированным пользователям.
    // - DenyAllAuthorizationManager: Всегда запрещает доступ.
    // - AuthorityAuthorizationManager: Проверяет, есть ли у пользователя определенное право доступа.
    // - RoleAuthorizationManager: Проверяет, есть ли у пользователя определенная роль.
    // Эти стандартные реализации часто достаточны для простых сценариев авторизации. Однако для более сложных требований
    // потребуется создать собственный AuthorizationManager.

    // Доступ к деталям аутентификации:
    // Объект Authentication предоставляет доступ к principal, credentials и authorities аутентифицированного
    // пользователя. Вы можете использовать эту информацию для кастомизации логики обработки успеха на основе ролей или
    // других атрибутов пользователя.
    // Пример:
    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //        http
    //            .authorizeHttpRequests(authorize -> authorize
    //                 .requestMatchers("/admin/**").hasRole("ADMIN")
    //                 .anyRequest().authenticated()
    //            )
    //            .formLogin(form -> form
    //                 .successHandler((request, response, authentication) -> {
    //                     UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    //                     String username = userDetails.getUsername();
    //                     Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    //                     System.out.println("Пользователь " + username + " вошел с ролями: " + authorities);
    //                     response.sendRedirect("/home");
    //                 })
    //                 .permitAll()
    //            );
    //        return http.build();
    // }
    //
    // Этот пример извлекает объект UserDetails из объекта Authentication и получает имя пользователя и его authorities.
    // Эта информация затем логируется в консоль и может использоваться для кастомизации URL перенаправления.

    // Создание пользовательского провайдера аутентификации:
    // Чтобы создать пользовательский AuthenticationProvider, необходимо реализовать интерфейс AuthenticationProvider.
    // Этот интерфейс определяет два метода:
    // •	Authentication authenticate(Authentication authentication) throws AuthenticationException;
    // •	boolean supports(Class<?> authentication);
    // Реализация метода authenticate:
    // Метод authenticate содержит основную логику аутентификации. Он принимает объект Authentication и пытается
    // аутентифицировать пользователя.
    // Вот базовый пример пользовательского AuthenticationProvider, который аутентифицирует пользователей по простому
    // хранилищу в памяти:

    // @Component
    // public class CustomAuthenticationProvider implements AuthenticationProvider {
    //    private final Map<String, String> userCredentials = Map.of(
    //            "user1", "password",
    //            "user2", "secret"
    //    );
    //
    //    @Override
    //    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    //        String username = authentication.getName();
    //        String password = authentication.getCredentials().toString();
    //
    //        if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
    //            // Аутентификация успешна
    //            return new UsernamePasswordAuthenticationToken(
    //                    username,
    //                    password,
    //                    List.of(new SimpleGrantedAuthority("ROLE_USER")) // Назначение ролей/прав
    //            );
    //        } else {
    //            // Аутентификация не удалась
    //            return null; // Или выбросить AuthenticationException или более конкретное исключение
    //            // throw new BadCredentialsException("Неверное имя пользователя или пароль");
    //        }
    //    }
    //
    //    @Override
    //    public boolean supports(Class<?> authentication) {
    //        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    //    }
    // }
    // Пояснение:
    // - Метод authenticate извлекает имя пользователя и пароль из объекта Authentication.
    // - Он проверяет, существует ли имя пользователя в userCredentials и совпадает ли предоставленный пароль с
    //   сохраненным.
    // - Если аутентификация успешна, создается новый UsernamePasswordAuthenticationToken с именем пользователя,
    //   паролем и списком предоставленных прав (в данном случае роль "ROLE_USER").
    // - Если аутентификация не удалась, возвращается null. Возврат null сигнализирует AuthenticationManager, что
    //   этот провайдер не может аутентифицировать пользователя, и следует попробовать следующий провайдер в цепочке.
    //   Альтернативно можно выбросить AuthenticationException, чтобы сразу указать на неудачу.
    // - Аннотация @Component делает этот класс управляемым Spring бином, позволяя автоматически обнаруживать и
    //   внедрять его.
    // Реализация метода supports():
    // Метод supports() определяет, может ли AuthenticationProvider обрабатывать определенный тип объекта Authentication.
    // Он возвращает true, если провайдер поддерживает данный тип Authentication, и false в противном случае.
    // В примере выше метод supports проверяет, является ли объект Authentication экземпляром
    // UsernamePasswordAuthenticationToken. Это означает, что провайдер может обрабатывать аутентификацию на основе
    // имени пользователя и пароля.


}
