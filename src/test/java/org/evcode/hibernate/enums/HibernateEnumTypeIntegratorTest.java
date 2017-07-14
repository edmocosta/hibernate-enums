package org.evcode.hibernate.enums;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.function.Predicate;

public class HibernateEnumTypeIntegratorTest {

    private static Configuration config;
    private static SessionFactory factory;
    private static Session hibernateSession;

    @BeforeClass
    public static void init() {
        config = new Configuration().addAnnotatedClass(EntityObject.class);
        config.configure(new File("src/test/resources/hibernate.cfg.xml"));
        factory = config.buildSessionFactory();
        hibernateSession = factory.openSession();
    }

    private void testNewEntity(EntityObject entityObject,
                               Predicate<EntityObject> checkEntityFunc,
                               String tableColumn,
                               Object expectedValue) {

        hibernateSession.persist(entityObject);
        hibernateSession.flush();

        EntityObject savedEntity = hibernateSession.get(EntityObject.class, entityObject.getId());
        Assert.assertTrue(checkEntityFunc.test(savedEntity));

        //Check the raw value on database
        Object result = hibernateSession
                .createSQLQuery("select " + tableColumn + " from EntityObject where id = ?")
                .setParameter(0, entityObject.getId())
                .uniqueResult();

        Assert.assertEquals(result, expectedValue);
    }

    @Test
    public void testIntegerEnumType() {
        EntityObject obj1 = new EntityObject();
        testNewEntity(obj1, p -> p.getIntegerEnum().equals(EntityObject.IntegerEnum.ONE), "integerEnum",
                EntityObject.IntegerEnum.ONE.toValue());

        EntityObject obj2 = new EntityObject();
        obj2.setIntegerEnum(EntityObject.IntegerEnum.TWO);
        testNewEntity(obj2, p -> p.getIntegerEnum().equals(EntityObject.IntegerEnum.TWO), "integerEnum",
                EntityObject.IntegerEnum.TWO.toValue());
    }

    @Test
    public void testStringEnumType() {
        EntityObject ob1 = new EntityObject();
        ob1.setStringEnum(EntityObject.StringEnum.OPTION_A);
        testNewEntity(ob1, p -> p.getStringEnum().equals(EntityObject.StringEnum.OPTION_A), "stringEnum",
                EntityObject.StringEnum.OPTION_A.toValue());

        EntityObject obj2 = new EntityObject();
        obj2.setStringEnum(EntityObject.StringEnum.OPTION_B);
        testNewEntity(obj2, p -> p.getStringEnum().equals(EntityObject.StringEnum.OPTION_B), "stringEnum",
                EntityObject.StringEnum.OPTION_B.toValue());
    }
}
