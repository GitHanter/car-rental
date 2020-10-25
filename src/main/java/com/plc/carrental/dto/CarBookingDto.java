package com.plc.carrental.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class CarBookingDto {
    @NotNull(message = "carId can't be null")
    private Long carId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "pickUpDate can't be null")
    private LocalDate pickUpDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull(message = "pickUpTime can't be null")
    private LocalTime pickUpTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "returnDate can't be null")
    private LocalDate returnDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull(message = "returnTime can't be null")
    private LocalTime returnTime;

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
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

    @Override
    public String toString() {
        return "CarBookingDto{" +
                "carId=" + carId +
                ", pickUpDate=" + pickUpDate +
                ", pickUpTime=" + pickUpTime +
                ", returnDate=" + returnDate +
                ", returnTime=" + returnTime +
                '}';
    }
}
