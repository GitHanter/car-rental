package com.plc.carrental.converter;

import com.plc.carrental.dto.CarBookingDto;
import com.plc.carrental.entity.Car;
import com.plc.carrental.entity.ReservationOrder;
import com.plc.carrental.entity.User;
import com.plc.carrental.mapper.CarMapper;
import com.plc.carrental.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

public abstract class CarBookingConverterDecorator implements CarBookingConverter {
    @Autowired
    @Qualifier("delegate")
    private CarBookingConverter delegate;

    @Autowired
    private CarMapper carMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public ReservationOrder carBookingToReserveOrder(CarBookingDto carBooking, String customerUserName) {
        ReservationOrder order = delegate.carBookingToReserveOrder(carBooking, customerUserName);
        Optional<User> customer = Optional.of(userMapper.findUserByUserName(customerUserName));
        order.setCustomer(customer.orElse(new User(customerUserName)));
        Optional<Car> orderedCar = Optional.of(carMapper.findCarById(carBooking.getCarId()));
        order.setOrderedCar(orderedCar.orElse(new Car(carBooking.getCarId())));
        return order;
    }
}
