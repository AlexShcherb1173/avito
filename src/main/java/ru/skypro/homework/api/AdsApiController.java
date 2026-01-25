package ru.skypro.homework.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Generated;
import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
@Controller
@RequestMapping("${openapi.aPIDocumentation.base-path:}")
public class AdsApiController implements AdsApi {

    private final AdsApiDelegate delegate;

    public AdsApiController(@Autowired(required = false) AdsApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new AdsApiDelegate() {});
    }

    @Override
    public AdsApiDelegate getDelegate() {
        return delegate;
    }

}
