package ru.skypro.homework.service.impl;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.entity.User;

@RequiredArgsConstructor
public class UserSecurityDetails implements UserDetails {

    // Создаем пользовательский класс UserSecurityDetails.
    // Этот класс хранит имя пользователя, пароль, authorities (роли) и флаги состояния учетной записи. Конструктор
    // принимает user-a в качестве аргумента. Класс содержит методы по возвращению соответствующих полей.

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * @return true, если не истек срок действия аккаунта, иначе - false
     */

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return true, если аккаунт не заблокирован, иначе - false
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return true, если не истек срок действия учетных данных, иначе - false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return true, если есть доступ, иначе - false
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Реализация пользовательского сервиса UserDetails является ключевым аспектом Spring Security, позволяя интегрировать
    // его с конкретной моделью пользовательских данных и требованиями аутентификации вашего приложения. В этом пoдрaздeле
    // мы рассмотрим, как этого достичь с помощью Lambda DSL, представленного в Spring Security 6.1, что обеспечивает
    // более лаконичную и читаемую конфигурацию по сравнению с традиционными подходами на основе XML. Мы изучим основные
    // интерфейсы, продемонстрируем практические стратегии реализации и выделим преимущества Lambda DSL в этом контексте.

    // Основу процесса аутентификации Spring Security составляют интерфейсы UserDetailsService и UserDetails.
    // - UserDetailsService: Этот интерфейс содержит единственный метод loadUserByUsername(String username), который
    // отвечает за получение информации о пользователе на основе предоставленного имени пользователя (или другого
    // уникального идентификатора). Реализация этого метода обычно взаимодействует с базой данных, сервером LDAP или
    // другим хранилищем пользовательских данных для получения информации о пользователе.
    // - UserDetails: Этот интерфейс представляет основную информацию о пользователе, необходимую Spring Security для
    // аутентификации и авторизации. Он включает:
    // - getUsername(): Возвращает имя пользователя, используемое для аутентификации.
    // - getPassword(): Возвращает пароль пользователя.
    // - getAuthorities(): Возвращает коллекцию объектов GrantedAuthority, представляющих роли или разрешения,
    //   предоставленные пользователю.
    // - isAccountNonExpired(): Указывает, истек ли срок действия учетной записи пользователя.
    // - isAccountNonLocked(): Указывает, заблокирован ли пользователь.
    // - isCredentialsNonExpired(): Указывает, истек ли срок действия учетных данных (пароля) пользователя.
    // - isEnabled(): Указывает, активен ли пользователь.

    // Интерфейс UserDetailsService — это ключевой компонент процесса аутентификации в Spring Security. Он отвечает за
    // загрузку пользовательских данных. Когда пользователь пытается пройти аутентификацию, Spring Security использует
    // UserDetailsService для получения информации о пользователе (имя пользователя, пароль, права доступа) из источника
    // данных.
    // UserDetailsService возвращает объект UserDetails, который представляет аутентифицированного пользователя.
    // Интерфейс UserDetails содержит методы для доступа к имени пользователя, паролю, статусу активности, сроку
    // действия аккаунта, сроку действия учетных данных и правам доступа (ролям/разрешениям).
    // Стандартные реализации:
    // Spring Security предоставляет стандартные реализации UserDetailsService, такие как InMemoryUserDetailsManager,
    // который хранит пользовательские данные в памяти. Однако для большинства приложений потребуется создать
    // пользовательскую реализацию, извлекающую данные пользователя из базы данных или другого постоянного хранилища.
    // Пользовательская реализация UserDetailsService:
    // Для интеграции с пользовательским репозиторием необходимо создать класс, реализующий интерфейс UserDetailsService
    // и переопределяющий метод loadUserByUsername(String username). Этот метод должен извлекать данные пользователя из
    // вашего источника данных на основе предоставленного имени пользователя и возвращать объект UserDetails.

    // Хотя Spring Security предоставляет стандартный класс User, реализующий интерфейс UserDetails, вам может
    // потребоваться создать собственную реализацию для хранения дополнительных атрибутов пользователя или специфических
    // требований приложения.
    // Расширение интерфейса UserDetails:
    // Создайте собственный класс, реализующий интерфейс UserDetails, и добавьте поля для любых дополнительных атрибутов
    // пользователя.
}

