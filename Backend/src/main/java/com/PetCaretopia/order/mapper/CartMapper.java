package com.PetCaretopia.order.mapper;

import com.PetCaretopia.order.DTO.CartDTO;
import com.PetCaretopia.order.entity.Cart;
import com.PetCaretopia.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

//    // ✅ تحويل User إلى userId

    @Mapping(target = "userId", source = "user", qualifiedByName = "mapUserToUserId")
   CartDTO toDTO(Cart cart);

    // ✅ تحويل userId إلى User

    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUserFromId")
    Cart toEntity(CartDTO cartDTO);




    @Named("mapUserToUserId")
    default Long mapUserToUserId(User user) {
        return (user != null) ? user.getUserID() : null;
    }

    @Named("mapUserFromId")
    default User mapUserFromId(Long userId) {
        if (userId == null) return null;
        User user = new User();
        user.setUserID(userId);
        return user;
    }
}
