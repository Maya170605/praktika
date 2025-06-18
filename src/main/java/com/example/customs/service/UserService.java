package com.example.customs.service;

import com.example.customs.dto.UserDTO;
import com.example.customs.entity.User;
import com.example.customs.exception.CustomException;
import com.example.customs.mapper.UserMapper;
import com.example.customs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VerificationService verificationService;
    private final UserMapper userMapper;


    public UserDTO register(UserDTO dto) {
        log.info("Попытка регистрации участника с УНП: {}", dto.getUnp());
        validateUser(dto);
        log.debug("Валидация пользователя с УНП {} прошла успешно", dto.getUnp());

        boolean isVerified = verificationService.verifyUNP(dto.getUnp());
        log.info("Результат проверки УНП {}: {}", dto.getUnp(), isVerified);

        User user = userMapper.toEntity(dto);
        user.setVerified(isVerified);
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    private void validateUser(UserDTO dto) {
        if (!verificationService.verifyUNP(dto.getUnp())) {
            log.warn("Валидация УНП не пройдена: {}", dto.getUnp());
            throw new CustomException("УНП не прошёл валидацию");
        }

        if (userRepository.findByUnp(dto.getUnp()).isPresent()) {
            log.warn("Пользователь с УНП {} уже существует", dto.getUnp());
            throw new CustomException("Пользователь с таким УНП уже существует");
        }
    }

}
