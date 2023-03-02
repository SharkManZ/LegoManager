package ru.shark.home.legomanager.dao.entity;

import ru.shark.home.common.dao.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

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

    @OneToMany(mappedBy = "partColor", fetch = FetchType.EAGER)
    private Set<PartColorNumberEntity> numbers;

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

    public Set<PartColorNumberEntity> getNumbers() {
        return numbers;
    }

    public void setNumbers(Set<PartColorNumberEntity> numbers) {
        this.numbers = numbers;
    }

    public static String getDescription() {
        return DESCRIPTION;
    }
}
