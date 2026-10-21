package com.example.BankManagement.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountUpdateRequestDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String emailId;

    @NotBlank
    private String accountNumber;

    public AccountUpdateRequestDTO(String name, String mobileNumber, String accountNumber) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.accountNumber = accountNumber;
    }
}
