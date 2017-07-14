package org.evcode.hibernate.enums;

import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.mapping.*;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;

public class EnumTypeWrapper extends SimpleValue implements KeyValue {
    private final PersistentClass entity;

    public EnumTypeWrapper(MetadataImplementor metadata, PersistentClass entity, Property property) {
        super(metadata);
        this.entity = entity;

        Properties properties = new Properties();
        properties.setProperty(DynamicParameterizedType.IS_DYNAMIC, "true");
        properties.setProperty(DynamicParameterizedType.RETURNED_CLASS, property.getType().getReturnedClass().getName());

        super.setTypeName(EnumTypeUserType.class.getName());
        super.setTypeParameters(properties);
        super.addColumn(new Column(property.getName()));
        super.setTable(entity.getTable());
    }
}
