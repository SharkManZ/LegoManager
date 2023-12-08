package ru.shark.home.legomanager.dao.entity.load;

import ru.shark.home.common.dao.entity.BaseEntity;
import ru.shark.home.legomanager.dao.entity.PartColorEntity;

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
@Table(name = "LEGO_PART_LOAD_COMPARISON")
public class PartLoadComparisonEntity extends BaseEntity {
    private static final String DESCRIPTION = "Сопоставление деталей импорта без номера с деталями из справочника";

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @Column(name = "LEGO_ID")
    @SequenceGenerator(name = "PartLoadComparisonGenerator", sequenceName = "LEGO_PART_LOAD_COMPARISON_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PartLoadComparisonGenerator")
    private Long id;

    @Column(name = "LEGO_LOAD_NUMBER", length = 50, nullable = false)
    private String loadNumber;

    @Column(name = "LEGO_PART_NAME", nullable = false)
    private String partName;

    @ManyToOne
    @JoinColumn(name = "LEGO_PART_COLOR_ID", nullable = false)
    private PartColorEntity partColor;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getLoadNumber() {
        return loadNumber;
    }

    public void setLoadNumber(String loadNumber) {
        this.loadNumber = loadNumber;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public PartColorEntity getPartColor() {
        return partColor;
    }

    public void setPartColor(PartColorEntity partColor) {
        this.partColor = partColor;
    }

    public static String getDescription() {
        return DESCRIPTION;
    }
}
