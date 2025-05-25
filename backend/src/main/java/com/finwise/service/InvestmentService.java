package com.finwise.service;

import com.finwise.dto.InvestmentDTO;
import com.finwise.entity.Investment;
import com.finwise.entity.SavingsPlan;
import com.finwise.entity.InvestmentOption;
import com.finwise.repository.InvestmentRepository;
import com.finwise.repository.SavingsPlanRepository;
import com.finwise.repository.InvestmentOptionRepository;
import com.finwise.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final SavingsPlanRepository savingsPlanRepository;
    private final InvestmentOptionRepository investmentOptionRepository;
    private final ModelMapper modelMapper;

    public InvestmentService(InvestmentRepository investmentRepository,
                             SavingsPlanRepository savingsPlanRepository,
                             InvestmentOptionRepository investmentOptionRepository,
                             ModelMapper modelMapper) {
        this.investmentRepository = investmentRepository;
        this.savingsPlanRepository = savingsPlanRepository;
        this.investmentOptionRepository = investmentOptionRepository;
        this.modelMapper = modelMapper;
    }

    public InvestmentDTO createInvestment(Long savingsPlanId, InvestmentDTO dto) throws ResourceNotFoundException {
        SavingsPlan savingsPlan = savingsPlanRepository.findById(savingsPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Savings plan not found with id: " + savingsPlanId));

        InvestmentOption investmentOption = investmentOptionRepository.findById(dto.getInvestmentOptionId())
                .orElseThrow(() -> new ResourceNotFoundException("Investment option not found with id: " + dto.getInvestmentOptionId()));

        Investment investment = modelMapper.map(dto, Investment.class);
        investment.setSavingsPlan(savingsPlan);
        investment.setInvestmentOption(investmentOption);
        investment.setCreatedDate(LocalDateTime.now());
        investment.setLastUpdatedDate(LocalDateTime.now());

        Investment saved = investmentRepository.save(investment);
        return modelMapper.map(saved, InvestmentDTO.class);
    }

    public InvestmentDTO getInvestment(Long id) throws ResourceNotFoundException {
        Investment investment = investmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found with id: " + id));
        return modelMapper.map(investment, InvestmentDTO.class);
    }

    public List<InvestmentDTO> getAllInvestmentsByFamily(Long familyProfileId) {
        return investmentRepository.findBySavingsPlan_FamilyProfile_Id(familyProfileId).stream()
                .map(investment -> modelMapper.map(investment, InvestmentDTO.class))
                .collect(Collectors.toList());
    }

    public List<InvestmentDTO> getInvestmentsBySavingsPlan(Long savingsPlanId) throws ResourceNotFoundException {
        SavingsPlan savingsPlan = savingsPlanRepository.findById(savingsPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Savings plan not found with id: " + savingsPlanId));

        return investmentRepository.findBySavingsPlan(savingsPlan).stream()
                .map(investment -> modelMapper.map(investment, InvestmentDTO.class))
                .collect(Collectors.toList());
    }

    public InvestmentDTO updateInvestment(Long id, InvestmentDTO dto) throws ResourceNotFoundException {
        Investment existingInvestment = investmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found with id: " + id));

        modelMapper.map(dto, existingInvestment);
        existingInvestment.setLastUpdatedDate(LocalDateTime.now());

        Investment updated = investmentRepository.save(existingInvestment);
        return modelMapper.map(updated, InvestmentDTO.class);
    }

    public void deleteInvestment(Long id) throws ResourceNotFoundException {
        if (!investmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Investment not found with id: " + id);
        }
        investmentRepository.deleteById(id);
    }

    public InvestmentDTO patchInvestment(Long id, Map<String, Object> updates) throws ResourceNotFoundException {
        Investment existingInvestment = investmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found with id: " + id));

        updates.forEach((fieldName, fieldValue) -> {
            try {
                Field field = Investment.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                if (field.getType().equals(BigDecimal.class) && fieldValue instanceof Number) {
                    field.set(existingInvestment, new BigDecimal(fieldValue.toString()));
                } else if (field.getType().equals(LocalDate.class)) {
                    field.set(existingInvestment, LocalDate.parse(fieldValue.toString()));
                } else if (field.getType().equals(String.class)) {
                    field.set(existingInvestment, fieldValue.toString());
                }
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException("Failed to patch field: " + fieldName, e);
            }
        });

        existingInvestment.setLastUpdatedDate(LocalDateTime.now());
        Investment updated = investmentRepository.save(existingInvestment);
        return modelMapper.map(updated, InvestmentDTO.class);
    }
}
