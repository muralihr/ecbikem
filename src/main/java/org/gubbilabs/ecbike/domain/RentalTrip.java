package org.gubbilabs.ecbike.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A RentalTrip.
 */
@Entity
@Table(name = "rental_trip")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rentaltrip")
public class RentalTrip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "rent_start_time", nullable = false)
    private ZonedDateTime rentStartTime;

    //@NotNull
    @Column(name = "rent_stop_time", nullable = false)
    private ZonedDateTime rentStopTime;

    @Column(name = "fine_applied")
    private Boolean fineApplied;

    @ManyToOne
    private Bicycle rentedCycle;

    @ManyToOne
    private MemberMobile customer;

    @ManyToOne
    private RentalBufferNode startNode;

    @ManyToOne
    private RentalBufferNode stopNode;

    @ManyToOne
    private HourlyRentalCharges rentalCharge;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getRentStartTime() {
        return rentStartTime;
    }

    public void setRentStartTime(ZonedDateTime rentStartTime) {
        this.rentStartTime = rentStartTime;
    }

    public ZonedDateTime getRentStopTime() {
        return rentStopTime;
    }

    public void setRentStopTime(ZonedDateTime rentStopTime) {
        this.rentStopTime = rentStopTime;
    }

    public Boolean isFineApplied() {
        return fineApplied;
    }

    public void setFineApplied(Boolean fineApplied) {
        this.fineApplied = fineApplied;
    }

    public Bicycle getRentedCycle() {
        return rentedCycle;
    }

    public void setRentedCycle(Bicycle bicycle) {
        this.rentedCycle = bicycle;
    }

    public MemberMobile getCustomer() {
        return customer;
    }

    public void setCustomer(MemberMobile memberMobile) {
        this.customer = memberMobile;
    }

    public RentalBufferNode getStartNode() {
        return startNode;
    }

    public void setStartNode(RentalBufferNode rentalBufferNode) {
        this.startNode = rentalBufferNode;
    }

    public RentalBufferNode getStopNode() {
        return stopNode;
    }

    public void setStopNode(RentalBufferNode rentalBufferNode) {
        this.stopNode = rentalBufferNode;
    }

    public HourlyRentalCharges getRentalCharge() {
        return rentalCharge;
    }

    public void setRentalCharge(HourlyRentalCharges hourlyRentalCharges) {
        this.rentalCharge = hourlyRentalCharges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RentalTrip rentalTrip = (RentalTrip) o;
        if(rentalTrip.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, rentalTrip.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RentalTrip{" +
            "id=" + id +
            ", rentStartTime='" + rentStartTime + "'" +
            ", rentStopTime='" + rentStopTime + "'" +
            ", fineApplied='" + fineApplied + "'" +
            '}';
    }
}
