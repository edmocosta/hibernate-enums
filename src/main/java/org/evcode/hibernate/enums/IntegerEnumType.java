package org.evcode.hibernate.enums;

import java.sql.Types;

public interface IntegerEnumType extends EnumType<Integer> {

    @Override
    default int getSqlType() {
        return Types.INTEGER;
    }
}
