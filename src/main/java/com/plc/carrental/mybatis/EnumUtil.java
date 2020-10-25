package com.plc.carrental.mybatis;

public final class EnumUtil {

    @SuppressWarnings("unchecked")
    public static <E extends PersistableEnumValue> E getEnumValue(Class<E> enumClass, Object value) {
        if (value != null) {
            for (PersistableEnumValue enumInstance : enumClass.getEnumConstants()) {
                if (String.valueOf(value).equals(String.valueOf(enumInstance.getValue()))) {
                    return (E) enumInstance;
                }
            }

            throw new IllegalArgumentException("No " + enumClass.getSimpleName() + " with value [" + value + "] found.");
        }

        return null;
    }
}