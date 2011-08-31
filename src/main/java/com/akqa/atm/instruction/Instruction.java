package com.akqa.atm.instruction;

import java.math.BigDecimal;

public class Instruction {

	private final InstructionType type;
	private final int accountNumber;
	private final BigDecimal amount;
	
	public Instruction(InstructionType type, int accountNumber, BigDecimal amount) {
		this.type = type;
		this.accountNumber = accountNumber;
		this.amount = amount;
	}
	
	public InstructionType getType() {
		return type;
	}
	
	public int getAccountNumber() {
		return accountNumber;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
}
