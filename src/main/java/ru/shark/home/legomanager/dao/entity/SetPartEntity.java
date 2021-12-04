package ru.shark.home.legomanager.dao.entity;

import ru.shark.home.common.dao.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "LEGO_SET_PART")
public class SetPartEntity extends BaseEntity {
    private static final String DESCRIPTION = "Деталь набора";

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @Column(name = "LEGO_ID")
    @SequenceGenerator(name = "SetPartGenerator", sequenceName = "LEGO_SET_PART_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SetPartGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "LEGO_SET_ID", nullable = false)
    private SetEntity set;

    @ManyToOne
    @JoinColumn(name = "LEGO_PART_COLOR_ID", nullable = false)
    private PartColorEntity partColor;

    @Column(name = "LEGO_COUNT", nullable = false)
    private Integer count;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public SetEntity getSet() {
        return set;
    }

    public void setSet(SetEntity set) {
        this.set = set;
    }

    public PartColorEntity getPartColor() {
        return partColor;
    }

    public void setPartColor(PartColorEntity partColor) {
        this.partColor = partColor;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public static String getDescription() {
        return DESCRIPTION;
    }
}
