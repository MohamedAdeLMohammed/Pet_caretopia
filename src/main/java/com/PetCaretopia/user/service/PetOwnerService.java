package com.PetCaretopia.user.service;




import com.PetCaretopia.user.entity.PetOwner;
import com.PetCaretopia.user.repository.PetOwnerRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PetOwnerService {

    private final PetOwnerRepository petOwnerRepository;

    public PetOwnerService(PetOwnerRepository petOwnerRepository) {
        this.petOwnerRepository = petOwnerRepository;
    }

    public PetOwner savePetOwner(PetOwner petOwner) {
        return petOwnerRepository.save(petOwner);
    }

    public Optional<PetOwner> getPetOwnerByUserId(Long userID) {
        return petOwnerRepository.findByUser_UserID(userID);
    }
}
