package com.akqa.atm.model;

import java.math.BigDecimal;

public class Account {

	private final int accountNumber;
	private final BigDecimal balance;
	private final BigDecimal overdraft;
	private final short pin;
	
	public Account(int accountNumber, BigDecimal accountBalance, short pin) {
		this(accountNumber, accountBalance, BigDecimal.ZERO.setScale(2), pin);
	}
	
	public Account(int accountNumber, BigDecimal accountBalance, BigDecimal overdraft, short pin) {
		this.accountNumber = accountNumber;
		balance = accountBalance;
		this.overdraft = overdraft;
		this.pin = pin;
	}

	public int getAccountNumber() {
		return accountNumber;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	public BigDecimal getOverdraft() {
		return overdraft;
	}
	
	public short getPin() {
		return pin;
	}

	public boolean hasSufficientFunds(BigDecimal withdrawalAmount) {
		if(getEffectiveBalance().compareTo(withdrawalAmount) == -1)
			return false;
		
		return true;
	}
	
	public Account withdraw(BigDecimal withdrawalAmount) {
		BigDecimal newBalance = balance.subtract(withdrawalAmount);
		
		return new Account(accountNumber, newBalance, overdraft, pin);
	}
	
	private BigDecimal getEffectiveBalance() {
		return balance.add(overdraft);
	}
	
}
