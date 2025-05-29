package com.PetCaretopia.pet.service;

import com.PetCaretopia.pet.DTO.PetTypeDTO;
import com.PetCaretopia.pet.Mapper.PetTypeMapper;
import com.PetCaretopia.pet.entity.PetType;
import com.PetCaretopia.pet.repository.PetTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetTypeService {

    private final PetTypeRepository repo;

    public List<PetTypeDTO> getAll() {
        return repo.findAll().stream().map(PetTypeMapper::toDTO).toList();
    }

    public PetTypeDTO getById(Long id) {
        PetType type = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Pet type not found"));
        return PetTypeMapper.toDTO(type);
    }

    public PetTypeDTO create(PetTypeDTO dto) {
        PetType type = PetTypeMapper.toEntity(dto);
        return PetTypeMapper.toDTO(repo.save(type));
    }

    public PetTypeDTO update(Long id, PetTypeDTO dto) {
        PetType existing = repo.findById(id).orElseThrow();
        existing.setTypeName(dto.getTypeName());
        return PetTypeMapper.toDTO(repo.save(existing));
    }

    public void delete(Long id) {
        PetType type = repo.findById(id).orElseThrow();
        repo.delete(type);
    }
}
