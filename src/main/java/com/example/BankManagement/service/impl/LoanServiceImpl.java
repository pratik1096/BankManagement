package com.example.BankManagement.service.impl;

import com.example.BankManagement.dto.LoanEligibleRequestDTO;
import com.example.BankManagement.dto.LoanEligibleResponseDTO;
import com.example.BankManagement.dto.LoanRequestDTO;
import com.example.BankManagement.dto.LoanResponseDTO;
import com.example.BankManagement.entity.Account;
import com.example.BankManagement.entity.Eligibility;
import com.example.BankManagement.entity.Loan;
import com.example.BankManagement.enums.EligibilityStatus;
import com.example.BankManagement.enums.LoanStatus;
import com.example.BankManagement.repository.AccountRepository;
import com.example.BankManagement.repository.EligibilityRepository;
import com.example.BankManagement.repository.LoanRepository;
import com.example.BankManagement.service.LoanService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {

    private final BigDecimal limit = BigDecimal.valueOf(500000);

    AccountRepository accountRepository;
    EligibilityRepository eligibilityRepo;
    LoanRepository loanRepository;

    public LoanServiceImpl(AccountRepository accountRepository,
                           EligibilityRepository eligibilityRepo,
                           LoanRepository loanRepository){
        this.accountRepository = accountRepository;
        this.eligibilityRepo = eligibilityRepo;
        this.loanRepository = loanRepository;
    }

    @Override
    public LoanEligibleResponseDTO checkLoanEligibility(LoanEligibleRequestDTO loanEligibleRequestDTO){
        System.out.println("checkLoanEligibility");
        Optional<Account> account = accountRepository.findByAccountNumber(loanEligibleRequestDTO.getAccountNumber());
        LoanEligibleResponseDTO responseDTO;

        Eligibility eligibility;
        if(loanEligibleRequestDTO.isIndianNational() && loanEligibleRequestDTO.getAge()>=18 &&
                loanEligibleRequestDTO.getMonthlyIncome()>=30000 && loanEligibleRequestDTO.getLoanAmount().compareTo(limit)<0){
            eligibility = new Eligibility(loanEligibleRequestDTO.getAccountNumber(), limit,
                    EligibilityStatus.ELIGIBLE, LocalDateTime.now().plusHours(24), LocalDateTime.now(), false);
            System.out.println("eligibility id created : " + eligibility);
        }
        else {
            eligibility = new Eligibility(loanEligibleRequestDTO.getAccountNumber(), BigDecimal.ZERO,
                    EligibilityStatus.NOT_ELIGIBLE, LocalDateTime.now().plusHours(24), LocalDateTime.now(), false);
            System.out.println("eligibility id created : " + eligibility);
        }
        System.out.println("saving entity : " + eligibility);
        Eligibility savedEligibility = eligibilityRepo.save(eligibility);
        System.out.println("saved entity : " + savedEligibility);
        responseDTO = new LoanEligibleResponseDTO(savedEligibility.getAccountNumber(),savedEligibility.getId(),savedEligibility.getEligibleAmount(),
                savedEligibility.getStatus());
        System.out.println("responseDTO : " + responseDTO);
        return responseDTO;
    }

    @Override
    public LoanResponseDTO applyLoan(LoanRequestDTO loanRequestDTO){

        Eligibility eligibility = eligibilityRepo.findById(
                        loanRequestDTO.getEligibilityId())
                .orElseThrow(() -> new RuntimeException("Eligibility not found"));

        if(eligibility.getStatus().equals(EligibilityStatus.NOT_ELIGIBLE) || eligibility.getUsed() ||
                eligibility.getValidTill().isBefore(LocalDateTime.now()) || loanRequestDTO.getAmount().compareTo(eligibility.getEligibleAmount())>0){
                return new LoanResponseDTO(null, loanRequestDTO.getAmount(), LoanStatus.REJECTED,"NOT ELIGIBLE");
        }
        Loan loan = new Loan(generateLoanAccountNumber(),
                loanRequestDTO.getAccountNumber(),
                loanRequestDTO.getAmount(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                eligibility.getId(),
                LoanStatus.APPLIED,
                loanRequestDTO.getTenureInMonths(),
                loanRequestDTO.getLoanPurpose().getInterestRate(),
                calculateEmi(loanRequestDTO.getAmount(),loanRequestDTO.getLoanPurpose().getInterestRate(),loanRequestDTO.getTenureInMonths()),
                null,
                0,
                BigDecimal.ZERO,
                loanRequestDTO.getLoanPurpose(),
                "Loan Applied",
                LocalDateTime.now(),
                "SYSTEM",
                LocalDateTime.now(),
                "SYSTEM");

        Loan savedLoan = loanRepository.save(loan);
        eligibility.setUsed(Boolean.TRUE);
        eligibilityRepo.save(eligibility);

        return new LoanResponseDTO(savedLoan.getLoanAccountNumber(),savedLoan.getAmountSanctioned(),savedLoan.getLoanStatus(),loan.getRemarks());

    }

    private BigDecimal calculateEmi(BigDecimal principal,
                                    BigDecimal annualInterestRate,
                                    Integer tenureInMonths) {

        double p = principal.doubleValue();
        double monthlyRate = annualInterestRate.doubleValue() / (12 * 100);
        int n = tenureInMonths;

        double emi;

        if (monthlyRate == 0) {
            emi = p / n;
        } else {
            emi = (p * monthlyRate * Math.pow(1 + monthlyRate, n))
                    / (Math.pow(1 + monthlyRate, n) - 1);
        }

        return BigDecimal.valueOf(emi).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private String generateLoanAccountNumber() {

        String loanAccountNumber;

        do {
            loanAccountNumber = "LN"
                    + LocalDate.now().getYear()
                    + (100000 + (int) (Math.random() * 900000));
        } while (loanRepository.existsByLoanAccountNumber(loanAccountNumber));

        return loanAccountNumber;
    }

    @Override
    public LoanResponseDTO getLoan(String loanAccountNumber){
        Loan loan = loanRepository.findByLoanAccountNumber(loanAccountNumber);

        return new LoanResponseDTO(loan.getLoanAccountNumber(),
                loan.getAmountSanctioned(),loan.getLoanStatus(),loan.getRemarks());

    }

    @Override
    public List<LoanResponseDTO> getLoansByAccountNumber(String accountNumber){
        List<Loan> list = loanRepository.findAll(accountNumber);
        List<LoanResponseDTO> responseList = new ArrayList<>();
        for(Loan loan : list){
            responseList.add(new LoanResponseDTO(loan.getLoanAccountNumber(),
                    loan.getLoanAmount(),loan.getLoanStatus(),loan.getRemarks()));
        }
        return  responseList;
    }

    @Override
    public List<LoanResponseDTO> getLoans(){
        List<Loan> list =  loanRepository.findAll();

        List<LoanResponseDTO> responseList = new ArrayList<>();

        for(Loan loan : list){
            responseList.add(new LoanResponseDTO(loan.getLoanAccountNumber(),
                    loan.getLoanAmount(),loan.getLoanStatus(),loan.getRemarks()));
        }
        return responseList;
    }

    @Override
    public LoanResponseDTO processLoan(String loanAccountNumber, BigDecimal interest, BigDecimal amount,
                                       String remarks, LoanStatus loanStatus){

        Loan loan = loanRepository.findByLoanAccountNumber(loanAccountNumber);

        loan.setAmountSanctioned(amount);
        loan.setInterestRate(interest);
        loan.setRemarks(remarks);
        loan.setLoanStatus(loanStatus);
        loan.setModifiedOn(LocalDateTime.now());
        loan.setModifiedBy("SYSTEM");

        loanRepository.save(loan);

        return new LoanResponseDTO(loan.getLoanAccountNumber(),loan.getAmountSanctioned(),loan.getLoanStatus(),loan.getRemarks());

    }

}
