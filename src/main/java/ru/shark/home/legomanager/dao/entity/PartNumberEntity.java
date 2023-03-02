package ru.shark.home.legomanager.dao.entity;

import ru.shark.home.common.dao.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "LEGO_PART_NUMBER")
public class PartNumberEntity extends BaseEntity {
    private static final String DESCRIPTION = "Номер детали";

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @Column(name = "LEGO_ID")
    @SequenceGenerator(name = "PartNumberGenerator", sequenceName = "LEGO_PART_NUMBER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PartNumberGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "LEGO_PART_ID", nullable = false)
    private PartEntity part;

    @Column(name = "LEGO_NUMBER", nullable = false)
    private String number;

    @Column(name = "LEGO_MAIN", nullable = false)
    private Boolean main;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Boolean getMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    public static String getDescription() {
        return DESCRIPTION;
    }
}
