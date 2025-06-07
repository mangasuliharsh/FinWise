package com.finwise.dto;

import com.finwise.entity.EducationPlan;
import com.finwise.entity.MarriagePlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class UserFinancialDataDTO {
    private BigDecimal monthlySalary;
    private BigDecimal monthlyExpenses;
    private List<EducationPlan> educationPlans;
    private List<MarriagePlan> marriagePlans;
}
