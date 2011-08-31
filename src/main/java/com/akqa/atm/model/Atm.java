package com.akqa.atm.model;

import java.math.BigDecimal;

public class Atm {

	private static BigDecimal balance = BigDecimal.ZERO.setScale(2);
	
	public static void setBalance(BigDecimal balance) {
		Atm.balance = balance;
	}
	
	public static BigDecimal getBalance() {
		return balance;
	}
	
	public static void subtract(BigDecimal amount) {
		Atm.balance = Atm.balance.subtract(amount);
	}
	
}
