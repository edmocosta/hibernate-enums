package org.evcode.hibernate.enums;

import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.mapping.*;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;

public class EnumTypeWrapper extends SimpleValue implements KeyValue {

    public EnumTypeWrapper(MetadataImplementor metadata, PersistentClass entity, Property property) {
        super(metadata);

        Properties properties = new Properties();
        properties.setProperty(DynamicParameterizedType.IS_DYNAMIC, "true");
        properties.setProperty(DynamicParameterizedType.RETURNED_CLASS, property.getType().getReturnedClass().getName());

        super.setTypeName(EnumTypeUserType.class.getName());
        super.setTypeParameters(properties);

        Column column = new Column(property.getName());
        super.addColumn(column);

        super.setTable(entity.getTable());
    }
}
