package com.bankw.ms_accounts.services.impl;

import com.bankw.ms_accounts.config.JwtUtil;
import com.bankw.ms_accounts.entities.Account;
import com.bankw.ms_accounts.repositories.AccountRepository;
import com.bankw.ms_accounts.services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final JwtUtil jwtUtil;

  public AccountServiceImpl(AccountRepository accountRepository, JwtUtil jwtUtil) {
    this.accountRepository = accountRepository;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public boolean createAccount(String username) {
    if (accountRepository.findByUsername(username).isPresent()) {
      return false;
    }

    Account newAccount = new Account();
    newAccount.setUsername(username);
    newAccount.setAccountType("B2C");
    newAccount.setBalance(new BigDecimal("50000.00"));

    accountRepository.save(newAccount);
    return true;
  }

  @Override
  public boolean updateBalance(String username, BigDecimal amount) {
    Optional<Account> optionalAccount = accountRepository.findByUsername(username);
    if (optionalAccount.isEmpty()) return false;

    Account account = optionalAccount.get();
    BigDecimal newBalance = account.getBalance().add(amount);

    account.setBalance(newBalance);
    accountRepository.save(account);

    return true;
  }

  @Override
  public List<Account> getAllAccounts() {
    return accountRepository.findAll();
  }

  @Override
  public Map<String, Object> findAccount(String username) {
    Optional<Account> accountOpt = accountRepository.findByUsername(username);
    if (accountOpt.isEmpty()) {
      throw new RuntimeException("Cuenta no encontrada");
    }
    Account account = accountOpt.get();

    Map<String, Object> response = new HashMap<>();
    response.put("username", account.getUsername());
    response.put("balance", account.getBalance());
    response.put("accountType", account.getAccountType());

    return response;
  }

  @Override
  public Map<String, Object> getAuthenticatedUserAccount(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new RuntimeException("Token no proporcionado o inválido");
    }

    String token = authHeader.substring(7);
    String username = jwtUtil.extractUsername(token);

    Optional<Account> accountOpt = accountRepository.findByUsername(username);
    if (accountOpt.isEmpty()) {
      throw new RuntimeException("Cuenta no encontrada");
    }

    Account account = accountOpt.get();
    Map<String, Object> response = new HashMap<>();
    response.put("username", account.getUsername());
    response.put("balance", account.getBalance());
    response.put("accountType", account.getAccountType());

    return response;
  }
}
