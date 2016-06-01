package org.gubbilabs.ecbike.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockBufferNode.
 */
@Entity
@Table(name = "stock_buffer_node")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "stockbuffernode")
public class StockBufferNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "godown_name", nullable = false)
    private String godownName;

    @NotNull
    @Column(name = "storage_capacity", nullable = false)
    private String storageCapacity;

    @NotNull
    @Column(name = "address_1", nullable = false)
    private String address1;

    @NotNull
    @Column(name = "address_2", nullable = false)
    private String address2;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "state", nullable = false)
    private String state;

    @NotNull
    @Column(name = "zipcode", nullable = false)
    private String zipcode;

    @NotNull
    @Column(name = "longitude_pos", nullable = false)
    private Double longitudePos;

    @NotNull
    @Column(name = "latitude_pos", nullable = false)
    private Double latitudePos;

    @NotNull
    @Column(name = "color_code", nullable = false)
    private String colorCode;

    @ManyToOne
    private User stockManager;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGodownName() {
        return godownName;
    }

    public void setGodownName(String godownName) {
        this.godownName = godownName;
    }

    public String getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(String storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Double getLongitudePos() {
        return longitudePos;
    }

    public void setLongitudePos(Double longitudePos) {
        this.longitudePos = longitudePos;
    }

    public Double getLatitudePos() {
        return latitudePos;
    }

    public void setLatitudePos(Double latitudePos) {
        this.latitudePos = latitudePos;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public User getStockManager() {
        return stockManager;
    }

    public void setStockManager(User user) {
        this.stockManager = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockBufferNode stockBufferNode = (StockBufferNode) o;
        if(stockBufferNode.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockBufferNode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockBufferNode{" +
            "id=" + id +
            ", godownName='" + godownName + "'" +
            ", storageCapacity='" + storageCapacity + "'" +
            ", address1='" + address1 + "'" +
            ", address2='" + address2 + "'" +
            ", city='" + city + "'" +
            ", state='" + state + "'" +
            ", zipcode='" + zipcode + "'" +
            ", longitudePos='" + longitudePos + "'" +
            ", latitudePos='" + latitudePos + "'" +
            ", colorCode='" + colorCode + "'" +
            '}';
    }
}
