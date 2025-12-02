package com.example.spring_vfdwebsite.services.bank;

import java.util.List;

import com.example.spring_vfdwebsite.dtos.bankDTOs.BankCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.bankDTOs.BankResponseDto;
import com.example.spring_vfdwebsite.dtos.bankDTOs.BankUpdateRequestDto;

public interface BankService {
    
    BankResponseDto createBank(BankCreateRequestDto dto);

    List<BankResponseDto> getAllBanks();

    BankResponseDto getBankById(Integer id);

    BankResponseDto updateBank(Integer id, BankUpdateRequestDto dto);

    void deleteBank(Integer id);
}
