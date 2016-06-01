package org.gubbilabs.ecbike.web.rest.mapper;

import org.gubbilabs.ecbike.domain.*;
import org.gubbilabs.ecbike.web.rest.dto.TypeOfIDCardDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TypeOfIDCard and its DTO TypeOfIDCardDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeOfIDCardMapper {

    TypeOfIDCardDTO typeOfIDCardToTypeOfIDCardDTO(TypeOfIDCard typeOfIDCard);

    List<TypeOfIDCardDTO> typeOfIDCardsToTypeOfIDCardDTOs(List<TypeOfIDCard> typeOfIDCards);

    TypeOfIDCard typeOfIDCardDTOToTypeOfIDCard(TypeOfIDCardDTO typeOfIDCardDTO);

    List<TypeOfIDCard> typeOfIDCardDTOsToTypeOfIDCards(List<TypeOfIDCardDTO> typeOfIDCardDTOs);
}
