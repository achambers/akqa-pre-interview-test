package com.akqa.atm.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.akqa.atm.instruction.Instruction;

public class UserSession {

	private final int accountNumber;
	
	//TODO Again, this is stupid how the front end knows what the actual pin is and that we're passing
	//in the current balance and overdraft.  The UI shouldn't know this
	private final short actualPin;
	private final short enteredPin;
	private final BigDecimal initialBalance;
	private final BigDecimal overdraft;
	
	private final List<Instruction> instructions;

	public UserSession(int accountNumber, short actualPin, short enteredPin, BigDecimal balance, BigDecimal overdraft) {
		this(accountNumber, actualPin, enteredPin, balance, overdraft, new ArrayList<Instruction>());
	}
	
	public UserSession(int accountNumber, short actualPin, short enteredPin, BigDecimal initialBalance, BigDecimal overdraft, List<Instruction> instructions) {
		this.accountNumber = accountNumber;
		this.actualPin = actualPin;
		this.enteredPin = enteredPin;
		this.initialBalance = initialBalance;
		this.overdraft = overdraft;
		this.instructions = instructions;
	}
	
	public int getAccountNumber() {
		return accountNumber;
	}
	
	public short getActualPin() {
		return actualPin;
	}
	
	public short getEnteredPin() {
		return enteredPin;
	}
	
	public BigDecimal getInitialBalance() {
		return initialBalance;
	}
	
	public BigDecimal getOverdraft() {
		return overdraft;
	}
	
	public List<Instruction> getInstructions() {
		return instructions;
	}
	
	public void addInstruction(Instruction instruction) {
		instructions.add(instruction);
	}
}
