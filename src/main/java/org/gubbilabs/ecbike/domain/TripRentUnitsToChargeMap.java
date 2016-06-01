package org.gubbilabs.ecbike.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TripRentUnitsToChargeMap.
 */
@Entity
@Table(name = "trip_rent_units_to_charge_map")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "triprentunitstochargemap")
public class TripRentUnitsToChargeMap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "type_of_pass_or_fare", nullable = false)
    private String typeOfPassOrFare;

    @NotNull
    @Column(name = "pre_paid_units", nullable = false)
    private Integer prePaidUnits;

    @NotNull
    @Column(name = "charges_for_paid_units", nullable = false)
    private Float chargesForPaidUnits;

    @NotNull
    @Column(name = "expiration_period_in_months", nullable = false)
    private Integer expirationPeriodInMonths;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeOfPassOrFare() {
        return typeOfPassOrFare;
    }

    public void setTypeOfPassOrFare(String typeOfPassOrFare) {
        this.typeOfPassOrFare = typeOfPassOrFare;
    }

    public Integer getPrePaidUnits() {
        return prePaidUnits;
    }

    public void setPrePaidUnits(Integer prePaidUnits) {
        this.prePaidUnits = prePaidUnits;
    }

    public Float getChargesForPaidUnits() {
        return chargesForPaidUnits;
    }

    public void setChargesForPaidUnits(Float chargesForPaidUnits) {
        this.chargesForPaidUnits = chargesForPaidUnits;
    }

    public Integer getExpirationPeriodInMonths() {
        return expirationPeriodInMonths;
    }

    public void setExpirationPeriodInMonths(Integer expirationPeriodInMonths) {
        this.expirationPeriodInMonths = expirationPeriodInMonths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TripRentUnitsToChargeMap tripRentUnitsToChargeMap = (TripRentUnitsToChargeMap) o;
        if(tripRentUnitsToChargeMap.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tripRentUnitsToChargeMap.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TripRentUnitsToChargeMap{" +
            "id=" + id +
            ", typeOfPassOrFare='" + typeOfPassOrFare + "'" +
            ", prePaidUnits='" + prePaidUnits + "'" +
            ", chargesForPaidUnits='" + chargesForPaidUnits + "'" +
            ", expirationPeriodInMonths='" + expirationPeriodInMonths + "'" +
            '}';
    }
}
