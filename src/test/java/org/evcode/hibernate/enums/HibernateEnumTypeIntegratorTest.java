package org.evcode.hibernate.enums;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.time.Instant;
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

    private void createEntityObjectAndTest(EntityObject entityObject,
                                           Predicate<EntityObject> checkEntityFunc,
                                           String tableColumn,
                                           Object expectedValue) {

        hibernateSession.persist(entityObject);
        hibernateSession.flush();

        EntityObject savedEntity = hibernateSession.get(EntityObject.class, entityObject.id);
        Assert.assertTrue(checkEntityFunc.test(savedEntity));

        //Check the raw value on database
        Object result = hibernateSession
                .createSQLQuery("select " + tableColumn + " from EntityObject where id = ?")
                .setParameter(0, entityObject.id)
                .uniqueResult();

        Assert.assertEquals(expectedValue, result);
    }

    @Test
    public void testIntegerEnumType() {

        EntityObject obj1 = new EntityObject();
        createEntityObjectAndTest(obj1, p -> p.integerEnum.equals(EntityObject.IntegerEnum.ONE), "integerEnum",
                EntityObject.IntegerEnum.ONE.toValue());

        EntityObject obj2 = new EntityObject();
        obj2.integerEnum = EntityObject.IntegerEnum.TWO;
        createEntityObjectAndTest(obj2, p -> obj2.integerEnum.equals(EntityObject.IntegerEnum.TWO), "integerEnum",
                EntityObject.IntegerEnum.TWO.toValue());

        EntityObject obj3 = new EntityObject();
        obj3.integerEnum = null;
        createEntityObjectAndTest(obj3, p -> p.integerEnum == null, "integerEnum", null);


    }

    @Test
    public void testStringEnumType() {
        EntityObject ob1 = new EntityObject();
        ob1.stringEnum = EntityObject.StringEnum.OPTION_A;
        createEntityObjectAndTest(ob1, p -> p.stringEnum.equals(EntityObject.StringEnum.OPTION_A), "stringEnum",
                EntityObject.StringEnum.OPTION_A.toValue());

        EntityObject obj2 = new EntityObject();
        obj2.stringEnum = EntityObject.StringEnum.OPTION_B;
        createEntityObjectAndTest(obj2, p -> p.stringEnum.equals(EntityObject.StringEnum.OPTION_B), "stringEnum",
                EntityObject.StringEnum.OPTION_B.toValue());
    }

    @Test
    public void testCustomEnumType() {

        EntityObject obj1 = new EntityObject();
        createEntityObjectAndTest(obj1, p -> p.doubleEnum.equals(EntityObject.DoubleEnum.ONE), "doubleEnum",
                EntityObject.DoubleEnum.ONE.toValue());

        EntityObject obj2 = new EntityObject();
        obj2.doubleEnum = EntityObject.DoubleEnum.TWO;
        createEntityObjectAndTest(obj2, p -> p.doubleEnum.equals(EntityObject.DoubleEnum.TWO), "doubleEnum",
                EntityObject.DoubleEnum.TWO.toValue());
    }

    @Test
    public void testParse() {
        Assert.assertEquals(EnumType.fromValue(EntityObject.IntegerEnum.class, 1), EntityObject.IntegerEnum.ONE);
        Assert.assertEquals(EnumType.fromValue(EntityObject.IntegerEnum.class, 2), EntityObject.IntegerEnum.TWO);
        Assert.assertEquals(EnumType.fromValue(EntityObject.StringEnum.class, "A"), EntityObject.StringEnum.OPTION_A);
        Assert.assertEquals(EnumType.fromValue(EntityObject.StringEnum.class, "B"), EntityObject.StringEnum.OPTION_B);
    }

    @Test
    public void testEntityWithEmbeddedId() {

        RelatedObject relatedObject = new RelatedObject();
        relatedObject.pk = new RelatedObject.RelatedObjectPk(Instant.now().toEpochMilli(), RelatedObject.LetterId.FIRST);
        hibernateSession.persist(relatedObject);
        hibernateSession.flush();

        RelatedObject savedEntity = hibernateSession.get(RelatedObject.class, relatedObject.pk);
        Assert.assertEquals(relatedObject.pk.letterId, savedEntity.pk.letterId);

        //Check the raw value on database
        Object result = hibernateSession
                .createSQLQuery("select letterId from RelatedObject where id = ? and letterId = ?")
                .setParameter(0, relatedObject.pk.id)
                .setParameter(1, relatedObject.pk.letterId.toValue())
                .uniqueResult();

        Assert.assertEquals(result, relatedObject.pk.letterId.toValue());
    }
}
