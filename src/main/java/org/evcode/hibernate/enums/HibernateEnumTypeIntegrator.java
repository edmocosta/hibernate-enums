package org.evcode.hibernate.enums;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

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
                Property property = (Property) it.next();
                SimpleValue simpleValue = (SimpleValue) property.getValue();

                if (EnumType.class.isAssignableFrom(property.getType().getReturnedClass())) {
                    property.setValue(new EnumTypeWrapper(simpleValue.getMetadata(), entity, property));
                }
            }
        }
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor,
                             SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
        //There is nothing to do here!
    }
}
