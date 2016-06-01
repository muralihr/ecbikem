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
 * A MemberMobile.
 */
@Entity
@Table(name = "member_mobile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "membermobile")
public class MemberMobile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "address_1")
    private String address1;

    @Column(name = "address_2")
    private String address2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @NotNull
    @Column(name = "zipcode", nullable = false)
    private String zipcode;

    @Column(name = "country")
    private String country;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")    
    private String photoContentType;

    @Lob
    @Column(name = "photo_id_proof")
    private byte[] photoIDProof;

    @Column(name = "photo_id_proof_content_type")    
    private String photoIDProofContentType;

    @Column(name = "my_current_rent_units")
    private Integer myCurrentRentUnits;

    @Column(name = "my_charged_rent_units")
    private Integer myChargedRentUnits;

    @Column(name = "behavior_status")
    private Integer behaviorStatus;

    @Column(name = "my_current_fine_charges")
    private Integer myCurrentFineCharges;

    @Column(name = "date_of_expiration")
    private LocalDate dateOfExpiration;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "pass_word")
    private String passWord;

    @Column(name = "activated")
    private Boolean activated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public byte[] getPhotoIDProof() {
        return photoIDProof;
    }

    public void setPhotoIDProof(byte[] photoIDProof) {
        this.photoIDProof = photoIDProof;
    }

    public String getPhotoIDProofContentType() {
        return photoIDProofContentType;
    }

    public void setPhotoIDProofContentType(String photoIDProofContentType) {
        this.photoIDProofContentType = photoIDProofContentType;
    }

    public Integer getMyCurrentRentUnits() {
        return myCurrentRentUnits;
    }

    public void setMyCurrentRentUnits(Integer myCurrentRentUnits) {
        this.myCurrentRentUnits = myCurrentRentUnits;
    }

    public Integer getMyChargedRentUnits() {
        return myChargedRentUnits;
    }

    public void setMyChargedRentUnits(Integer myChargedRentUnits) {
        this.myChargedRentUnits = myChargedRentUnits;
    }

    public Integer getBehaviorStatus() {
        return behaviorStatus;
    }

    public void setBehaviorStatus(Integer behaviorStatus) {
        this.behaviorStatus = behaviorStatus;
    }

    public Integer getMyCurrentFineCharges() {
        return myCurrentFineCharges;
    }

    public void setMyCurrentFineCharges(Integer myCurrentFineCharges) {
        this.myCurrentFineCharges = myCurrentFineCharges;
    }

    public LocalDate getDateOfExpiration() {
        return dateOfExpiration;
    }

    public void setDateOfExpiration(LocalDate dateOfExpiration) {
        this.dateOfExpiration = dateOfExpiration;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberMobile memberMobile = (MemberMobile) o;
        if(memberMobile.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, memberMobile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MemberMobile{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", dateOfBirth='" + dateOfBirth + "'" +
            ", emailId='" + emailId + "'" +
            ", mobileNo='" + mobileNo + "'" +
            ", address1='" + address1 + "'" +
            ", address2='" + address2 + "'" +
            ", city='" + city + "'" +
            ", state='" + state + "'" +
            ", zipcode='" + zipcode + "'" +
            ", country='" + country + "'" +
            ", photo='" + photo + "'" +
            ", photoContentType='" + photoContentType + "'" +
            ", photoIDProof='" + photoIDProof + "'" +
            ", photoIDProofContentType='" + photoIDProofContentType + "'" +
            ", myCurrentRentUnits='" + myCurrentRentUnits + "'" +
            ", myChargedRentUnits='" + myChargedRentUnits + "'" +
            ", behaviorStatus='" + behaviorStatus + "'" +
            ", myCurrentFineCharges='" + myCurrentFineCharges + "'" +
            ", dateOfExpiration='" + dateOfExpiration + "'" +
            ", userName='" + userName + "'" +
            ", passWord='" + passWord + "'" +
            ", activated='" + activated + "'" +
            '}';
    }
}
