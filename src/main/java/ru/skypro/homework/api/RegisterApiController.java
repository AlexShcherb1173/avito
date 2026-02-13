package ru.skypro.homework.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Generated;
import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
@Controller
@RequestMapping("${openapi.aPIDocumentation.base-path:}")
public class RegisterApiController implements RegisterApi {

    private final RegisterApiDelegate delegate;

    public RegisterApiController(@Autowired(required = false) RegisterApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new RegisterApiDelegate() {});
    }

    @Override
    public RegisterApiDelegate getDelegate() {
        return delegate;
    }

}
