package org.evcode.hibernate.enums;

import java.sql.Types;

public interface StringEnumType extends EnumType<String> {

    @Override
    default int getSqlType() {
        return Types.VARCHAR;
    }
}
