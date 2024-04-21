package com.grinko.autoclothes.web;

import com.grinko.autoclothes.handlers.ClothHandlers;
import com.grinko.autoclothes.model.ClothDto;
import com.grinko.autoclothes.util.MutableHolder;
import com.grinko.autoclothes.web.api.ClothControllerApi;
import com.grinko.autoclothes.web.api.ClothJson;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Timed
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class ClothController implements ClothControllerApi {

    private final ClothHandlers handlers;

    private static ClothJson dto2json(final ClothDto dto) {

        return new ClothJson()
            .setId(dto.getId())
            .setName(dto.getName())
            .setVendorCode(dto.getVendorCode());
    }

    @Override
    public ResponseEntity<List<ClothJson>> allClothes() {

        var res = new ArrayList<ClothJson>();
        handlers
            .allClothes(dto -> res.add(dto2json(dto)))
            .run();
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<ClothJson> clothById(final String id) {

        var res = new MutableHolder<ClothJson>();
        handlers
            .clothById(id, dto -> res.accept(dto2json(dto)))
            .run();
        return ResponseEntity.ok(res.get());
    }
}
