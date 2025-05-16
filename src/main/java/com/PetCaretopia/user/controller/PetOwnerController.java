package com.PetCaretopia.user.controller;

import com.PetCaretopia.user.DTO.PetOwnerDTO;
import com.PetCaretopia.user.service.PetOwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class PetOwnerController {

    private final PetOwnerService service;

    @PostMapping
    public ResponseEntity<PetOwnerDTO> create(@RequestBody @Valid PetOwnerDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
}
