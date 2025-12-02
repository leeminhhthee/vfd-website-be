package com.example.spring_vfdwebsite.services.bank;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.annotations.LoggableAction;
import com.example.spring_vfdwebsite.dtos.bankDTOs.BankCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.bankDTOs.BankResponseDto;
import com.example.spring_vfdwebsite.dtos.bankDTOs.BankUpdateRequestDto;
import com.example.spring_vfdwebsite.entities.Bank;
import com.example.spring_vfdwebsite.events.bank.BankCreatedEvent;
import com.example.spring_vfdwebsite.events.bank.BankDeletedEvent;
import com.example.spring_vfdwebsite.events.bank.BankUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.BankJpaRepository;
import com.example.spring_vfdwebsite.utils.CloudinaryUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankJpaRepository bankRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CloudinaryUtils cloudinaryUtils;

    // Create new bank
    @Override
    @CacheEvict(value = "banks", allEntries = true)
    @LoggableAction(value = "CREATE", entity = "banks", description = "Create bank")
    public BankResponseDto createBank(BankCreateRequestDto dto) {
        Bank bank = Bank.builder()
                .fullName(dto.getFullName())
                .bankName(dto.getBankName())
                .accountNumber(dto.getAccountNumber())
                .branch(dto.getBranch())
                .imageUrl(dto.getImageUrl())
                .build();

        Bank savedBank = bankRepository.save(bank);

        eventPublisher.publishEvent(new BankCreatedEvent(bank.getId(), savedBank));

        return toDto(savedBank);
    }

    // Update bank
    @Override
    @Transactional
    @CachePut(value = "banks", key = "#p0")
    @CacheEvict(value = "banks", key = "'all'")
    @LoggableAction(value = "UPDATE", entity = "banks", description = "Update bank")
    public BankResponseDto updateBank(Integer id, BankUpdateRequestDto dto) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bank with id " + id + " not found"));

        if (dto.getFullName() != null) {
            bank.setFullName(dto.getFullName());
        }
        if (dto.getBankName() != null) {
            bank.setBankName(dto.getBankName());
        }
        if (dto.getAccountNumber() != null) {
            bank.setAccountNumber(dto.getAccountNumber());
        }
        if (dto.getBranch() != null) {
            bank.setBranch(dto.getBranch());
        }
        if (dto.getImageUrl() != null) {
            bank.setImageUrl(dto.getImageUrl());
        }

        Bank updatedBank = bankRepository.save(bank);

        eventPublisher.publishEvent(new BankUpdatedEvent(bank.getId(), updatedBank));

        return toDto(updatedBank);
    }

    // Delete bank
    @Override
    @CacheEvict(value = "banks", allEntries = true)
    @LoggableAction(value = "DELETE", entity = "banks", description = "Delete an existing bank")
    public void deleteBank(Integer id) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bank with id " + id + " not found"));

        if (bank.getImageUrl() != null) {
            cloudinaryUtils.deleteFile(bank.getImageUrl());
        }

        bankRepository.deleteById(id);

        eventPublisher.publishEvent(new BankDeletedEvent(id));
    }

    // Get all banks
    @Override
    @Cacheable(value = "banks", key = "'all'")
    public List<BankResponseDto> getAllBanks() {
        System.out.println("Fetching all banks from database");
        List<Bank> banks = bankRepository.findAll();
        return banks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Get bank by ID
    @Override
    @Cacheable(value = "banks", key = "#root.args[0]")
    public BankResponseDto getBankById(Integer id) {
        return bankRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Bank with id " + id + " not found"));
    }

    // ===================== Mapping to Dto =====================
    private BankResponseDto toDto(Bank bank) {
        return BankResponseDto.builder()
                .id(bank.getId())
                .fullName(bank.getFullName())
                .bankName(bank.getBankName())
                .accountNumber(bank.getAccountNumber())
                .branch(bank.getBranch())
                .imageUrl(bank.getImageUrl())
                .createdAt(bank.getCreatedAt())
                .updatedAt(bank.getUpdatedAt())
                .build();
    }
}
