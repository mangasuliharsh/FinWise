package com.finwise.service;

import com.finwise.dto.EducationCostReferenceDTO;
import com.finwise.entity.EducationCostReference;
import com.finwise.repository.EducationCostReferenceRepository;
import com.finwise.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class EducationCostReferenceService {

    private final EducationCostReferenceRepository educationCostReferenceRepository;
    private final ModelMapper modelMapper;

    public EducationCostReferenceService(EducationCostReferenceRepository educationCostReferenceRepository,
                                         ModelMapper modelMapper) {
        this.educationCostReferenceRepository = educationCostReferenceRepository;
        this.modelMapper = modelMapper;
    }

    public EducationCostReferenceDTO createEducationCostReference(EducationCostReferenceDTO dto) {
        EducationCostReference reference = modelMapper.map(dto, EducationCostReference.class);
        reference.setCreatedDate(LocalDateTime.now());
        reference.setLastUpdatedDate(LocalDateTime.now());

        EducationCostReference saved = educationCostReferenceRepository.save(reference);
        return modelMapper.map(saved, EducationCostReferenceDTO.class);
    }

    public EducationCostReferenceDTO getEducationCostReference(Long id) throws ResourceNotFoundException {
        EducationCostReference reference = educationCostReferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education cost reference not found with id: " + id));
        return modelMapper.map(reference, EducationCostReferenceDTO.class);
    }

    public List<EducationCostReferenceDTO> getAllEducationCostReferences() {
        return educationCostReferenceRepository.findAll().stream()
                .map(reference -> modelMapper.map(reference, EducationCostReferenceDTO.class))
                .collect(Collectors.toList());
    }

    public List<EducationCostReferenceDTO> getEducationCostReferencesByLocation(String region) {
        return educationCostReferenceRepository.findByRegion(region).stream()
                .map(reference -> modelMapper.map(reference, EducationCostReferenceDTO.class))
                .collect(Collectors.toList());
    }

    public EducationCostReferenceDTO updateEducationCostReference(Long id, EducationCostReferenceDTO dto) throws ResourceNotFoundException {
        EducationCostReference existingReference = educationCostReferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education cost reference not found with id: " + id));

        modelMapper.map(dto, existingReference);
        existingReference.setLastUpdatedDate(LocalDateTime.now());

        EducationCostReference updated = educationCostReferenceRepository.save(existingReference);
        return modelMapper.map(updated, EducationCostReferenceDTO.class);
    }

    public void deleteEducationCostReference(Long id) throws ResourceNotFoundException {
        if (!educationCostReferenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Education cost reference not found with id: " + id);
        }
        educationCostReferenceRepository.deleteById(id);
    }

    public EducationCostReferenceDTO patchEducationCostReference(Long id, Map<String, Object> updates) throws ResourceNotFoundException {
        EducationCostReference existingReference = educationCostReferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education cost reference not found with id: " + id));

        updates.forEach((fieldName, fieldValue) -> {
            try {
                Field field = EducationCostReference.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                if (field.getType().equals(BigDecimal.class) && fieldValue instanceof Number) {
                    field.set(existingReference, new BigDecimal(fieldValue.toString()));
                } else if (field.getType().equals(String.class)) {
                    field.set(existingReference, fieldValue.toString());
                } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                    field.set(existingReference, Integer.parseInt(fieldValue.toString()));
                }
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException("Failed to patch field: " + fieldName, e);
            }
        });

        existingReference.setLastUpdatedDate(LocalDateTime.now());
        EducationCostReference updated = educationCostReferenceRepository.save(existingReference);
        return modelMapper.map(updated, EducationCostReferenceDTO.class);
    }
}
