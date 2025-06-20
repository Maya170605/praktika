package com.example.customs.service;

import com.example.customs.dto.UserDTO;
import com.example.customs.entity.Unp;
import com.example.customs.entity.User;
import com.example.customs.exception.CustomException;
import com.example.customs.mapper.UserMapper;
import com.example.customs.repository.UnpRepository;
import com.example.customs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UnpRepository unpRepository;
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

        Unp unp = unpRepository.findByUnp(dto.getUnp())
                .orElseThrow(() -> new CustomException("УНП не найден в справочнике"));
        user.setUnp(unp);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    private void validateUser(UserDTO dto) {
        String unpValue = dto.getUnp();

        if (!verificationService.verifyUNP(unpValue)) {
            log.warn("Валидация УНП не пройдена: {}", unpValue);
            throw new CustomException("УНП не прошёл валидацию (должно быть 9 цифр)");
        }

        if (unpRepository.findByUnp(unpValue).isEmpty()) {
            log.warn("УНП {} не найден в справочнике", unpValue);
            throw new CustomException("УНП не найден в справочнике");
        }

        if (userRepository.existsByUnp_Unp(unpValue)) {
            log.warn("Пользователь с УНП {} уже существует", unpValue);
            throw new CustomException("Пользователь с таким УНП уже существует");
        }
    }


}
