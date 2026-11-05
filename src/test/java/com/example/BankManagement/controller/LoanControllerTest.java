package com.example.BankManagement.controller;

import com.example.BankManagement.dto.LoanEligibleRequestDTO;
import com.example.BankManagement.dto.LoanEligibleResponseDTO;
import com.example.BankManagement.dto.LoanRequestDTO;
import com.example.BankManagement.dto.LoanResponseDTO;
import com.example.BankManagement.enums.LoanPurpose;
import com.example.BankManagement.enums.LoanStatus;
import com.example.BankManagement.service.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void checkEligibility() throws Exception {
        LoanEligibleRequestDTO dto = new LoanEligibleRequestDTO();
        dto.setAccountNumber("123");
        dto.setIndianNational(true);
        dto.setAge(25);
        dto.setMonthlyIncome(40000);
        dto.setLoanAmount(BigDecimal.valueOf(100000));
        when(loanService.checkLoanEligibility(any())).thenReturn(new LoanEligibleResponseDTO("123", 1L, BigDecimal.valueOf(500000), com.example.BankManagement.enums.EligibilityStatus.ELIGIBLE));
        mockMvc.perform(post("/loan/eligibility").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
    }

    @Test
    void applyLoan() throws Exception {
        LoanRequestDTO dto = new LoanRequestDTO();
        dto.setAccountNumber("123");
        dto.setEligibilityId(1L);
        dto.setAmount(BigDecimal.valueOf(100000));
        dto.setTenureInMonths(12);
        dto.setLoanPurpose(LoanPurpose.PERSONAL_LOAN);
        when(loanService.applyLoan(any())).thenReturn(new LoanResponseDTO("LN1", BigDecimal.valueOf(100000), LoanStatus.APPLIED, "Loan Applied"));
        mockMvc.perform(post("/loan/applyLoan").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
    }

    @Test
    void getLoans() throws Exception {
        when(loanService.getLoans()).thenReturn(List.of());
        mockMvc.perform(get("/loan/all")).andExpect(status().isOk());
    }

    @Test
    void getLoan() throws Exception {
        when(loanService.getLoan("LN1")).thenReturn(new LoanResponseDTO("LN1", BigDecimal.valueOf(100000), LoanStatus.APPLIED, "Loan Applied"));
        mockMvc.perform(get("/loan/getLoan/{loanAccountNumber}", "LN1")).andExpect(status().isOk());
    }

    @Test
    void getLoansByAccount() throws Exception {
        when(loanService.getLoansByAccountNumber("123")).thenReturn(List.of());
        mockMvc.perform(get("/loan/getLoansByAccountNumber/{accountNumber}", "123")).andExpect(status().isOk());
    }

    @Test
    void processLoan() throws Exception {
        when(loanService.processLoan(any(), any(), any(), any(), any())).thenReturn(new LoanResponseDTO("LN1", BigDecimal.valueOf(90000), LoanStatus.APPROVED, "ok"));
        mockMvc.perform(put("/loan/process/{loanAccountNumber}", "LN1")
                .param("interest", "10.5")
                .param("amount", "90000")
                .param("remarks", "ok")
                .param("loanStatus", "APPROVED"))
                .andExpect(status().isOk());
    }
}