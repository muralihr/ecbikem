package org.gubbilabs.ecbike.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TypeOfIDCard.
 */
@Entity
@Table(name = "type_of_id_card")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "typeofidcard")
public class TypeOfIDCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "authorized_id_name", nullable = false)
    private String authorizedIDName;

    @NotNull
    @Column(name = "authorization_agency", nullable = false)
    private String authorizationAgency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorizedIDName() {
        return authorizedIDName;
    }

    public void setAuthorizedIDName(String authorizedIDName) {
        this.authorizedIDName = authorizedIDName;
    }

    public String getAuthorizationAgency() {
        return authorizationAgency;
    }

    public void setAuthorizationAgency(String authorizationAgency) {
        this.authorizationAgency = authorizationAgency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypeOfIDCard typeOfIDCard = (TypeOfIDCard) o;
        if(typeOfIDCard.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, typeOfIDCard.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TypeOfIDCard{" +
            "id=" + id +
            ", authorizedIDName='" + authorizedIDName + "'" +
            ", authorizationAgency='" + authorizationAgency + "'" +
            '}';
    }
}
