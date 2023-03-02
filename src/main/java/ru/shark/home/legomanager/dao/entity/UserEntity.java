package ru.shark.home.legomanager.dao.entity;

import ru.shark.home.common.dao.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "LEGO_USERS")
public class UserEntity extends BaseEntity {
    private static final String DESCRIPTION = "Владельцы";

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @Column(name = "LEGO_ID")
    @SequenceGenerator(name = "UsersGenerator", sequenceName = "LEGO_USERS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UsersGenerator")
    private Long id;

    /**
     * Имя владельца.
     */
    @Column(name = "LEGO_NAME", nullable = false)
    private String name;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String color) {
        this.name = color;
    }

    public static String getDescription() {
        return DESCRIPTION;
    }
}
