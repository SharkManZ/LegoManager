package ru.shark.home.legomanager.dao.entity;

import ru.shark.home.common.dao.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "LEGO_COLOR")
public class ColorEntity extends BaseEntity {
    private static final String DESCRIPTION = "Цвета";

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @Column(name = "LEGO_ID")
    @SequenceGenerator(name = "ColorGenerator", sequenceName = "LEGO_COLOR_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ColorGenerator")
    private Long id;

    @Column(name = "LEGO_NAME", nullable = false)
    private String name;

    @Column(name = "LEGO_HEX_COLOR", length = 6, nullable = false)
    private String hexColor;

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

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public static String getDescription() {
        return DESCRIPTION;
    }
}
