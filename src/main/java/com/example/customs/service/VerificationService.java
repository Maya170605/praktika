package com.example.customs.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificationService {
    private static final List<String> VERIFIED_UNP = List.of("123456783", "568812349",
            "111111116", "411896321", "999555117");

    public boolean verifyUNP(String unp) {
        return VERIFIED_UNP.contains(unp);
    }
}
