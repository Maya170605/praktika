package com.example.customs.controller;

import com.example.customs.dto.UserDTO;
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
import static org.hamcrest.Matchers.hasItem;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clearDb() {
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    void shouldRegisterUserSuccessfully() throws Exception {
        UserDTO dto = new UserDTO("Компания А", "123456789", "a@example.com", "Логистика");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unp").value("123456789"))
                .andExpect(jsonPath("$.name").value("Компания А"))
                .andExpect(jsonPath("$.email").value("a@example.com"))
                .andExpect(jsonPath("$.activityType").value("Логистика"));
        // .andExpect(jsonPath("$.verified").value(true)); // <- убрано, поле отсутствует в DTO
    }

    @Test
    @Order(2)
    void shouldFailIfUserWithSameUNPExists() throws Exception {
        UserDTO dto = new UserDTO("Компания Б", "987654321", "b@example.com", "Экспорт");
        userRepository.save(new com.example.customs.entity.User(null, "Компания Б", "987654321", "b@example.com", "Экспорт", true));

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
                .andExpect(jsonPath("$.errors", hasItem("Название обязательно")))
                .andExpect(jsonPath("$.errors", hasItem("УНП обязателен")))
                .andExpect(jsonPath("$.errors", hasItem("Вид активности обязателен")));

    }
}
