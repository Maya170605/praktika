package com.example.customs.service;

import com.example.customs.dto.UserDTO;
import com.example.customs.entity.Unp;
import com.example.customs.entity.User;
import com.example.customs.exception.CustomException;
import com.example.customs.mapper.UserMapper;
import com.example.customs.repository.UnpRepository;
import com.example.customs.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UnpRepository unpRepository;

    @Mock
    private VerificationService verificationService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void register_success() {
        // Arrange
        UserDTO dto = UserDTO.builder()
                .name("Иван")
                .unp("123456789")
                .email("ivan@example.com")
                .activityType("Производство")
                .build();

        Unp unp = new Unp(1L, "123456789");
        User userEntity = User.builder()
                .name("Иван")
                .email("ivan@example.com")
                .activityType("Производство")
                .verified(true)
                .build();

        User savedUser = User.builder()
                .name("Иван")
                .email("ivan@example.com")
                .activityType("Производство")
                .verified(true)
                .build();

        // Mocks
        when(verificationService.verifyUNP("123456789")).thenReturn(true);
        when(unpRepository.findByUnp("123456789")).thenReturn(Optional.of(unp));
        when(userRepository.existsByUnp_Unp("123456789")).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(dto);

        // Act
        UserDTO result = userService.register(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Иван", result.getName());
        assertEquals("123456789", result.getUnp());
        verify(userRepository).save(userEntity);
    }

    @Test
    void register_shouldThrow_whenUNPInvalidFormat() {
        UserDTO dto = UserDTO.builder()
                .name("Test")
                .unp("12345")  // некорректный формат
                .email("test@example.com")
                .activityType("Торговля")
                .build();

        when(verificationService.verifyUNP("12345")).thenReturn(false);

        CustomException ex = assertThrows(CustomException.class, () -> userService.register(dto));
        assertTrue(ex.getMessage().contains("должно быть 9 цифр"));
    }

    @Test
    void register_shouldThrow_whenUNPNotFoundInDirectory() {
        UserDTO dto = UserDTO.builder()
                .name("Test")
                .unp("987654321")
                .email("test@example.com")
                .activityType("Услуги")
                .build();

        when(verificationService.verifyUNP("987654321")).thenReturn(true);
        when(unpRepository.findByUnp("987654321")).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> userService.register(dto));
        assertTrue(ex.getMessage().contains("не найден в справочнике"));
    }

    @Test
    void register_shouldThrow_whenUserAlreadyExists() {
        UserDTO dto = UserDTO.builder()
                .name("Test")
                .unp("111222333")
                .email("test@example.com")
                .activityType("Логистика")
                .build();

        when(verificationService.verifyUNP("111222333")).thenReturn(true);
        when(unpRepository.findByUnp("111222333")).thenReturn(Optional.of(new Unp()));
        when(userRepository.existsByUnp_Unp("111222333")).thenReturn(true);

        CustomException ex = assertThrows(CustomException.class, () -> userService.register(dto));
        assertTrue(ex.getMessage().contains("уже существует"));
    }
}
