package com.example.BankManagement.repository;

import com.example.BankManagement.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByEmailId(String emailId);

    boolean existsByPanCardNumber(String  panCardNumber);

    boolean existsByAdharCardNumber(String  adharCardNumber);

    Optional<Account> findByAccountNumber(String accountNumber);

    void deleteByAccountNumber(String accountNumber);
}
