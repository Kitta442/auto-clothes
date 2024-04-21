package com.grinko.autoclothes.handlers;

import com.grinko.autoclothes.model.ClothDto;
import com.grinko.autoclothes.util.Action0;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClothHandlers {

    private final AllClothesGet allClothesGet;
    private final ClothByIdGet clothByIdGet;

    public Action0 allClothes(final Consumer<ClothDto> consumer) {

        return allClothesGet.action(consumer);
    }

    public Action0 clothById(final String id,
                             final Consumer<ClothDto> consumer) {
        return clothByIdGet.action(id, consumer);
    }
}
