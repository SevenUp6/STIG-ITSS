package com.xjrsoft.common.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageOutput<T> implements Serializable {

    @JsonProperty(value = "Total")
    public int total;

    @JsonProperty(value = "Rows")
    public List<T> rows;
}
