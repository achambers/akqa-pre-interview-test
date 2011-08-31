package com.akqa.atm.instruction;

import com.akqa.atm.model.Account;
import com.akqa.atm.service.AccountService;

public class BalanceInstructionStrategy implements InstructionStrategy {

	private Instruction instruction;
	
	private final AccountService accountService;
	
	public BalanceInstructionStrategy(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@Override
	public Account invoke() {
		if(instruction == null) {
			throw new IllegalArgumentException("Instruction not set");
		}
		
		return accountService.getAccount(instruction.getAccountNumber());
	}

	@Override
	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}
}
