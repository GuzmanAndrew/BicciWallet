package com.bankw.ms_accounts.services.impl;

import com.bankw.ms_accounts.config.JwtUtil;
import com.bankw.ms_accounts.entities.Account;
import com.bankw.ms_accounts.repositories.AccountRepository;
import com.bankw.ms_accounts.services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  public boolean createAccount(String username) {
    Optional<Account> existingAccount = accountRepository.findByUsername(username);

    if (existingAccount.isPresent()) {
      System.out.println("El usuario ya tiene una cuenta.");
      return false;
    }

    Account newAccount = new Account();
    newAccount.setUsername(username);
    newAccount.setAccountType("B2C");
    newAccount.setBalance(new BigDecimal("50000.00"));

    accountRepository.save(newAccount);
    System.out.println("Cuenta creada para: " + username);
    return true;
  }

  @Override
  public boolean updateBalance(String username, BigDecimal amount) {

    Optional<Account> optionalAccount = accountRepository.findByUsername(username);

    if (optionalAccount.isEmpty()) {
      return false;
    }

    Account account = optionalAccount.get();
    BigDecimal newBalance = account.getBalance().add(amount);

    account.setBalance(newBalance);
    accountRepository.save(account);

    System.out.println("Saldo actualizado: " + username + " -> " + newBalance); // ⚠️ Logging inseguro
    return true;
  }

}
