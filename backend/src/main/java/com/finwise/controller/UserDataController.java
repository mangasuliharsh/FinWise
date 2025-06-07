package com.finwise.controller;


import com.finwise.dto.UserFinancialDataDTO;
import com.finwise.entity.EducationPlan;
import com.finwise.entity.FamilyProfile;
import com.finwise.entity.MarriagePlan;
import com.finwise.service.EducationPlanService;
import com.finwise.service.FamilyProfileService;
import com.finwise.service.MarriagePlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserDataController {

    private final FamilyProfileService familyProfileService;
    private final EducationPlanService educationPlanService;
    private final MarriagePlanService marriagePlanService;

    @GetMapping("/api/user/financial-data")
    public UserFinancialDataDTO getUserFinancialData() {
        FamilyProfile familyProfile = familyProfileService.getLoggedInUserFamilyProfile();

        BigDecimal salary = familyProfile.getMonthlyIncome();
        BigDecimal expenses = familyProfile.getMonthlyExpenses();

        List<EducationPlan> educationPlans = educationPlanService.findByFamilyProfile(familyProfile);
        List<MarriagePlan> marriagePlans = marriagePlanService.findByFamilyProfile(familyProfile);

        return new UserFinancialDataDTO(salary, expenses, educationPlans, marriagePlans);
    }
}

