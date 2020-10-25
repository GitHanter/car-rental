package com.plc.carrental.entity;

import java.time.LocalDate;

public class CarDailyPrice extends AuditObject {
    private Car car;
    private LocalDate dayOfYear;
    private Double price;
    private Boolean isAvailable;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDate getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(LocalDate dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}
