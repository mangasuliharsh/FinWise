package com.finwise.service;

import com.finwise.dto.MarriageCostReferenceDTO;
import com.finwise.entity.MarriageCostReference;
import com.finwise.repository.MarriageCostReferenceRepository;
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
public class MarriageCostReferenceService {

    private final MarriageCostReferenceRepository marriageCostReferenceRepository;
    private final ModelMapper modelMapper;

    public MarriageCostReferenceService(MarriageCostReferenceRepository marriageCostReferenceRepository,
                                        ModelMapper modelMapper) {
        this.marriageCostReferenceRepository = marriageCostReferenceRepository;
        this.modelMapper = modelMapper;
    }

    public MarriageCostReferenceDTO createMarriageCostReference(MarriageCostReferenceDTO dto) {
        MarriageCostReference reference = modelMapper.map(dto, MarriageCostReference.class);
        reference.setCreatedDate(LocalDateTime.now());
        reference.setLastUpdatedDate(LocalDateTime.now());

        MarriageCostReference saved = marriageCostReferenceRepository.save(reference);
        return modelMapper.map(saved, MarriageCostReferenceDTO.class);
    }

    public MarriageCostReferenceDTO getMarriageCostReference(Long id) throws ResourceNotFoundException {
        MarriageCostReference reference = marriageCostReferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marriage cost reference not found with id: " + id));
        return modelMapper.map(reference, MarriageCostReferenceDTO.class);
    }

    public List<MarriageCostReferenceDTO> getAllMarriageCostReferences() {
        return marriageCostReferenceRepository.findAll().stream()
                .map(reference -> modelMapper.map(reference, MarriageCostReferenceDTO.class))
                .collect(Collectors.toList());
    }

    public List<MarriageCostReferenceDTO> getMarriageCostReferencesByLocation(String region) {
        return marriageCostReferenceRepository.findByRegion(region).stream()
                .map(reference -> modelMapper.map(reference, MarriageCostReferenceDTO.class))
                .collect(Collectors.toList());
    }

    public MarriageCostReferenceDTO updateMarriageCostReference(Long id, MarriageCostReferenceDTO dto) throws ResourceNotFoundException {
        MarriageCostReference existingReference = marriageCostReferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marriage cost reference not found with id: " + id));

        modelMapper.map(dto, existingReference);
        existingReference.setLastUpdatedDate(LocalDateTime.now());

        MarriageCostReference updated = marriageCostReferenceRepository.save(existingReference);
        return modelMapper.map(updated, MarriageCostReferenceDTO.class);
    }

    public void deleteMarriageCostReference(Long id) throws ResourceNotFoundException {
        if (!marriageCostReferenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Marriage cost reference not found with id: " + id);
        }
        marriageCostReferenceRepository.deleteById(id);
    }

    public MarriageCostReferenceDTO patchMarriageCostReference(Long id, Map<String, Object> updates) throws ResourceNotFoundException {
        MarriageCostReference existingReference = marriageCostReferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marriage cost reference not found with id: " + id));

        updates.forEach((fieldName, fieldValue) -> {
            try {
                Field field = MarriageCostReference.class.getDeclaredField(fieldName);
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
        MarriageCostReference updated = marriageCostReferenceRepository.save(existingReference);
        return modelMapper.map(updated, MarriageCostReferenceDTO.class);
    }
}
