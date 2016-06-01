package org.gubbilabs.ecbike.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Bicycle.
 */
@Entity
@Table(name = "bicycle")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bicycle")
public class Bicycle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "tag_id", nullable = false)
    private String tagId;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "cycle_manufacturer", length = 20, nullable = false)
    private String cycleManufacturer;

    @NotNull
    @Column(name = "date_of_purchase", nullable = false)
    private LocalDate dateOfPurchase;

    @NotNull
    @Column(name = "cost_of_cycle", nullable = false)
    private Float costOfCycle;

    @NotNull
    @Column(name = "insurance_no", nullable = false)
    private String insuranceNo;

    @NotNull
    @Column(name = "move_status", nullable = false)
    private Integer moveStatus;

    @NotNull
    @Column(name = "insurance_status", nullable = false)
    private Integer insuranceStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getCycleManufacturer() {
        return cycleManufacturer;
    }

    public void setCycleManufacturer(String cycleManufacturer) {
        this.cycleManufacturer = cycleManufacturer;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public Float getCostOfCycle() {
        return costOfCycle;
    }

    public void setCostOfCycle(Float costOfCycle) {
        this.costOfCycle = costOfCycle;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public Integer getMoveStatus() {
        return moveStatus;
    }

    public void setMoveStatus(Integer moveStatus) {
        this.moveStatus = moveStatus;
    }

    public Integer getInsuranceStatus() {
        return insuranceStatus;
    }

    public void setInsuranceStatus(Integer insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bicycle bicycle = (Bicycle) o;
        if(bicycle.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bicycle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bicycle{" +
            "id=" + id +
            ", tagId='" + tagId + "'" +
            ", cycleManufacturer='" + cycleManufacturer + "'" +
            ", dateOfPurchase='" + dateOfPurchase + "'" +
            ", costOfCycle='" + costOfCycle + "'" +
            ", insuranceNo='" + insuranceNo + "'" +
            ", moveStatus='" + moveStatus + "'" +
            ", insuranceStatus='" + insuranceStatus + "'" +
            '}';
    }
}
