package org.evcode.hibernate.enums;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class RelatedObject {

    @EmbeddedId
    RelatedObjectPk pk;

    @Column
    String name = UUID.randomUUID().toString();

    public enum LetterId implements EnumType<String> {

        FIRST("A"), SECOND("B");

        private final String value;

        LetterId(String value) {
            this.value = value;
        }

        @Override
        public String toValue() {
            return value;
        }
    }

    @Embeddable
    public static class RelatedObjectPk implements Serializable {

        @Column
        Long id;

        @Column(columnDefinition = "VARCHAR")
        LetterId letterId;

        public RelatedObjectPk(Long id, LetterId letterId) {
            this.id = id;
            this.letterId = letterId;
        }
    }
}
