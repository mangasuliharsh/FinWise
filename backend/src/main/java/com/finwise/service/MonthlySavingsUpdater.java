package com.finwise.service;

import com.finwise.entity.EducationPlan;
import com.finwise.entity.MarriagePlan;
import com.finwise.repository.EducationPlanRepository;
import com.finwise.repository.MarriagePlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class MonthlySavingsUpdater {

    @Autowired
    private EducationPlanRepository educationPlanRepository;

    @Autowired
    private MarriagePlanRepository marriagePlanRepository;

    // This will run at midnight on the first day of every month
    @Scheduled(cron = "0 0 0 1 * *")
    public void addMonthlyContributionsToSavings() {
        // Update Education Plans
        List<EducationPlan> educationPlans = educationPlanRepository.findAll();
        for (EducationPlan plan : educationPlans) {
            BigDecimal currentSavings = plan.getCurrentSavings() != null ? plan.getCurrentSavings() : BigDecimal.ZERO;
            BigDecimal monthlyContribution = plan.getMonthlyContribution() != null ? plan.getMonthlyContribution() : BigDecimal.ZERO;
            plan.setCurrentSavings(currentSavings.add(monthlyContribution));
            educationPlanRepository.save(plan);
        }

        // Update Marriage Plans
        List<MarriagePlan> marriagePlans = marriagePlanRepository.findAll();
        for (MarriagePlan plan : marriagePlans) {
            BigDecimal currentSavings = plan.getCurrentSavings() != null ? plan.getCurrentSavings() : BigDecimal.ZERO;
            BigDecimal monthlyContribution = plan.getMonthlyContribution() != null ? plan.getMonthlyContribution() : BigDecimal.ZERO;
            plan.setCurrentSavings(currentSavings.add(monthlyContribution));
            marriagePlanRepository.save(plan);
        }
        System.out.println("Monthly contributions added to all plans.");
    }
}
