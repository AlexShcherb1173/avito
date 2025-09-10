package ru.skypro.homework.filter;


import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        // Это заголовок определяет, разрешены ли для кросс-доменного запроса учетные данные, такие как куки и
        // HTTP-заголовки авторизации.
        // Если значение заголовка установлено в "true", то сервер разрешает использовать учетные данные в запросе между
        // источниками (фронтенд и бэкенд), если браузер также их отправит.
        filterChain.doFilter(httpServletRequest, httpServletResponse);

        // Вы также можете настроить CORS с помощью Filter. Этот подход дает максимальный контроль над конфигурацией
        // CORS, так как позволяет напрямую управлять заголовками запроса и ответа.

        // Этот метод в Spring Security (или в общем, в Java-разработке с использованием HttpServletResponse) добавляет
        // в ответ HTTP-заголовок Access-Control-Allow-Credentials со значением true. Этот заголовок указывает браузеру,
        // что сервер позволяет включать в запросы кросс-доменные запросы с учетными данными.
        // Учетные данные, включаемые в кросс-доменные запросы, могут включать:
        // Cookies
        // TLS-клиентские сертификаты
        // Заголовки аутентификации, содержащие имя пользователя и пароль
        // Когда этот заголовок установлен в true, браузер будет отправлять запросы с учетными данными только в том
        // случае, если соответствующий заголовок присутствует в ответе сервера на предварительный запрос
        // (preflight request). Если заголовок не присутствует или не имеет значения true, браузер будет блокировать
        // запросы с учетными данными, чтобы предотвратить атаки CSRF (межсайтовые подделки запросов).
        // В контексте Spring Security это означает, что ваше приложение готово к обработке запросов с учетными данными
        // от клиентов, что может быть необходимо для обеспечения аутентификации или других функций, требующих доступа
        // к учетным данным пользователя.
        // httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        // Этот код похож на использование аннотации @CrossOrigin в Spring, которая позволяет включить аналогичное
        // поведение путем установки атрибута allowCredentials в значение true.
        // Например:
        // @CrossOrigin(allowCredentials = true)
        // Этот подход позволяет конфигурировать поддержку CORS в приложении Spring более гибко и менее boilerplate-кодом.

        // Что такое FilterChain?
        // FilterChain — это объект, который представляет собой цепочку фильтров. Каждый фильтр в этой цепочке может
        // выполнить какую-либо обработку над запросом или ответом, а затем передать управление следующему фильтру в цепочке.
        // Что делает метод doFilter()?
        // Метод doFilter() — это основной метод интерфейса Filter, который реализует логику фильтра. В этом методе
        // фильтр может изменить или дополнить HTTP-запрос и ответ, а потом вызвать chain.doFilter(), чтобы передать
        // управление следующему фильтру в цепочке или сервлету, если это последний фильтр.
    }
}
