package com.plc.carrental.converter;

import com.plc.carrental.dto.CarDto;
import com.plc.carrental.entity.Car;
import com.plc.carrental.entity.CarDailyPrice;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Mapper
public abstract class CarConverter {

    public abstract CarDto toCarDto(Car car);

    public abstract List<CarDto> toCarDtos(List<Car> car);

    @AfterMapping
    protected void fillDailyAvgPrice(Car car, @MappingTarget CarDto result) {
        long days = car.getPrices().get(0).getDayOfYear()
                .until(car.getPrices().get(car.getPrices().size() - 1).getDayOfYear(), ChronoUnit.DAYS) + 1;
        result.setSelectedDays(days);
        result.setDailyPriceOfRange(car.getPrices().stream().mapToDouble(CarDailyPrice::getPrice).sum()/days);
    }
}
