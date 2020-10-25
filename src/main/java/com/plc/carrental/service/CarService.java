package com.plc.carrental.service;

import com.plc.carrental.entity.Car;
import com.plc.carrental.entity.CarDailyPrice;
import com.plc.carrental.entity.OrderStatus;
import com.plc.carrental.entity.ReservationOrder;
import com.plc.carrental.exception.BookingException;
import com.plc.carrental.mapper.CarMapper;
import com.plc.carrental.mapper.OrderMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CarService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarService.class);
    private static final String ORDER_CAR_LOCK_PREFIX = "CAR_LOCK_";
    private static final String ORDER_LOCK_PREFIX = "ORDER_LOCK_";
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedissonClient redissonClient;

    public List<Car> findAvailableCars(LocalDate startDate, LocalDate endDate) {
        return carMapper.findAvailableCars(startDate, endDate,
                startDate.until(endDate, ChronoUnit.DAYS) + 1, null);
    }

    public Car findAvailableCar(LocalDate startDate, LocalDate endDate, Long carId) {
        List<Car> cars = carMapper.findAvailableCars(startDate, endDate,
                startDate.until(endDate, ChronoUnit.DAYS) + 1, carId);
        return CollectionUtils.isEmpty(cars) ? null : cars.get(0);
    }

    public Car findCarInfo(Long carId) {
        return carMapper.findCarById(carId);
    }

    /**
     * A car may be concurrently reserved by multiple user
     * @param initialOrder order with initial parameters
     * @return
     * @throws BookingException
     */
    @Transactional(rollbackFor = Throwable.class)
    public Long bookCar(ReservationOrder initialOrder) throws BookingException {
        String lockKeyBuilder = ORDER_CAR_LOCK_PREFIX + initialOrder.getOrderedCar().getId() + "_" +
                initialOrder.getOrderedCar().getPlateNumber();
        RLock lock = redissonClient.getLock(lockKeyBuilder);
        try {
            if (lock.tryLock(3, 5, TimeUnit.SECONDS)) {
                //Double check
                Car availableCar = findAvailableCar(initialOrder.getPickUpDate(), initialOrder.getReturnDate(),
                        initialOrder.getOrderedCar().getId());
                if (availableCar == null) {
                    LOGGER.debug("Car({}) not available, it may be booked by other people or updated by administrator!",
                            initialOrder.getOrderedCar().getId());
                    throw new BookingException("The Car is not available!");
                }
                //Car price is dynamic, we stored the snapshot total amount for the order
                initialOrder.setTotalAmount(availableCar.getPrices().stream()
                        .mapToDouble(CarDailyPrice::getPrice).sum());
                initialOrder.setReserveStatus(OrderStatus.CONFIRMED);
                initialOrder.setReservedDate(LocalDateTime.now());
                initialOrder.setCreatedBy(initialOrder.getCustomer().getUserName());
                initialOrder.setCreatedTime(LocalDateTime.now());
                initialOrder.setModifiedBy(initialOrder.getCustomer().getUserName());
                initialOrder.setModifiedTime(LocalDateTime.now());
                initialOrder.setVersion(0L);
                orderMapper.createOrder(initialOrder);
                carMapper.updateCarAvailability(availableCar);
                return initialOrder.getId();
            }
        } catch (InterruptedException e) {
            LOGGER.warn("CurrentThread[{}] got interrupted!", Thread.currentThread().getName());
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
        return null;
    }

    public List<ReservationOrder> getReservedOrdersOfCustomer(String username) {
        return orderMapper.findOrdersByUsername(username);
    }

    /**
     * Order may be concurrently update:
     * e.g complete a order when try to cancel it
     * @param orderId reserved order id
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public boolean cancelOrder(Long orderId) {
        String lockKeyBuilder = ORDER_LOCK_PREFIX + "_" + orderId;
        RLock lock = redissonClient.getLock(lockKeyBuilder);
        boolean result = true;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            if (lock.tryLock(3, 5, TimeUnit.SECONDS)) {
                ReservationOrder order = orderMapper.findOrderById(orderId);
                if (order == null) {
                    result = false;
                } else if (order.getReserveStatus() == OrderStatus.COMPLETED){
                    result = false;
                    LOGGER.warn("{} try to cancel a completed order[{}]!", username, order);
                } else if (order.getReserveStatus() == OrderStatus.CONFIRMED){
                    orderMapper.updateOrderStatus(orderId, OrderStatus.CANCELLED, order.getVersion(),
                            username, LocalDateTime.now());
                    carMapper.makeCarAvailable(order.getOrderedCar().getId(),
                            order.getPickUpDate(), order.getReturnDate());
                }
            } else {
                result = false;
            }
        } catch (InterruptedException e) {
            LOGGER.warn("CurrentThread[{}] got interrupted!", Thread.currentThread().getName());
            Thread.currentThread().interrupt();
            result =  false;
        } finally {
            lock.unlock();
        }
        return result;
    }
}
