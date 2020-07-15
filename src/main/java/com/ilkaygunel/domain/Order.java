package com.ilkaygunel.domain;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String id;
    private List items;

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
