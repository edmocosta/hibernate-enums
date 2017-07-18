package org.evcode.hibernate.enums;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.mapping.*;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.hibernate.type.Type;

import java.util.Iterator;

public class HibernateEnumTypeIntegrator implements Integrator {

    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactoryImplementor,
                          SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {

        final MetadataImplementor mi;
        if (metadata instanceof MetadataImplementor) {
            mi = (MetadataImplementor) metadata;
        } else {
            throw new IllegalArgumentException("Metadata was not assignable to MetadataImplementor: " + metadata.getClass());
        }

        for (PersistentClass entity : mi.getEntityBindings()) {
            for (Iterator it = entity.getDeclaredPropertyIterator(); it.hasNext(); ) {
                integrateProperty((Property) it.next(), entity);
            }
            if (entity.hasIdentifierProperty()) {
                integrateProperty(entity.getIdentifierProperty(), entity);
            }
        }

        for (Collection collection : mi.getCollectionBindings()) {
            integrateCollection(collection);
        }
    }


    private void integrateSimpleProperty(Property property, PersistentClass persistentClass) {
        SimpleValue simpleValue = (SimpleValue) property.getValue();

        if (EnumType.class.isAssignableFrom(property.getType().getReturnedClass())) {
            property.setValue(new EnumTypeWrapper(simpleValue.getMetadata(), persistentClass, property));
        }
    }

    private void integrateProperty(Property property, PersistentClass persistentClass) {
        Value value = property.getValue();
        Type propertyType = value.getType();

        if (propertyType.isComponentType()) {
            Iterator<Property> innerProperties = ((Component) value).getPropertyIterator();
            innerProperties.forEachRemaining(p -> integrateProperty(p, persistentClass));
            return;
        }

        if (value.isSimpleValue()) {
            integrateSimpleProperty(property, persistentClass);
        }
    }

    private void integrateCollection(Collection collection) {
        Value elementValue = collection.getElement();
        if (elementValue.isSimpleValue() && EnumType.class.isAssignableFrom(elementValue.getType().getReturnedClass())) {
            SimpleValue simpleValue = (SimpleValue) elementValue;
            collection.setElement(new EnumTypeWrapper(simpleValue.getMetadata(), simpleValue.getTypeParameters()));
        }
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor,
                             SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
        //There is nothing to do here yet!
    }
}
