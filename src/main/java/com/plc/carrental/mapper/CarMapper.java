package com.plc.carrental.mapper;

import com.plc.carrental.entity.Car;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarMapper {

    List<Car> findAvailableCars(@Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("days") Long daysOfRange,
                                @Param("carId") Long carId);

    Car findCarById(Long carId);

    @Transactional
    int updateCarAvailability(Car bookedCar);

    @Transactional
    int makeCarAvailable(@Param("carId") Long carId,
                         @Param("startDate") LocalDate startDate,
                         @Param("endDate") LocalDate endDate);
}
