package ru.shark.home.legomanager.dao.entity;

import ru.shark.home.common.dao.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "LEGO_PART_COLOR")
public class PartColorEntity extends BaseEntity {

    private static final String DESCRIPTION = "Цвет детали";

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @Column(name = "LEGO_ID")
    @SequenceGenerator(name = "PartColorGenerator", sequenceName = "LEGO_PART_COLOR_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PartColorGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "LEGO_PART_ID", nullable = false)
    private PartEntity part;

    @ManyToOne
    @JoinColumn(name = "LEGO_COLOR_ID", nullable = false)
    private ColorEntity color;

    @Column(name = "LEGO_NUMBER", length = 10, nullable = false)
    private String number;

    @Column(name = "LEGO_ALTERNATE_NUMBER")
    private String alternateNumber;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public PartEntity getPart() {
        return part;
    }

    public void setPart(PartEntity part) {
        this.part = part;
    }

    public ColorEntity getColor() {
        return color;
    }

    public void setColor(ColorEntity color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public static String getDescription() {
        return DESCRIPTION;
    }
}
