package com.example.customs.repository;

import com.example.customs.entity.Unp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnpRepository extends JpaRepository<Unp, Long> {
    Optional<Unp> findByUnp(String unp);

}
