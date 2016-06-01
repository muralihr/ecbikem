package org.gubbilabs.ecbike.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the TypeOfIDCard entity.
 */
public class TypeOfIDCardDTO implements Serializable {

    private Long id;

    @NotNull
    private String authorizedIDName;


    @NotNull
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

        TypeOfIDCardDTO typeOfIDCardDTO = (TypeOfIDCardDTO) o;

        if ( ! Objects.equals(id, typeOfIDCardDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TypeOfIDCardDTO{" +
            "id=" + id +
            ", authorizedIDName='" + authorizedIDName + "'" +
            ", authorizationAgency='" + authorizationAgency + "'" +
            '}';
    }
}
