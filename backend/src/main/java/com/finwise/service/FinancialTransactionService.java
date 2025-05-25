package com.finwise.service;

import com.finwise.dto.FinancialTransactionDTO;
import com.finwise.entity.FinancialTransaction;
import com.finwise.entity.User;
import com.finwise.repository.FinancialTransactionRepository;
import com.finwise.repository.UserRepository;
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
public class FinancialTransactionService {

    private final FinancialTransactionRepository financialTransactionRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public FinancialTransactionService(FinancialTransactionRepository financialTransactionRepository,
                                       UserRepository userRepository,
                                       ModelMapper modelMapper) {
        this.financialTransactionRepository = financialTransactionRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public FinancialTransactionDTO createTransaction(Long userId, FinancialTransactionDTO dto) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        FinancialTransaction transaction = modelMapper.map(dto, FinancialTransaction.class);
        transaction.setUser(user);
        transaction.setCreatedDate(LocalDateTime.now());

        FinancialTransaction saved = financialTransactionRepository.save(transaction);
        return modelMapper.map(saved, FinancialTransactionDTO.class);
    }

    public FinancialTransactionDTO getTransaction(Long id) throws ResourceNotFoundException {
        FinancialTransaction transaction = financialTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial transaction not found with id: " + id));
        return modelMapper.map(transaction, FinancialTransactionDTO.class);
    }

    public List<FinancialTransactionDTO> getAllTransactionsByFamily(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return financialTransactionRepository.findByUser(user).stream()
                .map(transaction -> modelMapper.map(transaction, FinancialTransactionDTO.class))
                .collect(Collectors.toList());
    }

    public List<FinancialTransactionDTO> getRecentTransactions(Long userId, int limit) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return financialTransactionRepository.findByUserOrderByTransactionDateDesc(user).stream()
                .map(transaction -> modelMapper.map(transaction, FinancialTransactionDTO.class))
                .collect(Collectors.toList());
    }

    public FinancialTransactionDTO updateTransaction(Long id, FinancialTransactionDTO dto) throws ResourceNotFoundException {
        FinancialTransaction existingTransaction = financialTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial transaction not found with id: " + id));

        modelMapper.map(dto, existingTransaction);
        FinancialTransaction updated = financialTransactionRepository.save(existingTransaction);
        return modelMapper.map(updated, FinancialTransactionDTO.class);
    }

    public void deleteTransaction(Long id) throws ResourceNotFoundException {
        if (!financialTransactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Financial transaction not found with id: " + id);
        }
        financialTransactionRepository.deleteById(id);
    }

    public FinancialTransactionDTO patchTransaction(Long id, Map<String, Object> updates) throws ResourceNotFoundException {
        FinancialTransaction existingTransaction = financialTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial transaction not found with id: " + id));

        updates.forEach((fieldName, fieldValue) -> {
            try {
                Field field = FinancialTransaction.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                if (field.getType().equals(BigDecimal.class) && fieldValue instanceof Number) {
                    field.set(existingTransaction, new BigDecimal(fieldValue.toString()));
                } else if (field.getType().equals(String.class)) {
                    field.set(existingTransaction, fieldValue.toString());
                } else if (field.getType().equals(LocalDateTime.class)) {
                    field.set(existingTransaction, LocalDateTime.parse(fieldValue.toString()));
                } else if (field.getType().equals(Long.class)) {
                    field.set(existingTransaction, Long.parseLong(fieldValue.toString()));
                }
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException("Failed to patch field: " + fieldName, e);
            }
        });

        FinancialTransaction updated = financialTransactionRepository.save(existingTransaction);
        return modelMapper.map(updated, FinancialTransactionDTO.class);
    }
}
