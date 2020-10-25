package com.plc.carrental.controller;

import com.plc.carrental.converter.CarBookingConverter;
import com.plc.carrental.converter.CarConverter;
import com.plc.carrental.converter.ReservationOrderConverter;
import com.plc.carrental.dto.CarBookingDto;
import com.plc.carrental.dto.CarDto;
import com.plc.carrental.dto.OrderDetailDto;
import com.plc.carrental.dto.ResponseMessage;
import com.plc.carrental.entity.Car;
import com.plc.carrental.entity.ReservationOrder;
import com.plc.carrental.exception.BookingException;
import com.plc.carrental.service.CarService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RentalController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RentalController.class);

    @Autowired
    private CarService carService;

    @Autowired
    private CarBookingConverter carBookingConverter;

    @RequestMapping(value="/getAvailableCars", method= RequestMethod.GET, produces={"application/json"})
    public ResponseMessage<List<CarDto>> getAvailableVehicle(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                 LocalDate startDate,
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                 LocalDate endDate) {
        ResponseMessage<List<CarDto>> response = new ResponseMessage<>();
        response.setError(false);
        if (startDate.until(endDate, ChronoUnit.DAYS) < 0) {
            response.setError(true);
            response.setErrorMessage("Parameter error: endDate must equal or later than startDate");
            return response;
        }
        if (LocalDate.now().isAfter(startDate)) {
            response.setError(true);
            response.setErrorMessage("Parameter error: startDate must equal or later than current date");
            return response;
        }

        List<Car> cars = carService.findAvailableCars(startDate, endDate);
        response.setData(Mappers.getMapper(CarConverter.class).toCarDtos(cars));
        return response;
    }

    @RequestMapping(value="/book", method=RequestMethod.POST, consumes= {"application/json"}, produces={"application/json"})
    public ResponseMessage<Long> bookCar(OAuth2Authentication authentication,
                                            @RequestBody @Valid CarBookingDto dto, BindingResult bindingResult) {
        ResponseMessage<Long> response = new ResponseMessage<>();
        response.setError(true);
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .map(fe -> fe.getField() + ":" + fe.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            response.setErrorMessage(message);
            return response;
        }

        if (dto.getPickUpDate().until(dto.getReturnDate(), ChronoUnit.DAYS) < 0) {
            response.setErrorMessage("Parameter error: returnDate must equal or later than pickUpDate");
            return response;
        }
        if (LocalDate.now().isAfter(dto.getPickUpDate())) {
            response.setErrorMessage("Parameter error: pickUpDate and returnDate must equal or later than current date");
            return response;
        }

        Car carInfo = carService.findCarInfo(dto.getCarId());
        if (carInfo == null) {
            response.setErrorMessage("Parameter error: carId");
            return response;
        }
        Car car = carService.findAvailableCar(dto.getPickUpDate(), dto.getReturnDate(), dto.getCarId());
        if (car == null) {
            String error = "Car(%d,%s,%s,%s) not available at the range: %s,%s";
            response.setErrorMessage(String.format(error, carInfo.getId(), carInfo.getCarBrand(),
                    carInfo.getCarModel(), carInfo.getCarType(), dto.getPickUpDate(), dto.getReturnDate()));
            return response;
        }
        Long orderId;
        try {
            orderId = carService.bookCar(carBookingConverter.carBookingToReserveOrder(dto, authentication.getName()));
        } catch (BookingException be) {
            response.setErrorMessage(be.getMessage());
            return response;
        } catch (Exception ex) {
            LOGGER.error("Order car filed: {}", dto);
            LOGGER.error("Exception:", ex);
            response.setErrorMessage("Internal server error!");
            return response;
        }

        if (orderId == null) {
            LOGGER.error("Order acquire lock failed or got interrupted: {}", dto);
            response.setErrorMessage("Internal server error!");
            return response;
        }
        response.setError(false);
        response.setData(orderId);
        return response;
    }

    @RequestMapping(value="/getOrders", method= RequestMethod.GET, produces={"application/json"})
    public ResponseMessage<List<OrderDetailDto>> findCustomerOrders(OAuth2Authentication authentication) {
        List<ReservationOrder> orders = carService.getReservedOrdersOfCustomer(authentication.getName());
        ResponseMessage<List<OrderDetailDto>> response = new ResponseMessage<>();
        response.setError(false);
        response.setData(Mappers.getMapper(ReservationOrderConverter.class).reservationsToOrderDetails(orders));
        return response;
    }

    @RequestMapping(value="/cancel/{orderId}", method=RequestMethod.DELETE, produces={"application/json"})
    public ResponseMessage<Boolean> bookCar(@PathVariable Long orderId) {
        ResponseMessage<Boolean> response = new ResponseMessage<>();
        boolean result = carService.cancelOrder(orderId);
        response.setError(result);
        response.setData(result);
        return response;
    }

}
