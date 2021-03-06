package com.heuristify.mdu.pojo;


public class Bluetooth {
    private String device_name;
    private String device_address;

    public Bluetooth() {
    }

    public Bluetooth(String device_name, String device_address) {
        this.device_name = device_name;
        this.device_address = device_address;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_address() {
        return device_address;
    }

    public void setDevice_address(String device_address) {
        this.device_address = device_address;
    }
}
