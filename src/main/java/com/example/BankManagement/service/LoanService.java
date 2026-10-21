package com.example.BankManagement.service;

import com.example.BankManagement.dto.LoanEligibleRequestDTO;
import com.example.BankManagement.dto.LoanEligibleResponseDTO;
import com.example.BankManagement.dto.LoanRequestDTO;
import com.example.BankManagement.dto.LoanResponseDTO;
import com.example.BankManagement.enums.LoanStatus;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

public interface LoanService {

    LoanEligibleResponseDTO checkLoanEligibility(LoanEligibleRequestDTO loanEligibleRequestDTO);

    LoanResponseDTO applyLoan(LoanRequestDTO loanRequestDTO);

    LoanResponseDTO getLoan(String loanAccountNumber);

    List<LoanResponseDTO> getLoansByAccountNumber(String accountNumber);

    List<LoanResponseDTO> getLoans();

    LoanResponseDTO processLoan(String loanAccountNumber, BigDecimal interest, BigDecimal amount, String remarks, LoanStatus loanStatus);

}
