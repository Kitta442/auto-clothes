package com.grinko.autoclothes.web.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/v1/cloth")
public interface ClothControllerApi {

    @RequestMapping(
        method = RequestMethod.GET,
        value = "/cloths",
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    ResponseEntity<List<ClothJson>> allClothes();

    @RequestMapping(
        method = RequestMethod.GET,
        value = "/cloth",
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    ResponseEntity<ClothJson> clothById(
        @RequestParam(value = "id") String id
    );
}
