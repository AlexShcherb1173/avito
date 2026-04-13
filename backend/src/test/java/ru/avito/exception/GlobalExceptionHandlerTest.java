package ru.avito.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/bad")
        public void bad() {
            throw new BadRequestException("Bad request happened");
        }

        @GetMapping("/empty")
        public void empty() {
            throw new BadRequestException("");
        }

        @GetMapping("/unauthorized")
        public void unauthorized() {
            throw new UnauthorizedException("Unauthorized happened");
        }

        @GetMapping("/forbidden")
        public void forbidden() {
            throw new ForbiddenException("Access denied");
        }

        @GetMapping("/notfound")
        public void notfound() {
            throw new NotFoundException("Not found");
        }

        @GetMapping("/file")
        public void file() {
            throw new FileStorageException("File storage failed");
        }

        @GetMapping("/illegal")
        public void illegal() {
            throw new IllegalArgumentException("Illegal argument");
        }

        @GetMapping("/runtime")
        public void runtime() {
            throw new RuntimeException("Unexpected error");
        }

        @GetMapping("/missing-part")
        public void missingPart() throws MissingServletRequestPartException {
            throw new MissingServletRequestPartException("image");
        }

        @GetMapping("/unsupported-media")
        public void unsupportedMedia() throws HttpMediaTypeNotSupportedException {
            throw new HttpMediaTypeNotSupportedException("text/plain");
        }

        @GetMapping("/max-upload")
        public void maxUpload() {
            throw new MaxUploadSizeExceededException(1024);
        }

        @PostMapping("/validation")
        public void validation(@Valid @RequestBody Req req) {
        }
    }

    static class Req {
        @NotBlank(message = "name must not be blank")
        @Size(min = 3, message = "name size must be at least 3")
        public String name;
    }

    @Test
    void handleBadRequestShouldReturn400() throws Exception {
        mockMvc.perform(get("/test/bad"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bad request happened"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void emptyMessage() throws Exception {
        mockMvc.perform(get("/test/empty"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void handleUnauthorizedShouldReturn401() throws Exception {
        mockMvc.perform(get("/test/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthorized happened"))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void handleForbiddenShouldReturn403() throws Exception {
        mockMvc.perform(get("/test/forbidden"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"))
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void handleNotFoundShouldReturn404() throws Exception {
        mockMvc.perform(get("/test/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void handleFileStorageShouldReturn500() throws Exception {
        mockMvc.perform(get("/test/file"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("File storage failed"))
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    void handleIllegalArgumentShouldReturn400() throws Exception {
        mockMvc.perform(get("/test/illegal"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Illegal argument"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void handleValidationShouldReturn400() throws Exception {
        Req req = new Req();
        req.name = "";

        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("name")))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void handleMissingPartShouldReturn400() throws Exception {
        mockMvc.perform(get("/test/missing-part"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing request part: image"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void handleMediaTypeShouldReturn415() throws Exception {
        mockMvc.perform(get("/test/unsupported-media"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.message").value("Unsupported content type"))
                .andExpect(jsonPath("$.status").value(415));
    }

    @Test
    void handleMaxUploadShouldReturn400() throws Exception {
        mockMvc.perform(get("/test/max-upload"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("File is too large"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void handleGenericExceptionShouldReturn500() throws Exception {
        mockMvc.perform(get("/test/runtime"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal server error"))
                .andExpect(jsonPath("$.status").value(500));
    }
}