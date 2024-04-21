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
class AllClothesGet {

    private final ClothDao dao;

    public Action0 action(final Consumer<ClothDto> consumer) {

        return () -> dao.fetchAllCloths(
            t -> consumer.accept(mapDto(t))
        );
    }
}
