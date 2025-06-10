package com.finwise.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComprehensiveReportDTO {

    private Long userId;
    private LocalDateTime reportGeneratedAt;
    private FamilyProfileDTO familyProfile;
    private List<MarriagePlanProgressDTO> marriagePlans;
    private List<EducationPlanProgressDTO> educationPlans;
    private List<InvestmentPlanProgressDTO> investmentPlans;
    private OverallSummaryDTO overallSummary;
}
