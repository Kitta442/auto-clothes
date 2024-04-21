package com.grinko.autoclothes.dao.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@ToString(includeFieldNames = false, doNotUseGetters = true)
@Builder(builderClassName = "Builder", toBuilder = true)
public class ClothTuple {

    private String id;

    private String name;

    private String vendorCode;
}
