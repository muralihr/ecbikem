package org.gubbilabs.ecbike.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CycleToCustomerMapper.
 */
@Entity
@Table(name = "cycle_to_customer_mapper")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cycletocustomermapper")
public class CycleToCustomerMapper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private MemberMobile rentedCustomer;

    @ManyToOne
    private Bicycle movedCycle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MemberMobile getRentedCustomer() {
        return rentedCustomer;
    }

    public void setRentedCustomer(MemberMobile memberMobile) {
        this.rentedCustomer = memberMobile;
    }

    public Bicycle getMovedCycle() {
        return movedCycle;
    }

    public void setMovedCycle(Bicycle bicycle) {
        this.movedCycle = bicycle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CycleToCustomerMapper cycleToCustomerMapper = (CycleToCustomerMapper) o;
        if(cycleToCustomerMapper.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cycleToCustomerMapper.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CycleToCustomerMapper{" +
            "id=" + id +
            '}';
    }
}
