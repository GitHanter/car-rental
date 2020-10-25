package com.plc.carrental.dto;

import com.plc.carrental.entity.Car;
import com.plc.carrental.entity.OrderStatus;
import com.plc.carrental.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class OrderDetailDto {
    private Long id;
    private CarDto orderedCar;
    private LocalDateTime reservedDate;
    private LocalDate pickUpDate;
    private LocalTime pickUpTime;
    private LocalDate returnDate;
    private LocalTime returnTime;
    private Double totalAmount;
    private OrderStatus reserveStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarDto getOrderedCar() {
        return orderedCar;
    }

    public void setOrderedCar(CarDto orderedCar) {
        this.orderedCar = orderedCar;
    }

    public LocalDateTime getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(LocalDateTime reservedDate) {
        this.reservedDate = reservedDate;
    }

    public LocalDate getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(LocalDate pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public LocalTime getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(LocalTime pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalTime returnTime) {
        this.returnTime = returnTime;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(OrderStatus reserveStatus) {
        this.reserveStatus = reserveStatus;
    }
}
