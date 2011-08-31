package com.akqa.atm.service;

import java.math.BigDecimal;

import com.akqa.atm.exception.AtmServiceException;
import com.akqa.atm.model.Account;
import com.akqa.atm.model.ErrorCode;
import com.akqa.atm.persistence.AccountDao;

//TODO Transactional
public class AccountService {

	private final AccountDao accountDao;
	
	public AccountService(AccountDao acccountDao) {
		accountDao = acccountDao;
	}

	public Account getAccount(int accountNumber) {
		return accountDao.get(accountNumber);
	}

	public Account withdraw(int accountNumber, BigDecimal withdrawalAmount) {
		Account account = accountDao.get(accountNumber);
		
		if(!account.hasSufficientFunds(withdrawalAmount)) {
			throw new AtmServiceException(ErrorCode.FUNDS_ERR);
		}
		
		Account updatedAccount = account.withdraw(withdrawalAmount);
		
		accountDao.updateAccount(updatedAccount);

		return updatedAccount;
	}

}
