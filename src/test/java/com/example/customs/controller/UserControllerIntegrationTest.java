package com.example.customs.controller;

import com.example.customs.dto.UserDTO;
import com.example.customs.entity.Unp;
import com.example.customs.entity.User;
import com.example.customs.repository.UnpRepository;
import com.example.customs.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnpRepository unpRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String VALID_UNP = "123456789";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        unpRepository.deleteAll();
        unpRepository.save(Unp.builder().unp(VALID_UNP).build());
        unpRepository.save(Unp.builder().unp("987654321").build());
    }

    @Test
    @Order(1)
    void shouldRegisterUserSuccessfully() throws Exception {
        UserDTO dto = new UserDTO("Компания А", VALID_UNP, "a@example.com", "Логистика");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unp").value(VALID_UNP))
                .andExpect(jsonPath("$.name").value("Компания А"))
                .andExpect(jsonPath("$.email").value("a@example.com"))
                .andExpect(jsonPath("$.activityType").value("Логистика"));
    }

    @Test
    @Order(2)
    void shouldFailIfUserWithSameUNPExists() throws Exception {
        Unp unp = unpRepository.findByUnp("111111111").orElseThrow();
        userRepository.save(User.builder()
                .name("Компания Б")
                .unp(unp)
                .email("b@example.com")
                .activityType("Экспорт")
                .verified(true)
                .build());

        UserDTO dto = new UserDTO("Компания Б", "111111111", "b@example.com", "Экспорт");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("уже существует")));
    }

    @Test
    @Order(3)
    void shouldRejectInvalidUNPFormat() throws Exception {
        UserDTO dto = new UserDTO("Компания С", "abc", "c@example.com", "Импорт");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Ровно 9 цифр")));
    }

    @Test
    @Order(4)
    void shouldRejectEmptyFields() throws Exception {
        UserDTO dto = new UserDTO("", "", "", "");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Название обязательно")))
                .andExpect(content().string(containsString("УНП обязателен")))
                .andExpect(content().string(containsString("Вид активности обязателен")));
    }
}
