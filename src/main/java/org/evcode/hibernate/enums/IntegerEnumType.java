package org.evcode.hibernate.enums;

import java.sql.Types;

/**
 * EnumType for integer values
 */
public interface IntegerEnumType extends EnumType<Integer> {
    @Override
    default int getSqlType() {
        return Types.INTEGER;
    }
}
