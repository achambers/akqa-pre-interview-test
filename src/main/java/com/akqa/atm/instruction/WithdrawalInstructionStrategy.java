package com.akqa.atm.instruction;

import com.akqa.atm.exception.AtmServiceException;
import com.akqa.atm.model.Account;
import com.akqa.atm.model.Atm;
import com.akqa.atm.model.ErrorCode;
import com.akqa.atm.persistence.AtmDao;
import com.akqa.atm.service.AccountService;

public class WithdrawalInstructionStrategy implements InstructionStrategy {

	private Instruction instruction;
	
	private final AccountService accountService;
	private final AtmDao atmDao;
	
	public WithdrawalInstructionStrategy(AccountService accountService, AtmDao atmDao) {
		this.accountService = accountService;
		this.atmDao = atmDao;
	}
	
	@Override
	public Account invoke() {
		
		if(instruction == null) {
			throw new IllegalArgumentException("Instruction not set");
		}
		
		//Synchronized because we need to make sure this all happens in an atomic transaction
		//We don't want 2 threads to withdraw money from an account before the ATM balance has been
		//updated
		synchronized (this) {
			if(atmDao.getBalance().compareTo(instruction.getAmount()) < 0) {
				throw new AtmServiceException(ErrorCode.ATM_ERR);
			}
			
			Account account = accountService.withdraw(instruction.getAccountNumber(), instruction.getAmount());
			
			Atm.subtract(instruction.getAmount());
			
			return account;
		}
	}

	@Override
	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

}
