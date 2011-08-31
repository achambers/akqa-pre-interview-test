package com.akqa.atm.persistence;

import com.akqa.atm.model.Account;
import com.akqa.atm.model.Bank;

public class AccountDao {

	public Account get(int accountNumber) {
		Account account = Bank.getAccount(accountNumber);
		
		if(account == null) {
			throw new RuntimeException("Invalid account number");
		}
		
		return account;
	}

	public void updateAccount(Account account) {
		Bank.addAccount(account);
	}

}
