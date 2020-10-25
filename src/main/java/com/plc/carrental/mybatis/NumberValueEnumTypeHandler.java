package com.plc.carrental.mybatis;

import com.plc.carrental.entity.OrderStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes({OrderStatus.class})
public class NumberValueEnumTypeHandler<E extends Enum<E> & PersistableEnumValue> extends BaseTypeHandler<E> {
    private Class<E> type;

    private final E[] enums;

    public NumberValueEnumTypeHandler(Class<E> type) {
        if (type == null) throw new IllegalArgumentException("Type argument cannot be null");
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null) throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
    }

    /**
     * Entity-to-DB mapping
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    /**
     * DB-to-Entity mapping
     *
     * @param rs
     *            ResultSet
     * @param columnName
     *            String
     */
    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Integer value = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        }
        return EnumUtil.getEnumValue(type, value);
    }

    /**
     * DB-to-Entity mapping
     *
     * @param rs
     *            ResultSet
     * @param columnIndex
     *            Integer
     */
    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        Integer value = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return EnumUtil.getEnumValue(type, value);
    }

    /**
     * DB-to-Entity mapping
     *
     * @param cs
     *            CallableStatement
     * @param columnIndex
     *            Integer
     */
    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Integer value = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        return EnumUtil.getEnumValue(type, value);
    }
}
