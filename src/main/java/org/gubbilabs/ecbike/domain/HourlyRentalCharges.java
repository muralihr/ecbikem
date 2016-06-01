package org.gubbilabs.ecbike.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A HourlyRentalCharges.
 */
@Entity
@Table(name = "hourly_rental_charges")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "hourlyrentalcharges")
public class HourlyRentalCharges implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "deductable_units", nullable = false)
    private Integer deductableUnits;

    @NotNull
    @Column(name = "rented_hours", nullable = false)
    private Float rentedHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDeductableUnits() {
        return deductableUnits;
    }

    public void setDeductableUnits(Integer deductableUnits) {
        this.deductableUnits = deductableUnits;
    }

    public Float getRentedHours() {
        return rentedHours;
    }

    public void setRentedHours(Float rentedHours) {
        this.rentedHours = rentedHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HourlyRentalCharges hourlyRentalCharges = (HourlyRentalCharges) o;
        if(hourlyRentalCharges.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, hourlyRentalCharges.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HourlyRentalCharges{" +
            "id=" + id +
            ", deductableUnits='" + deductableUnits + "'" +
            ", rentedHours='" + rentedHours + "'" +
            '}';
    }
}
