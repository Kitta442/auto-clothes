package com.grinko.autoclothes.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@ToString(includeFieldNames = false, doNotUseGetters = true)
@Builder(builderClassName = "Builder", toBuilder = true)
public class ClothDto {

    private String id;

    private String name;

    private String vendorCode;
}
