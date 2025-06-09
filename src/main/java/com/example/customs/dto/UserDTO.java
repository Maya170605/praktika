package com.example.customs.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotBlank(message = "Название обязательно")
    private String name;

    @NotBlank(message = "УНП обязателен")
    @Pattern(regexp = "\\d{9}", message = "Ровно 9 цифр")
    private String unp;

    @Email(message = "Неверный тип email")
    private String email;

    @NotBlank(message = "Вид активности обязателен")
    private String activityType;
}
