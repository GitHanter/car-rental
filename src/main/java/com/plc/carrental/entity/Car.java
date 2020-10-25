package com.plc.carrental.entity;

import java.util.List;

public class Car extends AuditObject {
    private String carBrand;
    private String carModel;
    private CarType carType;
    private CarColor color;
    private Short capacity;
    private String plateNumber;
    private Integer mileage;

    private List<CarDailyPrice> prices;

    public Car() {
    }

    public Car(Long id) {
        super(id);
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public CarColor getColor() {
        return color;
    }

    public void setColor(CarColor color) {
        this.color = color;
    }

    public Short getCapacity() {
        return capacity;
    }

    public void setCapacity(Short capacity) {
        this.capacity = capacity;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public List<CarDailyPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<CarDailyPrice> prices) {
        this.prices = prices;
    }
}
