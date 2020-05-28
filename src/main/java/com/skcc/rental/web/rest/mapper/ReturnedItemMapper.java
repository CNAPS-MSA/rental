package com.skcc.rental.web.rest.mapper;


import com.skcc.rental.domain.*;
import com.skcc.rental.web.rest.dto.ReturnedItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReturnedItem} and its DTO {@link ReturnedItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {RentalMapper.class})
public interface ReturnedItemMapper extends EntityMapper<ReturnedItemDTO, ReturnedItem> {

    @Mapping(source = "rental.id", target = "rentalId")
    ReturnedItemDTO toDto(ReturnedItem returnedItem);

    @Mapping(source = "rentalId", target = "rental")
    ReturnedItem toEntity(ReturnedItemDTO returnedItemDTO);

    default ReturnedItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReturnedItem returnedItem = new ReturnedItem();
        returnedItem.setId(id);
        return returnedItem;
    }
}
