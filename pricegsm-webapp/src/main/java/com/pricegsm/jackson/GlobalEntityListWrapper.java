package com.pricegsm.jackson;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pricegsm.domain.GlobalEntity;

import java.util.List;

/**
 * Short version of global entity list which contain only id and name fields.
 * <pre>
 *     [{'id':1, name:'Black'}, {'id':2, name:'White'}]
 * </pre>
 */
@JsonSerialize(using = GlobalEntityListSerializer.class)
public class GlobalEntityListWrapper {

    private final List<? extends GlobalEntity> list;

    public GlobalEntityListWrapper(List<? extends GlobalEntity> list) {
        this.list = list;
    }

    public List<? extends GlobalEntity> getList() {
        return list;
    }
}
