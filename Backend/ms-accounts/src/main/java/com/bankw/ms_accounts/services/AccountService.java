package com.bankw.ms_accounts.services;

public interface AccountService {

    boolean createAccount(String email);

    boolean updateBalance(String username, Double amount);
}
