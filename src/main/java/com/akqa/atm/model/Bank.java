package com.akqa.atm.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Bank {

	private static Map<Integer, Account> accounts = new HashMap<Integer, Account>();
	
	public static void addAccount(Account account) {
		accounts.put(account.getAccountNumber(), account);
	}
	
	public static Account getAccount(int accountNumber) {
		return accounts.get(Integer.valueOf(accountNumber));
	}

	public static void createAccount(int accountNumber, BigDecimal initialBalance, BigDecimal overdraft, short pin) {
		Account account = new Account(accountNumber, initialBalance, overdraft, pin);
		
		addAccount(account);
	}
	
}
