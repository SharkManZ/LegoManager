package ru.shark.home.legomanager.dao.entity;

import ru.shark.home.common.dao.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "LEGO_SET")
public class SetEntity extends BaseEntity {
    private static final String DESCRIPTION = "Набор";

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @Column(name = "LEGO_ID")
    @SequenceGenerator(name = "SetsGenerator", sequenceName = "LEGO_SET_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SetsGenerator")
    private Long id;

    @Column(name = "LEGO_NAME", nullable = false)
    private String name;

    @Column(name = "LEGO_NUMBER", nullable = false, length = 10)
    private String number;

    @Column(name = "LEGO_YEAR", nullable = false)
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "LEGO_SERIES_ID", nullable = false)
    private SeriesEntity series;

    @OneToMany(mappedBy = "set")
    private Set<SetPartEntity> parts;

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public SeriesEntity getSeries() {
        return series;
    }

    public void setSeries(SeriesEntity series) {
        this.series = series;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Set<SetPartEntity> getParts() {
        return parts;
    }

    public void setParts(Set<SetPartEntity> parts) {
        this.parts = parts;
    }

    public static String getDescription() {
        return DESCRIPTION;
    }
}
