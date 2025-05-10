package com.bankw.ms_accounts.services;

import com.bankw.ms_accounts.entities.Account;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AccountService {

    boolean createAccount(String email);

    boolean updateBalance(String username, BigDecimal amount);

    List<Account> getAllAccounts();

    Map<String, Object> findAccount(String username);

    Map<String, Object> getAuthenticatedUserAccount(HttpServletRequest request);

}
