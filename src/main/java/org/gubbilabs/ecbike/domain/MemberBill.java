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
 * A MemberBill.
 */
@Entity
@Table(name = "member_bill")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "memberbill")
public class MemberBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_of_payment", nullable = false)
    private ZonedDateTime dateOfPayment;

    @ManyToOne
    private BillingNode billCenter;

    @ManyToOne
    private MemberMobile memberToBill;

    @ManyToOne
    private TripRentUnitsToChargeMap unitPrepaidCharge;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(ZonedDateTime dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public BillingNode getBillCenter() {
        return billCenter;
    }

    public void setBillCenter(BillingNode billingNode) {
        this.billCenter = billingNode;
    }

    public MemberMobile getMemberToBill() {
        return memberToBill;
    }

    public void setMemberToBill(MemberMobile memberMobile) {
        this.memberToBill = memberMobile;
    }

    public TripRentUnitsToChargeMap getUnitPrepaidCharge() {
        return unitPrepaidCharge;
    }

    public void setUnitPrepaidCharge(TripRentUnitsToChargeMap tripRentUnitsToChargeMap) {
        this.unitPrepaidCharge = tripRentUnitsToChargeMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberBill memberBill = (MemberBill) o;
        if(memberBill.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, memberBill.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MemberBill{" +
            "id=" + id +
            ", dateOfPayment='" + dateOfPayment + "'" +
            '}';
    }
}
