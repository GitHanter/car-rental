package com.plc.carrental.converter;

import com.plc.carrental.dto.CarBookingDto;
import com.plc.carrental.entity.ReservationOrder;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(CarBookingConverterDecorator.class)
public interface CarBookingConverter {

    ReservationOrder carBookingToReserveOrder(CarBookingDto carBooking, String customerUserName);
}
