package com.example.BankManagement.repository;

import com.example.BankManagement.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    boolean existsByLoanAccountNumber(String loanAccountNumber);

    Loan findByLoanAccountNumber(String loanAccountNumber);

    List<Loan> findAll(String accountNumber);
}
