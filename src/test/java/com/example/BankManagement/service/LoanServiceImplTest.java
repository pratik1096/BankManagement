package com.example.BankManagement.service;

import com.example.BankManagement.dto.LoanEligibleRequestDTO;
import com.example.BankManagement.dto.LoanEligibleResponseDTO;
import com.example.BankManagement.dto.LoanRequestDTO;
import com.example.BankManagement.dto.LoanResponseDTO;
import com.example.BankManagement.entity.Eligibility;
import com.example.BankManagement.entity.Loan;
import com.example.BankManagement.enums.EligibilityStatus;
import com.example.BankManagement.enums.LoanPurpose;
import com.example.BankManagement.enums.LoanStatus;
import com.example.BankManagement.exception.ResourceNotFoundException;
import com.example.BankManagement.repository.AccountRepository;
import com.example.BankManagement.repository.EligibilityRepository;
import com.example.BankManagement.repository.LoanRepository;
import com.example.BankManagement.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LoanServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private EligibilityRepository eligibilityRepo;
    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    @BeforeEach
    void setUp() {
        when(eligibilityRepo.save(any())).thenAnswer(inv -> {
            Eligibility e = inv.getArgument(0);
            e.setId(1L);
            return e;
        });
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(loanRepository.existsByLoanAccountNumber(any())).thenReturn(false);
    }

    @Test
    void checkLoanEligibility_eligible() {
        LoanEligibleRequestDTO dto = new LoanEligibleRequestDTO();
        dto.setAccountNumber("123");
        dto.setIndianNational(true);
        dto.setAge(25);
        dto.setMonthlyIncome(40000);
        dto.setLoanAmount(BigDecimal.valueOf(100000));

        LoanEligibleResponseDTO res = loanService.checkLoanEligibility(dto);
        assertEquals(EligibilityStatus.ELIGIBLE, res.getEligibilityStatus());
        assertEquals(BigDecimal.valueOf(500000), res.getEligibleAmount());
    }

    @Test
    void checkLoanEligibility_notEligible() {
        LoanEligibleRequestDTO dto = new LoanEligibleRequestDTO();
        dto.setAccountNumber("123");
        dto.setIndianNational(false);
        dto.setAge(25);
        dto.setMonthlyIncome(40000);
        dto.setLoanAmount(BigDecimal.valueOf(100000));

        LoanEligibleResponseDTO res = loanService.checkLoanEligibility(dto);
        assertEquals(EligibilityStatus.NOT_ELIGIBLE, res.getEligibilityStatus());
        assertEquals(BigDecimal.ZERO, res.getEligibleAmount());
    }

    @Test
    void applyLoan_eligibilityNotFound() {
        LoanRequestDTO dto = new LoanRequestDTO();
        dto.setAccountNumber("123");
        dto.setEligibilityId(99);
        dto.setAmount(BigDecimal.valueOf(100000));
        dto.setTenureInMonths(12);
        dto.setLoanPurpose(LoanPurpose.PERSONAL_LOAN);

        when(eligibilityRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> loanService.applyLoan(dto));
    }

    @Test
    void applyLoan_notEligible() {
        Eligibility eligibility = new Eligibility("123", BigDecimal.valueOf(500000), EligibilityStatus.NOT_ELIGIBLE, LocalDateTime.now().plusHours(1), LocalDateTime.now(), false);
        eligibility.setId(1L);
        LoanRequestDTO dto = new LoanRequestDTO();
        dto.setAccountNumber("123");
        dto.setEligibilityId(1L);
        dto.setAmount(BigDecimal.valueOf(100000));
        dto.setTenureInMonths(12);
        dto.setLoanPurpose(LoanPurpose.PERSONAL_LOAN);

        when(eligibilityRepo.findById(1L)).thenReturn(Optional.of(eligibility));
        LoanResponseDTO res = loanService.applyLoan(dto);
        assertEquals(LoanStatus.REJECTED, res.getLoanStatus());
    }

    @Test
    void applyLoan_used() {
        Eligibility eligibility = new Eligibility("123", BigDecimal.valueOf(500000), EligibilityStatus.ELIGIBLE, LocalDateTime.now().plusHours(1), LocalDateTime.now(), true);
        eligibility.setId(1L);
        LoanRequestDTO dto = new LoanRequestDTO();
        dto.setAccountNumber("123");
        dto.setEligibilityId(1L);
        dto.setAmount(BigDecimal.valueOf(100000));
        dto.setTenureInMonths(12);
        dto.setLoanPurpose(LoanPurpose.PERSONAL_LOAN);

        when(eligibilityRepo.findById(1L)).thenReturn(Optional.of(eligibility));
        LoanResponseDTO res = loanService.applyLoan(dto);
        assertEquals(LoanStatus.REJECTED, res.getLoanStatus());
    }

    @Test
    void applyLoan_expired() {
        Eligibility eligibility = new Eligibility("123", BigDecimal.valueOf(500000), EligibilityStatus.ELIGIBLE, LocalDateTime.now().minusHours(1), LocalDateTime.now(), false);
        eligibility.setId(1L);
        LoanRequestDTO dto = new LoanRequestDTO();
        dto.setAccountNumber("123");
        dto.setEligibilityId(1L);
        dto.setAmount(BigDecimal.valueOf(100000));
        dto.setTenureInMonths(12);
        dto.setLoanPurpose(LoanPurpose.PERSONAL_LOAN);

        when(eligibilityRepo.findById(1L)).thenReturn(Optional.of(eligibility));
        LoanResponseDTO res = loanService.applyLoan(dto);
        assertEquals(LoanStatus.REJECTED, res.getLoanStatus());
    }

    @Test
    void applyLoan_amountExceeds() {
        Eligibility eligibility = new Eligibility("123", BigDecimal.valueOf(500000), EligibilityStatus.ELIGIBLE, LocalDateTime.now().plusHours(1), LocalDateTime.now(), false);
        eligibility.setId(1L);
        LoanRequestDTO dto = new LoanRequestDTO();
        dto.setAccountNumber("123");
        dto.setEligibilityId(1L);
        dto.setAmount(BigDecimal.valueOf(600000));
        dto.setTenureInMonths(12);
        dto.setLoanPurpose(LoanPurpose.PERSONAL_LOAN);

        when(eligibilityRepo.findById(1L)).thenReturn(Optional.of(eligibility));
        LoanResponseDTO res = loanService.applyLoan(dto);
        assertEquals(LoanStatus.REJECTED, res.getLoanStatus());
    }

    @Test
    void applyLoan_success() {
        Eligibility eligibility = new Eligibility("123", BigDecimal.valueOf(500000), EligibilityStatus.ELIGIBLE, LocalDateTime.now().plusHours(1), LocalDateTime.now(), false);
        eligibility.setId(1L);
        LoanRequestDTO dto = new LoanRequestDTO();
        dto.setAccountNumber("123");
        dto.setEligibilityId(1L);
        dto.setAmount(BigDecimal.valueOf(100000));
        dto.setTenureInMonths(12);
        dto.setLoanPurpose(LoanPurpose.PERSONAL_LOAN);

        when(eligibilityRepo.findById(1L)).thenReturn(Optional.of(eligibility));
        LoanResponseDTO res = loanService.applyLoan(dto);
        assertEquals(LoanStatus.APPLIED, res.getLoanStatus());
        verify(eligibilityRepo).save(eligibility);
        assertTrue(eligibility.getUsed());
    }

    @Test
    void getLoan_success() {
        Loan loan = new Loan("LN20261", "123", BigDecimal.valueOf(100000), BigDecimal.ZERO, BigDecimal.ZERO, 1L, LoanStatus.APPLIED, 12, BigDecimal.valueOf(12.75), BigDecimal.valueOf(9000), null, 0, BigDecimal.ZERO, LoanPurpose.PERSONAL_LOAN, "Loan Applied", LocalDateTime.now(), "SYSTEM", LocalDateTime.now(), "SYSTEM");
        when(loanRepository.findByLoanAccountNumber("LN20261")).thenReturn(loan);
        LoanResponseDTO res = loanService.getLoan("LN20261");
        assertEquals("LN20261", res.getLoanAccountNumber());
    }

    @Test
    void getLoan_notFound() {
        when(loanRepository.findByLoanAccountNumber("X")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> loanService.getLoan("X"));
    }

    @Test
    void getLoansByAccountNumber_success() {
        Loan loan = new Loan("LN20261", "123", BigDecimal.valueOf(100000), BigDecimal.ZERO, BigDecimal.ZERO, 1L, LoanStatus.APPLIED, 12, BigDecimal.valueOf(12.75), BigDecimal.valueOf(9000), null, 0, BigDecimal.ZERO, LoanPurpose.PERSONAL_LOAN, "Loan Applied", LocalDateTime.now(), "SYSTEM", LocalDateTime.now(), "SYSTEM");
        when(loanRepository.findByAccountNumber("123")).thenReturn(List.of(loan));
        List<LoanResponseDTO> list = loanService.getLoansByAccountNumber("123");
        assertEquals(1, list.size());
    }

    @Test
    void getLoans_success() {
        Loan loan = new Loan("LN20261", "123", BigDecimal.valueOf(100000), BigDecimal.ZERO, BigDecimal.ZERO, 1L, LoanStatus.APPLIED, 12, BigDecimal.valueOf(12.75), BigDecimal.valueOf(9000), null, 0, BigDecimal.ZERO, LoanPurpose.PERSONAL_LOAN, "Loan Applied", LocalDateTime.now(), "SYSTEM", LocalDateTime.now(), "SYSTEM");
        when(loanRepository.findAll()).thenReturn(List.of(loan));
        List<LoanResponseDTO> list = loanService.getLoans();
        assertEquals(1, list.size());
    }

    @Test
    void processLoan_success() {
        Loan loan = new Loan("LN20261", "123", BigDecimal.valueOf(100000), BigDecimal.ZERO, BigDecimal.ZERO, 1L, LoanStatus.APPLIED, 12, BigDecimal.valueOf(12.75), BigDecimal.valueOf(9000), null, 0, BigDecimal.ZERO, LoanPurpose.PERSONAL_LOAN, "Loan Applied", LocalDateTime.now(), "SYSTEM", LocalDateTime.now(), "SYSTEM");
        when(loanRepository.findByLoanAccountNumber("LN20261")).thenReturn(loan);
        LoanResponseDTO res = loanService.processLoan("LN20261", BigDecimal.valueOf(10.5), BigDecimal.valueOf(90000), "ok", LoanStatus.APPROVED);
        assertEquals(LoanStatus.APPROVED, res.getLoanStatus());
        verify(loanRepository).save(loan);
    }

    @Test
    void processLoan_notFound() {
        when(loanRepository.findByLoanAccountNumber("X")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> loanService.processLoan("X", BigDecimal.ONE, BigDecimal.ONE, "r", LoanStatus.APPROVED));
    }
}