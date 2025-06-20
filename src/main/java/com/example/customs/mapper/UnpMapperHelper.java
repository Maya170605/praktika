package com.example.customs.mapper;

import com.example.customs.entity.Unp;
import com.example.customs.repository.UnpRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnpMapperHelper {

    @Autowired
    private UnpRepository unpRepository;

    @Named("mapUnpFromString")
    public Unp mapUnpFromString(String unpStr) {
        return unpRepository.findByUnp(unpStr)
                .orElseThrow(() -> new IllegalArgumentException("УНП " + unpStr + " не найден в базе данных"));
    }
}
