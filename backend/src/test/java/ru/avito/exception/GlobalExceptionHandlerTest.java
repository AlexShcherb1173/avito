package ru.avito.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/bad")
        public void bad() {
            throw new BadRequestException("Bad request happened");
        }

        @GetMapping("/forbidden")
        public void forbidden() {
            throw new ForbiddenException("Access denied");
        }

        @GetMapping("/notfound")
        public void notfound() {
            throw new NotFoundException("Not found");
        }

        @GetMapping("/illegal")
        public void illegal() {
            throw new IllegalArgumentException("Illegal argument");
        }

        @GetMapping("/runtime")
        public void runtime() {
            throw new RuntimeException("Unexpected error");
        }

        @PostMapping("/validation")
        public void validation(@Valid @RequestBody Req req) {
        }
    }

    static class Req {
        @NotBlank(message = "name must not be blank")
        public String name;
    }

    @Test
    void badRequest() throws Exception {
        mockMvc.perform(get("/test/bad"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bad request happened"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void forbidden() throws Exception {
        mockMvc.perform(get("/test/forbidden"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"))
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void notFound() throws Exception {
        mockMvc.perform(get("/test/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void illegalArgument() throws Exception {
        mockMvc.perform(get("/test/illegal"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Illegal argument"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void runtime() throws Exception {
        mockMvc.perform(get("/test/runtime"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal server error"))
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    void validation() throws Exception {
        Req req = new Req();
        req.name = "";

        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("name")))
                .andExpect(jsonPath("$.status").value(400));
    }
}