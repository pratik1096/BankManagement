package com.example.BankManagement.dto;

import com.example.BankManagement.enums.EligibilityStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class LoanEligibleResponseDTO {

    private String accountNumber;
    private long eligibilityId;
    private BigDecimal eligibleAmount;
    private EligibilityStatus eligibilityStatus;

    public LoanEligibleResponseDTO(String accountNumber, long id,
                                   BigDecimal eligibleAmount,
                                   EligibilityStatus status) {
        this.accountNumber = accountNumber;
        this.eligibilityId = id;
        this.eligibleAmount = eligibleAmount;
        this.eligibilityStatus = status;
    }
}
