package com.example.BankManagement.repository;

import com.example.BankManagement.entity.Eligibility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EligibilityRepository extends JpaRepository<Eligibility, Long> {
}
