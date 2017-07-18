package org.evcode.hibernate.enums;

import java.sql.Types;

public interface EnumType<T> {
    /**
     * @return Get the value to be persisted on database
     */
    T toValue();

    /**
     * Parse a persisted value to an {@link EnumType} instance
     *
     * @param enumType EnumType implementation
     * @param value    Persisted value
     * @return Enum value from given string
     */
    static <T extends EnumType<V>, V> T fromValue(Class<T> enumType, V value) {
        if (enumType.isEnum()) {
            for (EnumType val : enumType.getEnumConstants()) {
                if (val.toValue().equals(value)) {
                    return (T) val;
                }
            }
        }
        throw new IllegalArgumentException("Unknown Enum for value [" + value + "]");
    }

    /**
     * Parse a persisted value to an {@link EnumType} instance
     *
     * @param value Persisted value
     * @return Enum value from given string
     */
    default EnumType<T> fromValue(T value) {
        if (this.getClass().isEnum()) {
            return fromValue(this.getClass(), value);
        }

        throw new IllegalArgumentException("Unknown Enum for value [" + value + "]");
    }

    /**
     * @return Get the JDBC SQL Type of the current @{@link EnumType} implementation
     */
    default int getSqlType() {
        return Types.VARCHAR;
    }
}
