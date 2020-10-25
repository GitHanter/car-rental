package com.plc.carrental.converter;

import com.plc.carrental.dto.CarDto;
import com.plc.carrental.dto.OrderDetailDto;
import com.plc.carrental.entity.Car;
import com.plc.carrental.entity.ReservationOrder;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Mapper
public abstract class ReservationOrderConverter {
    public abstract OrderDetailDto reservationToOrderDetail(ReservationOrder order);

    @AfterMapping
    public void afterReservationConvert(ReservationOrder order, @MappingTarget OrderDetailDto orderDetailDto) {
        long reservedDays = orderDetailDto.getPickUpDate().until(orderDetailDto.getReturnDate(), ChronoUnit.DAYS) + 1;
        orderDetailDto.getOrderedCar().setSelectedDays(reservedDays);
        orderDetailDto.getOrderedCar().setDailyPriceOfRange(orderDetailDto.getTotalAmount()/reservedDays);
    }

    public abstract CarDto reservedCarToCarDto(Car car);

    public abstract List<OrderDetailDto> reservationsToOrderDetails(List<ReservationOrder> orders);
}
