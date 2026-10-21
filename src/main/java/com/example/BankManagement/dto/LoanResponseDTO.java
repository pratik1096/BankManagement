package com.example.BankManagement.dto;


import com.example.BankManagement.enums.LoanStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanResponseDTO {

    private String loanAccountNumber;

    private BigDecimal amount;

    private LoanStatus loanStatus;

    private String remark;

    public LoanResponseDTO(String loanAccountNumber, BigDecimal amount, LoanStatus loanStatus, String remark) {
        this.loanAccountNumber = loanAccountNumber;
        this.amount = amount;
        this.loanStatus = loanStatus;
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "loanAccountNumber : " +  loanAccountNumber + ", amount : " + amount + ", loanStatus : " + loanStatus + ", remark : " + remark;
    }

}
