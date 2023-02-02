package com.xjrsoft.common.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public interface CacheAble extends Serializable {
    @JsonIgnore
    String getCacheId();
}
