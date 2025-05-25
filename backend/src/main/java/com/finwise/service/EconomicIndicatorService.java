package com.finwise.service;

import com.finwise.dto.EconomicIndicatorDTO;
import com.finwise.entity.EconomicIndicator;
import com.finwise.repository.EconomicIndicatorRepository;
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
public class EconomicIndicatorService {

    private final EconomicIndicatorRepository economicIndicatorRepository;
    private final ModelMapper modelMapper;

    public EconomicIndicatorService(EconomicIndicatorRepository economicIndicatorRepository,
                                    ModelMapper modelMapper) {
        this.economicIndicatorRepository = economicIndicatorRepository;
        this.modelMapper = modelMapper;
    }

    public EconomicIndicatorDTO createEconomicIndicator(EconomicIndicatorDTO dto) {
        EconomicIndicator indicator = modelMapper.map(dto, EconomicIndicator.class);
        indicator.setCreatedDate(LocalDateTime.now());
        indicator.setLastUpdatedDate(LocalDateTime.now());

        EconomicIndicator saved = economicIndicatorRepository.save(indicator);
        return modelMapper.map(saved, EconomicIndicatorDTO.class);
    }

    public EconomicIndicatorDTO getEconomicIndicator(Long id) throws ResourceNotFoundException {
        EconomicIndicator indicator = economicIndicatorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Economic indicator not found with id: " + id));
        return modelMapper.map(indicator, EconomicIndicatorDTO.class);
    }

    public List<EconomicIndicatorDTO> getAllEconomicIndicators() {
        return economicIndicatorRepository.findAll().stream()
                .map(indicator -> modelMapper.map(indicator, EconomicIndicatorDTO.class))
                .collect(Collectors.toList());
    }

    public EconomicIndicatorDTO updateEconomicIndicator(Long id, EconomicIndicatorDTO dto) throws ResourceNotFoundException {
        EconomicIndicator existingIndicator = economicIndicatorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Economic indicator not found with id: " + id));

        modelMapper.map(dto, existingIndicator);
        existingIndicator.setLastUpdatedDate(LocalDateTime.now());

        EconomicIndicator updated = economicIndicatorRepository.save(existingIndicator);
        return modelMapper.map(updated, EconomicIndicatorDTO.class);
    }

    public void deleteEconomicIndicator(Long id) throws ResourceNotFoundException {
        if (!economicIndicatorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Economic indicator not found with id: " + id);
        }
        economicIndicatorRepository.deleteById(id);
    }

    public EconomicIndicatorDTO patchEconomicIndicator(Long id, Map<String, Object> updates) throws ResourceNotFoundException {
        EconomicIndicator existingIndicator = economicIndicatorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Economic indicator not found with id: " + id));

        updates.forEach((fieldName, fieldValue) -> {
            try {
                Field field = EconomicIndicator.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                if (field.getType().equals(BigDecimal.class) && fieldValue instanceof Number) {
                    field.set(existingIndicator, new BigDecimal(fieldValue.toString()));
                } else if (field.getType().equals(String.class)) {
                    field.set(existingIndicator, fieldValue.toString());
                } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                    field.set(existingIndicator, Integer.parseInt(fieldValue.toString()));
                }
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException("Failed to patch field: " + fieldName, e);
            }
        });

        existingIndicator.setLastUpdatedDate(LocalDateTime.now());
        EconomicIndicator updated = economicIndicatorRepository.save(existingIndicator);
        return modelMapper.map(updated, EconomicIndicatorDTO.class);
    }
}
