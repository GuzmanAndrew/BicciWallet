package com.bankw.ms_accounts.services;

import java.math.BigDecimal;

public interface AccountService {

    boolean createAccount(String email);

    boolean updateBalance(String username, BigDecimal amount);
}
