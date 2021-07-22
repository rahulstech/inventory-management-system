/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 first_name last_name
 */
package ucf.assignments;

import java.util.Objects;

public class InventoryItem {

    private String serialNo;
    private float value;
    private String name;

    public InventoryItem(String serialNo, float value, String name) {
        this.serialNo = serialNo;
        this.value = value;
        this.name = name;
    }

    public InventoryItem() {}

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryItem)) return false;
        InventoryItem that = (InventoryItem) o;
        return Objects.equals(serialNo, that.serialNo);
    }

    public boolean equalsContent(InventoryItem o) {
        if (null == o) return false;
        if (this == o) return true;
        return this.value == o.value &&
                this.serialNo.equals(o.serialNo) &&
                this.name.equals(o.name);
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "serialNo='" + serialNo + '\'' +
                ", value=" + value +
                ", name='" + name + '\'' +
                '}';
    }
}
