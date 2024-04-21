package com.grinko.autoclothes.handlers;

import com.grinko.autoclothes.dao.model.ClothTuple;
import com.grinko.autoclothes.model.ClothDto;

public final class MapUtil {

    private MapUtil() {
    }

    public static ClothDto mapDto(final ClothTuple t) {

        return ClothDto.builder()
            .id(t.getId())
            .name(t.getName())
            .vendorCode(t.getVendorCode())
            .build();
    }

}
