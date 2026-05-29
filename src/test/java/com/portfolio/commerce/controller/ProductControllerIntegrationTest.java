package com.portfolio.commerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.commerce.dto.ProductRequest;
import com.portfolio.commerce.entity.Role;
import com.portfolio.commerce.entity.User;
import com.portfolio.commerce.repository.UserRepository;
import com.portfolio.commerce.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateAndListProductWithAdminToken() throws Exception {
        var user = userRepository.findByEmail("it-admin@commerce.dev")
                .orElseGet(() -> userRepository.save(new User(
                        "it-admin@commerce.dev",
                        passwordEncoder.encode("Admin@123"),
                        Set.of(Role.ADMIN)
                )));
        var token = jwtService.generate(user.getEmail(), user.getRoles());
        var request = new ProductRequest("Monitor", "27 inch monitor", new BigDecimal("1299.90"), 8);

        mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Monitor")));

        mockMvc.perform(get("/api/v1/products")
                        .param("name", "Monitor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name", is("Monitor")));
    }
}
