package com.PetCaretopia.user.controller;



import com.PetCaretopia.user.entity.PetOwner;
import com.PetCaretopia.user.service.PetOwnerService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/petOwners")
public class PetOwnerController {

    private final PetOwnerService petOwnerService;

    public PetOwnerController(PetOwnerService petOwnerService) {
        this.petOwnerService = petOwnerService;
    }

    @PostMapping("/register")
    public PetOwner registerPetOwner(@RequestBody PetOwner petOwner) {
        return petOwnerService.savePetOwner(petOwner);
    }

    @GetMapping("/user/{userID}")
    public Optional<PetOwner> getPetOwnerByUserId(@PathVariable Long userID) {
        return petOwnerService.getPetOwnerByUserId(userID);
    }
}
