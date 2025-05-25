package com.finwise.service;

import com.finwise.dto.InvestmentOptionDTO;
import com.finwise.entity.InvestmentOption;
import com.finwise.repository.InvestmentOptionRepository;
import com.finwise.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvestmentOptionService {

    private final InvestmentOptionRepository investmentOptionRepository;
    private final ModelMapper modelMapper;

    public InvestmentOptionService(InvestmentOptionRepository investmentOptionRepository,
                                   ModelMapper modelMapper) {
        this.investmentOptionRepository = investmentOptionRepository;
        this.modelMapper = modelMapper;
    }

    public InvestmentOptionDTO createInvestmentOption(InvestmentOptionDTO dto) {
        InvestmentOption option = modelMapper.map(dto, InvestmentOption.class);
        InvestmentOption saved = investmentOptionRepository.save(option);
        return modelMapper.map(saved, InvestmentOptionDTO.class);
    }

    public InvestmentOptionDTO getInvestmentOption(Long id) throws ResourceNotFoundException {
        InvestmentOption option = investmentOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment option not found with id: " + id));
        return modelMapper.map(option, InvestmentOptionDTO.class);
    }

    public List<InvestmentOptionDTO> getAllInvestmentOptions() {
        return investmentOptionRepository.findAll().stream()
                .map(option -> modelMapper.map(option, InvestmentOptionDTO.class))
                .collect(Collectors.toList());
    }

    public List<InvestmentOptionDTO> getInvestmentOptionsByRiskLevel(String riskLevel) {
        return investmentOptionRepository.findByRiskLevel(riskLevel).stream()
                .map(option -> modelMapper.map(option, InvestmentOptionDTO.class))
                .collect(Collectors.toList());
    }

    public InvestmentOptionDTO updateInvestmentOption(Long id, InvestmentOptionDTO dto) throws ResourceNotFoundException {
        InvestmentOption existingOption = investmentOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment option not found with id: " + id));

        modelMapper.map(dto, existingOption);
        InvestmentOption updated = investmentOptionRepository.save(existingOption);
        return modelMapper.map(updated, InvestmentOptionDTO.class);
    }

    public void deleteInvestmentOption(Long id) throws ResourceNotFoundException {
        if (!investmentOptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Investment option not found with id: " + id);
        }
        investmentOptionRepository.deleteById(id);
    }

    public InvestmentOptionDTO patchInvestmentOption(Long id, Map<String, Object> updates) throws ResourceNotFoundException {
        InvestmentOption existingOption = investmentOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment option not found with id: " + id));

        updates.forEach((fieldName, fieldValue) -> {
            try {
                Field field = InvestmentOption.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                if (field.getType().equals(BigDecimal.class) && fieldValue instanceof Number) {
                    field.set(existingOption, new BigDecimal(fieldValue.toString()));
                } else if (field.getType().equals(String.class)) {
                    field.set(existingOption, fieldValue.toString());
                }
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException("Failed to patch field: " + fieldName, e);
            }
        });

        InvestmentOption updated = investmentOptionRepository.save(existingOption);
        return modelMapper.map(updated, InvestmentOptionDTO.class);
    }
}

