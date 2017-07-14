package org.evcode.hibernate.enums;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.Instant;
import java.util.UUID;

@Entity
public class EntityObject {

    @Id
    private Long id = Instant.now().toEpochMilli();

    @Column
    @Transient
    private String name = UUID.randomUUID().toString();

    @Column(columnDefinition = "INT")
    private IntegerEnum integerEnum = IntegerEnum.ONE;

    @Column(columnDefinition = "VARCHAR")
    private StringEnum stringEnum = StringEnum.OPTION_A;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IntegerEnum getIntegerEnum() {
        return integerEnum;
    }

    public void setIntegerEnum(IntegerEnum integerEnum) {
        this.integerEnum = integerEnum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StringEnum getStringEnum() {
        return stringEnum;
    }

    public void setStringEnum(StringEnum stringEnum) {
        this.stringEnum = stringEnum;
    }

    public enum IntegerEnum implements IntegerEnumType {

        ONE(1),
        TWO(2);

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

        OPTION_A("A"),
        OPTION_B("B");

        private final String value;

        StringEnum(String value) {
            this.value = value;
        }

        @Override
        public String toValue() {
            return value;
        }
    }
}
