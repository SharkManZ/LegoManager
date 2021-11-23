package ru.shark.home.legomanager.dao.entity;

import ru.shark.home.common.dao.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "LEGO_PART")
public class PartEntity extends BaseEntity {
    private static final String DESCRIPTION = "Детали";

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @Column(name = "LEGO_ID")
    @SequenceGenerator(name = "PartGenerator", sequenceName = "LEGO_PART_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PartGenerator")
    private Long id;

    @Column(name = "LEGO_NAME", nullable = false)
    private String name;

    @Column(name = "LEGO_NUMBER", length = 10, nullable = false)
    private String number;

    @ManyToOne
    @JoinColumn(name = "LEGO_PART_CATEGORY_ID", nullable = false)
    private PartCategoryEntity category;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public PartCategoryEntity getCategory() {
        return category;
    }

    public void setCategory(PartCategoryEntity category) {
        this.category = category;
    }

    public static String getDescription() {
        return DESCRIPTION;
    }
}
