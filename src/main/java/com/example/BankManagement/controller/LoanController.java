package com.example.BankManagement.controller;

import com.example.BankManagement.dto.LoanEligibleRequestDTO;
import com.example.BankManagement.dto.LoanEligibleResponseDTO;
import com.example.BankManagement.dto.LoanRequestDTO;
import com.example.BankManagement.dto.LoanResponseDTO;
import com.example.BankManagement.enums.LoanStatus;
import com.example.BankManagement.service.LoanService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/eligibility")
    public LoanEligibleResponseDTO checkLoanEligibility(@RequestBody LoanEligibleRequestDTO loanEligibleRequestDTO){
       return loanService.checkLoanEligibility(loanEligibleRequestDTO);
    }

    @PostMapping("/applyLoan")
    public LoanResponseDTO applyLoan(@RequestBody LoanRequestDTO loanRequestDTO){
        return loanService.applyLoan(loanRequestDTO);
    }

    @GetMapping("/all")
    public List<LoanResponseDTO> getLoans(){
        return loanService.getLoans();
    }

    @GetMapping("/getLoan/{loanAccountNumber}")
    public LoanResponseDTO getLoan(@PathVariable String loanAccountNumber){
        return loanService.getLoan(loanAccountNumber);
    }

    @GetMapping("getLoansByAccountNumber/{accountNumber}")
    public List<LoanResponseDTO> getLoansByAccountNumber(@PathVariable String accountNumber){
        return loanService.getLoansByAccountNumber(accountNumber);
    }

    @PutMapping("/process/{loanAccountNumber}")
    public LoanResponseDTO processLoan(@PathVariable String loanAccountNumber,
                                       @RequestParam BigDecimal interest,
                                       @RequestParam BigDecimal amount,
                                       @RequestParam String remarks,
                                       @RequestParam LoanStatus loanStatus){

        return loanService.processLoan(loanAccountNumber,interest,amount,remarks,loanStatus);

    }




}
