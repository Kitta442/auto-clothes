package com.grinko.autoclothes.handlers;

import com.grinko.autoclothes.dao.ClothDao;
import com.grinko.autoclothes.model.ClothDto;
import com.grinko.autoclothes.util.Action0;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static com.grinko.autoclothes.handlers.MapUtil.mapDto;

@Component
@RequiredArgsConstructor
public class ClothByIdGet {

    private final ClothDao dao;

    public Action0 action(final String id,
                          final Consumer<ClothDto> consumer) {

        return () -> dao.fetchClothById(
            id,
            t -> consumer.accept(mapDto(t))
        );
    }
}
