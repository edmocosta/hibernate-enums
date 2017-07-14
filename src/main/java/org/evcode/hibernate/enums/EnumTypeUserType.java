package org.evcode.hibernate.enums;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static java.sql.Types.VARCHAR;

public final class EnumTypeUserType implements DynamicParameterizedType, UserType, ParameterizedType {

    private Class<? extends Enum> enumClass;

    @Override
    public Object assemble(Serializable cached, Object owner) {
        return cached;
    }

    @Override
    public Object deepCopy(Object value) {
        return value;
    }

    @Override
    public Serializable disassemble(Object value) {
        return (Serializable) value;
    }

    @Override
    public boolean equals(Object x, Object y) {
        return x == y;
    }

    @Override
    public int hashCode(Object x) {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException {

        String label = rs.getString(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        for (Enum value : returnedClass().getEnumConstants()) {
            if (value instanceof EnumType) {
                EnumType valuedEnum = (EnumType) value;
                if (valuedEnum.toValue().equals(label)) {
                    return value;
                }
            }
        }

        throw new HibernateException("Unknown " + returnedClass().getSimpleName() + " label");
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
            throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, VARCHAR);
        } else {
            if (value instanceof StringEnumType) {
                st.setString(index, ((StringEnumType) value).toValue());
            } else if (value instanceof IntegerEnumType) {
                st.setInt(index, ((IntegerEnumType) value).toValue());
            } else if (value instanceof EnumType) {
                EnumType enumValue = ((EnumType) value);
                st.setObject(index, enumValue.toValue(), enumValue.getSqlType());
            } else {
                throw new HibernateException("EnumType for class " + value.getClass() + " is not supported!");
            }
        }
    }

    @Override
    public Object replace(Object original, Object target, Object owner) {
        return original;
    }

    @Override
    public Class<? extends Enum> returnedClass() {
        return enumClass;
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{VARCHAR};
    }

    @Override
    public void setParameterValues(Properties parameters) {
        ParameterType params = (ParameterType) parameters.get(PARAMETER_TYPE);
        enumClass = params.getReturnedClass();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
