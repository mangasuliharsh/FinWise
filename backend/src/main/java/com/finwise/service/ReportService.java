package com.finwise.service;

import com.finwise.dto.ReportDTO;
import com.finwise.entity.Report;
import com.finwise.entity.User;
import com.finwise.repository.ReportRepository;
import com.finwise.repository.UserRepository;
import com.finwise.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ReportService(ReportRepository reportRepository,
                         UserRepository userRepository,
                         ModelMapper modelMapper) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public ReportDTO createReport(Long userId, ReportDTO dto) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Report report = modelMapper.map(dto, Report.class);
        report.setUser(user);
        report.setGeneratedDate(LocalDateTime.now());

        Report saved = reportRepository.save(report);
        return modelMapper.map(saved, ReportDTO.class);
    }

    public ReportDTO getReport(Long id) throws ResourceNotFoundException {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        return modelMapper.map(report, ReportDTO.class);
    }

    public List<ReportDTO> getAllReportsByFamily(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return reportRepository.findByUser(user).stream()
                .map(report -> modelMapper.map(report, ReportDTO.class))
                .collect(Collectors.toList());
    }

    public List<ReportDTO> getReportsByType(Long userId, String reportType) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return reportRepository.findByUserAndReportType(user, reportType).stream()
                .map(report -> modelMapper.map(report, ReportDTO.class))
                .collect(Collectors.toList());
    }

    public ReportDTO updateReport(Long id, ReportDTO dto) throws ResourceNotFoundException {
        Report existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        modelMapper.map(dto, existingReport);
        Report updated = reportRepository.save(existingReport);
        return modelMapper.map(updated, ReportDTO.class);
    }

    public void deleteReport(Long id) throws ResourceNotFoundException {
        if (!reportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Report not found with id: " + id);
        }
        reportRepository.deleteById(id);
    }

    public ReportDTO patchReport(Long id, Map<String, Object> updates) throws ResourceNotFoundException {
        Report existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        updates.forEach((fieldName, fieldValue) -> {
            try {
                Field field = Report.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                if (field.getType().equals(String.class)) {
                    field.set(existingReport, fieldValue.toString());
                }
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException("Failed to patch field: " + fieldName, e);
            }
        });

        Report updated = reportRepository.save(existingReport);
        return modelMapper.map(updated, ReportDTO.class);
    }
}
