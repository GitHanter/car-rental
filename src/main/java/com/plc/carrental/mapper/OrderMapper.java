package com.plc.carrental.mapper;

import com.plc.carrental.entity.OrderStatus;
import com.plc.carrental.entity.ReservationOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderMapper {

    @Transactional
    Long createOrder(ReservationOrder order);

    List<ReservationOrder> findOrdersByUsername(String username);

    ReservationOrder findOrderById(Long orderId);

    @Transactional
    void updateOrderStatus(@Param("orderId") Long orderId, @Param("newStatus") OrderStatus newStatus,
                           @Param("originalVersion") Long originalVersion, @Param("operator") String operator,
                           @Param("operateTime") LocalDateTime operateTime);
}
