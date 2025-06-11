package com.PetCaretopia.user.service;
import com.PetCaretopia.user.DTO.PetOwnerDTO;
import com.PetCaretopia.user.Mapper.PetOwnerMapper;
import com.PetCaretopia.user.entity.PetOwner;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.PetOwnerRepository;
import com.PetCaretopia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetOwnerService {
    private final PetOwnerRepository petOwnerRepository;

    private final UserRepository userRepository;

    private final PetOwnerMapper petOwnerMapper;

    public PetOwnerDTO create(PetOwnerDTO dto) {
        User user = userRepository.findById(dto.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        PetOwner entity = petOwnerMapper.toEntity(dto, user);
        return petOwnerMapper.toDTO(petOwnerRepository.save(entity));

    }


    public PetOwner savePetOwner(PetOwner petOwner) {
        return petOwnerRepository.save(petOwner);
    }

    public Optional<PetOwner> getPetOwnerByUserId(Long userID) {
        return petOwnerRepository.findByUser_UserID(userID);
    }
}
