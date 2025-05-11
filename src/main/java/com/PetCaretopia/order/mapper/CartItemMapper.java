package com.PetCaretopia.order.mapper;


import com.PetCaretopia.order.DTO.CartItemDTO;
import com.PetCaretopia.order.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemDTO toDTO(CartItem cartItem);

    CartItem toEntity(CartItemDTO cartItemDTO);
}
