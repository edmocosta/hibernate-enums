package org.evcode.hibernate.enums;

import java.sql.Types;

public interface EnumType<T> {

    T getValue();

    EnumType<T> fromValue(T value);

    default int getSqlType() {
        return Types.VARCHAR;
    }
}
