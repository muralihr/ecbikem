package org.gubbilabs.ecbike.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CycleToStockNodeMapper.
 */
@Entity
@Table(name = "cycle_to_stock_node_mapper")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cycletostocknodemapper")
public class CycleToStockNodeMapper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private StockBufferNode nodeDest;

    @ManyToOne
    private Bicycle movedCycle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockBufferNode getNodeDest() {
        return nodeDest;
    }

    public void setNodeDest(StockBufferNode stockBufferNode) {
        this.nodeDest = stockBufferNode;
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
        CycleToStockNodeMapper cycleToStockNodeMapper = (CycleToStockNodeMapper) o;
        if(cycleToStockNodeMapper.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cycleToStockNodeMapper.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CycleToStockNodeMapper{" +
            "id=" + id +
            '}';
    }
}
