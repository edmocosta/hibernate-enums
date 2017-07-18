package org.evcode.hibernate.enums;

import javax.persistence.*;
import java.sql.Types;
import java.time.Instant;
import java.util.UUID;

@Entity
public class EntityObject {

    @Id
    Long id = Instant.now().toEpochMilli();

    @Column
    String name = UUID.randomUUID().toString();

    @Column(columnDefinition = "INT")
    IntegerEnum integerEnum = IntegerEnum.ONE;

    @Column(columnDefinition = "VARCHAR")
    StringEnum stringEnum = StringEnum.OPTION_A;

    @Column(columnDefinition = "DOUBLE")
    DoubleEnum doubleEnum = DoubleEnum.ONE;

    @Embedded
    EmbeddedProperty embeddedProperty = new EmbeddedProperty();


    public enum DoubleEnum implements EnumType<Double> {
        ONE(12.58), TWO(22.15);

        private final Double value;

        DoubleEnum(Double value) {
            this.value = value;
        }

        @Override
        public Double toValue() {
            return value;
        }

        @Override
        public int getSqlType() {
            return Types.DOUBLE;
        }
    }

    public enum IntegerEnum implements IntegerEnumType {
        ONE(1), TWO(2);

        private final int value;

        IntegerEnum(int value) {
            this.value = value;
        }

        @Override
        public Integer toValue() {
            return value;
        }
    }

    public enum StringEnum implements StringEnumType {
        OPTION_A("A"), OPTION_B("B");

        private final String value;

        StringEnum(String value) {
            this.value = value;
        }

        @Override
        public String toValue() {
            return value;
        }
    }

    @Embeddable
    public static class EmbeddedProperty {

        @Column(columnDefinition = "INTEGER")
        EmbeddedIntegerEnum embeddedInt;

        public enum EmbeddedIntegerEnum implements IntegerEnumType {
            THREE(1), FOUR(2);

            private final int value;

            EmbeddedIntegerEnum(int value) {
                this.value = value;
            }

            @Override
            public Integer toValue() {
                return value;
            }
        }
    }
}
