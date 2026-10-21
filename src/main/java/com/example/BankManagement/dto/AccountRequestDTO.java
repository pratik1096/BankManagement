package com.example.BankManagement.dto;

import com.example.BankManagement.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class AccountRequestDTO {

    @NotBlank
    private String accountHolderName;

    @NotNull
    private AccountType accountType;

    @NotBlank
    private String emailId;

    @NotBlank
    private String panCardNumber;

    @NotBlank
    private String adharCardNumber;

    @NotBlank
    private String mobileNumber;

    public AccountRequestDTO(String accountHolderName,
                             AccountType accountType,
                             String emailId,
                             String panCardNumber,
                             String adharCardNumber,
                             String mobileNumber) {
        this.accountHolderName = accountHolderName;
        this.accountType = accountType;
        this.emailId = emailId;
        this.panCardNumber=panCardNumber;
        this.adharCardNumber=adharCardNumber;
        this.mobileNumber=mobileNumber;
    }
}
