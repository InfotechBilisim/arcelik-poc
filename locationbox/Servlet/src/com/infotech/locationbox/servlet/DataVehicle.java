package com.infotech.locationbox.servlet;

public class DataVehicle {
    private long id = 0;
    private String alias = null;
    private int capacity = 0;

    public DataVehicle() {
    }

    public DataVehicle(long id, String alias, int capacity) {
        this.id = id;
        this.alias = alias;
        this.capacity = capacity;
    }

    public DataVehicle(String id, String alias, String capacity) {
        this.id = Long.parseLong(id);
        this.alias = alias;
        this.capacity = Integer.parseInt(capacity);
    }

    //-----------------------------------------------------------------------------

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

}
