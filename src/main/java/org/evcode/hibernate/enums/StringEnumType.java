package org.evcode.hibernate.enums;

import java.sql.Types;

/**
 * EnumType for string values
 */
public interface StringEnumType extends EnumType<String> {
    @Override
    default int getSqlType() {
        return Types.VARCHAR;
    }
}
