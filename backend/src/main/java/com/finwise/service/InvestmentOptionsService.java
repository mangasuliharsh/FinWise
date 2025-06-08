package com.finwise.service;

import com.finwise.entity.FamilyProfile;
import com.finwise.repository.FamilyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class InvestmentOptionsService {

    @Autowired
    private FamilyProfileRepository familyProfileRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String MAGIC_LOOPS_URL = "https://magicloops.dev/api/loop/521f8b36-fb78-4faa-9ad7-c7ede6d80d12/run";

    public Map<String, Object> getInvestmentOptionsForFamily(Long familyProfileId) {
        try {
            // Fetch family profile
            Optional<FamilyProfile> optionalProfile = familyProfileRepository.findById(familyProfileId);
            if (!optionalProfile.isPresent()) {
                throw new RuntimeException("Family profile not found");
            }
            FamilyProfile familyProfile = optionalProfile.get();

            // Prepare request map
            Map<String, Object> request = new HashMap<>();
            request.put("risk_tolerance", familyProfile.getRiskTolerance());
            request.put("monthly_salary", familyProfile.getMonthlyIncome());
            request.put("monthly_expenses", familyProfile.getMonthlyExpenses());

            // Call external API
            return callMagicLoopsAPI(request);

        } catch (Exception e) {
            return getDefaultInvestmentOptions();
        }
    }

    public Map<String, Object> generateInvestmentOptions(Map<String, Object> request) {
        try {
            return callMagicLoopsAPI(request);
        } catch (Exception e) {
            return getDefaultInvestmentOptions();
        }
    }

    private Map<String, Object> callMagicLoopsAPI(Map<String, Object> request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    MAGIC_LOOPS_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to call external investment API", e);
        }
    }




    private Map<String, Object> getDefaultInvestmentOptions() {
        List<Map<String, Object>> defaultOptions = new ArrayList<>();

        // HDFC Equity Fund
        Map<String, Object> option1 = new HashMap<>();
        option1.put("name", "HDFC Equity Fund");
        option1.put("type", "Equity Mutual Fund");
        option1.put("expected_annual_return", "10-14%");
        option1.put("risk_level", "medium");
        option1.put("logo_url", "https://www.hdfcfund.com/favicon.ico");
        option1.put("invest_link", "https://www.hdfcfund.com/mutual-funds");
        option1.put("minimum_investment", 5000);

        // Axis Bluechip Fund
        Map<String, Object> option2 = new HashMap<>();
        option2.put("name", "Axis Bluechip Fund");
        option2.put("type", "Equity Mutual Fund");
        option2.put("expected_annual_return", "10-12%");
        option2.put("risk_level", "medium");
        option2.put("logo_url", "https://www.axismf.com/favicon.ico");
        option2.put("invest_link", "https://www.axismf.com/mutual-fund");
        option2.put("minimum_investment", 5000);

        // ICICI Prudential Midcap Fund
        Map<String, Object> option3 = new HashMap<>();
        option3.put("name", "ICICI Prudential Midcap Fund");
        option3.put("type", "Equity Mutual Fund");
        option3.put("expected_annual_return", "11-15%");
        option3.put("risk_level", "medium");
        option3.put("logo_url", "https://www.icicipruamc.com/favicon.ico");
        option3.put("invest_link", "https://www.icicipruamc.com/");
        option3.put("minimum_investment", 5000);

        // Infosys Ltd.
        Map<String, Object> option4 = new HashMap<>();
        option4.put("name", "Infosys Ltd.");
        option4.put("type", "Large-Cap Stock");
        option4.put("expected_annual_return", "8-12% (historical average)");
        option4.put("risk_level", "medium");
        option4.put("logo_url", "https://www.infosys.com/favicon.ico");
        option4.put("invest_link", "https://groww.in/stocks/infosys-ltd");
        option4.put("minimum_investment", 2500);

        // TCS
        Map<String, Object> option5 = new HashMap<>();
        option5.put("name", "Tata Consultancy Services Ltd.");
        option5.put("type", "Large-Cap Stock");
        option5.put("expected_annual_return", "8-12% (historical average)");
        option5.put("risk_level", "medium");
        option5.put("logo_url", "https://www.tcs.com/favicon.ico");
        option5.put("invest_link", "https://groww.in/stocks/tata-consultancy-services-ltd");
        option5.put("minimum_investment", 2500);

        // Government Bond
        Map<String, Object> option6 = new HashMap<>();
        option6.put("name", "Government of India Savings Bond");
        option6.put("type", "Government Bond");
        option6.put("expected_annual_return", "7.75%");
        option6.put("risk_level", "low");
        option6.put("logo_url", "https://rbidocs.rbi.org.in/favicon.ico");
        option6.put("invest_link", "https://www.rbi.org.in/Scripts/BS_PressReleaseDisplay.aspx");
        option6.put("minimum_investment", 10000);

        // Nifty 50 Futures
        Map<String, Object> option7 = new HashMap<>();
        option7.put("name", "Nifty 50 Futures");
        option7.put("type", "Stock Option (Derivatives)");
        option7.put("expected_annual_return", "Varies (high risk, high reward)");
        option7.put("risk_level", "high");
        option7.put("logo_url", "https://www.nseindia.com/favicon.ico");
        option7.put("invest_link", "https://www.nseindia.com/options");
        option7.put("minimum_investment", 15000);

        // HDFC Nifty ETF
        Map<String, Object> option8 = new HashMap<>();
        option8.put("name", "HDFC Nifty ETF");
        option8.put("type", "ETF");
        option8.put("expected_annual_return", "8-10% (tracks Nifty 50)");
        option8.put("risk_level", "medium");
        option8.put("logo_url", "https://www.hdfcfund.com/favicon.ico");
        option8.put("invest_link", "https://www.hdfcfund.com/mutual-funds/etf");
        option8.put("minimum_investment", 1000);

        // Ethereum
        Map<String, Object> option9 = new HashMap<>();
        option9.put("name", "Ethereum (via WazirX)");
        option9.put("type", "Cryptocurrency");
        option9.put("expected_annual_return", "Highly volatile (historically high)");
        option9.put("risk_level", "high");
        option9.put("logo_url", "https://wazirx.com/favicon.ico");
        option9.put("invest_link", "https://wazirx.com/ethereum-trading");
        option9.put("minimum_investment", 1000);

        // Corporate Bond Fund
        Map<String, Object> option10 = new HashMap<>();
        option10.put("name", "ICICI Prudential Regular Savings Fund");
        option10.put("type", "Corporate Bond Mutual Fund");
        option10.put("expected_annual_return", "6-8%");
        option10.put("risk_level", "low");
        option10.put("logo_url", "https://www.icicipruamc.com/favicon.ico");
        option10.put("invest_link", "https://www.icicipruamc.com/");
        option10.put("minimum_investment", 5000);

        defaultOptions.add(option1);
        defaultOptions.add(option2);
        defaultOptions.add(option3);
        defaultOptions.add(option4);
        defaultOptions.add(option5);
        defaultOptions.add(option6);
        defaultOptions.add(option7);
        defaultOptions.add(option8);
        defaultOptions.add(option9);
        defaultOptions.add(option10);

        Map<String, Object> response = new HashMap<>();
        response.put("investment_options", defaultOptions);
        return response;
    }
}
