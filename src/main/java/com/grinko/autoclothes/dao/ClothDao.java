package com.grinko.autoclothes.dao;

import com.grinko.autoclothes.dao.model.ClothTuple;

import java.util.function.Consumer;

public interface ClothDao {

    void fetchAllCloths(final Consumer<ClothTuple> consumer);

    void fetchClothById(final String id, final Consumer<ClothTuple> consumer);
}
