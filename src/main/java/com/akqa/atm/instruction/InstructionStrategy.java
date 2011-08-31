package com.akqa.atm.instruction;

import com.akqa.atm.model.Account;

public interface InstructionStrategy {

	void setInstruction(Instruction instruction);
	Account invoke();
	
}
