package com.akqa.atm.service;

import com.akqa.atm.model.Account;

public class PinService {

	private final AccountService accountService;
	
	public PinService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	//TODO NOTE: This is a pretty crap service.  Ideally, what should happen is that the 'UI' team should
	//be sending us the account number and the entered pin and we should be going to the database
	//and retrieving the correct pin for the account number and verifying the entered pin matches.
	//However, the below method satisfies the requirements of the test seeing the 'UI' team apparently
	//already know what the correct pin is
	public boolean validatePin(int accountNumber, short enteredPin) {
		Account account = accountService.getAccount(accountNumber);
		
		return account.getPin() == enteredPin;
	}
	
}
