package com.plc.carrental.dto;

import com.plc.carrental.entity.CarColor;
import com.plc.carrental.entity.CarType;

public class CarDto {
    private Long id;
    private String carBrand;
    private String carModel;
    private CarType carType;
    private CarColor color;
    private Short capacity;
    private String plateNumber;
    private Integer mileage;

    private Double dailyPriceOfRange;
    private Long selectedDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getDailyPriceOfRange() {
        return dailyPriceOfRange;
    }

    public void setDailyPriceOfRange(Double dailyPriceOfRange) {
        this.dailyPriceOfRange = dailyPriceOfRange;
    }

    public Long getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(Long selectedDays) {
        this.selectedDays = selectedDays;
    }
}
