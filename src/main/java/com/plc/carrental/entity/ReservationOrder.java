package com.plc.carrental.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationOrder extends AuditObject {
    private Car orderedCar;
    private User customer;
    private LocalDateTime reservedDate;
    private LocalDate pickUpDate;
    private LocalTime pickUpTime;
    private LocalDate returnDate;
    private LocalTime returnTime;
    private Double totalAmount;
    private OrderStatus reserveStatus;

    public Car getOrderedCar() {
        return orderedCar;
    }

    public void setOrderedCar(Car orderedCar) {
        this.orderedCar = orderedCar;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
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

    @Override
    public String toString() {
        return "ReservationOrder{" +
                "orderedCar=" + orderedCar +
                ", customer=" + customer +
                ", reservedDate=" + reservedDate +
                ", pickUpDate=" + pickUpDate +
                ", pickUpTime=" + pickUpTime +
                ", returnDate=" + returnDate +
                ", returnTime=" + returnTime +
                ", totalAmount=" + totalAmount +
                ", reserveStatus=" + reserveStatus +
                '}';
    }
}
