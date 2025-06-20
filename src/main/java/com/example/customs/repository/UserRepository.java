package com.example.customs.repository;

import com.example.customs.entity.Unp;
import com.example.customs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUnp_Unp(String unp);     // <-- ищет по User.unp.unp
    boolean existsByUnp_Unp(String unp);          // <-- проверяет существование по User.unp.unp
}

