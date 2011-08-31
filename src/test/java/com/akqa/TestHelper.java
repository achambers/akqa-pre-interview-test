package com.akqa;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Ignore;

import com.akqa.atm.instruction.Instruction;
import com.akqa.atm.instruction.InstructionType;
import com.akqa.atm.model.UserSession;

@Ignore
public class TestHelper {

	public InstructionBuilder instruction() {
		return new InstructionBuilder();
	}
	
	public UserSessionBuilder userSession() {
		return new UserSessionBuilder();
	}
	
	protected class UserSessionBuilder {
		private int accountNumber;
		private BigDecimal initialBalance = BigDecimal.ZERO.setScale(2);
		private BigDecimal overdraft = BigDecimal.ZERO.setScale(2);
		private short actualPin;
		private short enteredPin;
		private List<Instruction> instructions;
		
		public UserSessionBuilder withActualPin(short pin) {
			this.actualPin = pin;
			
			return this;
		}
		
		public UserSessionBuilder withEnteredPin(short pin) {
			this.enteredPin = pin;
			
			return this;
		}
		
		public UserSessionBuilder withInstructions(List<Instruction> instructions) {
			this.instructions = instructions;
			
			return this;
		}
		
		public UserSessionBuilder withAccountNumber(int accountNumber) {
			this.accountNumber = accountNumber;
			
			return this;
		}
		
		public UserSession build() {
			if(instructions == null) {
				return new UserSession(accountNumber, actualPin, enteredPin, initialBalance, overdraft);
			}
			
			return new UserSession(accountNumber, actualPin, enteredPin, initialBalance, overdraft, instructions);
		}
	}
	
	protected class InstructionBuilder {
		
		private InstructionType type;
		private int accountNumber;
		private BigDecimal amount;
		
		public InstructionBuilder withType(InstructionType type) {
			this.type = type;
			
			return this;
		}
		
		public InstructionBuilder withAccountNumber(int accountNumber) {
			this.accountNumber = accountNumber;
			
			return this;
		}
		
		public InstructionBuilder withAmount(BigDecimal amount) {
			this.amount = amount;
			
			return this;
		}
		
		public Instruction build() {
			return new Instruction(type, accountNumber, amount);
		}
		
	}
	
}
