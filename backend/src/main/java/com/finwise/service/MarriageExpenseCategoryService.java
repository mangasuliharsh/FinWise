package com.finwise.service;

import com.finwise.dto.MarriageExpenseCategoryDTO;
import com.finwise.entity.MarriageExpenseCategory;
import com.finwise.entity.MarriagePlan;
import com.finwise.repository.MarriageExpenseCategoryRepository;
import com.finwise.repository.MarriagePlanRepository;
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
public class MarriageExpenseCategoryService {

    private final MarriageExpenseCategoryRepository marriageExpenseCategoryRepository;
    private final MarriagePlanRepository marriagePlanRepository;
    private final ModelMapper modelMapper;

    public MarriageExpenseCategoryService(MarriageExpenseCategoryRepository marriageExpenseCategoryRepository,
                                          MarriagePlanRepository marriagePlanRepository,
                                          ModelMapper modelMapper) {
        this.marriageExpenseCategoryRepository = marriageExpenseCategoryRepository;
        this.marriagePlanRepository = marriagePlanRepository;
        this.modelMapper = modelMapper;
    }

    public MarriageExpenseCategoryDTO createExpenseCategory(Long marriagePlanId, MarriageExpenseCategoryDTO dto) throws ResourceNotFoundException {
        MarriagePlan marriagePlan = marriagePlanRepository.findById(marriagePlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Marriage plan not found with id: " + marriagePlanId));

        MarriageExpenseCategory category = modelMapper.map(dto, MarriageExpenseCategory.class);
        category.setMarriagePlan(marriagePlan);
        category.setCreatedDate(LocalDateTime.now());
        category.setLastUpdatedDate(LocalDateTime.now());

        MarriageExpenseCategory saved = marriageExpenseCategoryRepository.save(category);
        return modelMapper.map(saved, MarriageExpenseCategoryDTO.class);
    }

    public MarriageExpenseCategoryDTO getExpenseCategory(Long id) throws ResourceNotFoundException {
        MarriageExpenseCategory category = marriageExpenseCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marriage expense category not found with id: " + id));
        return modelMapper.map(category, MarriageExpenseCategoryDTO.class);
    }

    public List<MarriageExpenseCategoryDTO> getAllExpenseCategoriesByPlan(Long marriagePlanId) throws ResourceNotFoundException {
        MarriagePlan marriagePlan = marriagePlanRepository.findById(marriagePlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Marriage plan not found with id: " + marriagePlanId));

        return marriageExpenseCategoryRepository.findByMarriagePlan(marriagePlan).stream()
                .map(category -> modelMapper.map(category, MarriageExpenseCategoryDTO.class))
                .collect(Collectors.toList());
    }

    public MarriageExpenseCategoryDTO updateExpenseCategory(Long id, MarriageExpenseCategoryDTO dto) throws ResourceNotFoundException {
        MarriageExpenseCategory existingCategory = marriageExpenseCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marriage expense category not found with id: " + id));

        modelMapper.map(dto, existingCategory);
        existingCategory.setLastUpdatedDate(LocalDateTime.now());

        MarriageExpenseCategory updated = marriageExpenseCategoryRepository.save(existingCategory);
        return modelMapper.map(updated, MarriageExpenseCategoryDTO.class);
    }

    public void deleteExpenseCategory(Long id) throws ResourceNotFoundException {
        if (!marriageExpenseCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Marriage expense category not found with id: " + id);
        }
        marriageExpenseCategoryRepository.deleteById(id);
    }

    public MarriageExpenseCategoryDTO patchExpenseCategory(Long id, Map<String, Object> updates) throws ResourceNotFoundException {
        MarriageExpenseCategory existingCategory = marriageExpenseCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marriage expense category not found with id: " + id));

        updates.forEach((fieldName, fieldValue) -> {
            try {
                Field field = MarriageExpenseCategory.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                if (field.getType().equals(BigDecimal.class) && fieldValue instanceof Number) {
                    field.set(existingCategory, new BigDecimal(fieldValue.toString()));
                } else if (field.getType().equals(String.class)) {
                    field.set(existingCategory, fieldValue.toString());
                }
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException("Failed to patch field: " + fieldName, e);
            }
        });

        existingCategory.setLastUpdatedDate(LocalDateTime.now());
        MarriageExpenseCategory updated = marriageExpenseCategoryRepository.save(existingCategory);
        return modelMapper.map(updated, MarriageExpenseCategoryDTO.class);
    }
}
