package org.gubbilabs.ecbike.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RentalBufferNode.
 */
@Entity
@Table(name = "rental_buffer_node")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rentalbuffernode")
public class RentalBufferNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "station_name", nullable = false)
    private String stationName;

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

    @NotNull
    @Size(min = 1000, max = 35000)
    @Lob
    @Column(name = "photo_of_location", nullable = false)
    private byte[] photoOfLocation;

    @Column(name = "photo_of_location_content_type", nullable = false)    
    private String photoOfLocationContentType;

    @ManyToOne
    private User nodeManager;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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

    public byte[] getPhotoOfLocation() {
        return photoOfLocation;
    }

    public void setPhotoOfLocation(byte[] photoOfLocation) {
        this.photoOfLocation = photoOfLocation;
    }

    public String getPhotoOfLocationContentType() {
        return photoOfLocationContentType;
    }

    public void setPhotoOfLocationContentType(String photoOfLocationContentType) {
        this.photoOfLocationContentType = photoOfLocationContentType;
    }

    public User getNodeManager() {
        return nodeManager;
    }

    public void setNodeManager(User user) {
        this.nodeManager = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RentalBufferNode rentalBufferNode = (RentalBufferNode) o;
        if(rentalBufferNode.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, rentalBufferNode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RentalBufferNode{" +
            "id=" + id +
            ", stationName='" + stationName + "'" +
            ", address1='" + address1 + "'" +
            ", address2='" + address2 + "'" +
            ", city='" + city + "'" +
            ", state='" + state + "'" +
            ", zipcode='" + zipcode + "'" +
            ", longitudePos='" + longitudePos + "'" +
            ", latitudePos='" + latitudePos + "'" +
            ", colorCode='" + colorCode + "'" +
            ", photoOfLocation='" + photoOfLocation + "'" +
            ", photoOfLocationContentType='" + photoOfLocationContentType + "'" +
            '}';
    }
}
