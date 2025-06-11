package com.example.customs.service;

import com.example.customs.dto.UserDTO;
import com.example.customs.entity.User;
import com.example.customs.mapper.UserMapper;
import com.example.customs.repository.UserRepository;
import com.example.customs.util.UnpValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VerificationService verificationService;
    private final UserMapper userMapper; // 💡 подключили маппер


    public UserDTO register(UserDTO dto) {
        log.info("Попытка регистрации участника с УНП: {}", dto.getUnp());

        if (!UnpValidator.isValid(dto.getUnp())) {
            log.warn("УНП {} не прошёл контрольную проверку", dto.getUnp());
            throw new IllegalArgumentException("УНП не прошёл контрольную проверку.");
        }

        if (userRepository.findByUnp(dto.getUnp()).isPresent()) {
            log.warn("Пользователь с УНП {} уже зарегистрирован", dto.getUnp());
            throw new IllegalArgumentException("Пользователь с таким УНП уже существует.");
        }

        boolean isVerified = verificationService.verifyUNP(dto.getUnp());

        User user = userMapper.toEntity(dto);
        user.setVerified(isVerified);
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }
}
