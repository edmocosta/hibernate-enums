package org.evcode.hibernate.enums;

import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.mapping.*;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;

public class EnumTypeWrapper extends SimpleValue implements KeyValue {


    public EnumTypeWrapper(MetadataImplementor metadata, Properties properties) {

        super(metadata);
        super.setTypeName(EnumTypeUserType.class.getName());
        super.setTypeParameters(properties);

        String propertyName = properties.getProperty("org.hibernate.type.ParameterType.propertyName");
        if (propertyName != null) {
            Column column = new Column(propertyName);
            super.addColumn(column);
        }
    }

    public EnumTypeWrapper(MetadataImplementor metadata,
                           Table table,
                           String propertyName,
                           String propertyReturnedClassName) {
        super(metadata);

        Properties properties = new Properties();
        properties.setProperty(DynamicParameterizedType.IS_DYNAMIC, "true");

        if (propertyReturnedClassName != null) {
            properties.setProperty(DynamicParameterizedType.RETURNED_CLASS, propertyReturnedClassName);
        }

        super.setTypeName(EnumTypeUserType.class.getName());
        super.setTypeParameters(properties);

        if (propertyName != null) {
            Column column = new Column(propertyName);
            super.addColumn(column);
        }

        if (table != null) {
            super.setTable(table);
        }
    }


    public EnumTypeWrapper(MetadataImplementor metadata, PersistentClass persistentClass, Property property) {
        this(metadata, persistentClass.getTable(), property.getName(), property.getType().getReturnedClass().getName());
    }
}
