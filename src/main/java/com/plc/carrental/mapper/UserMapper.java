package com.plc.carrental.mapper;

import com.plc.carrental.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE USER_NAME = #{userName}")
    User findUserByUserName(String userName);
}
