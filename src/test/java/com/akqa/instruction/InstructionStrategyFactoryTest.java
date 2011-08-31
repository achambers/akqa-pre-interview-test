package com.akqa.instruction;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.akqa.TestHelper;
import com.akqa.atm.instruction.BalanceInstructionStrategy;
import com.akqa.atm.instruction.Instruction;
import com.akqa.atm.instruction.InstructionStrategy;
import com.akqa.atm.instruction.InstructionStrategyFactory;
import com.akqa.atm.instruction.InstructionType;
import com.akqa.atm.instruction.WithdrawalInstructionStrategy;

public class InstructionStrategyFactoryTest extends TestHelper {

	private InstructionStrategyFactory test;
	
	private InstructionStrategy balanceInstructionStrategy;
	private InstructionStrategy withdrawalInstructionStrategy;

	@Before
	public void setUp() {
		balanceInstructionStrategy = new BalanceInstructionStrategy(null);
		withdrawalInstructionStrategy = new WithdrawalInstructionStrategy(null, null);
		
		test = new InstructionStrategyFactory(balanceInstructionStrategy, withdrawalInstructionStrategy);
	}
	
	@Test
	public void shouldReturnBalanceInstructionStrategy() throws Exception {
		Instruction instruction = instruction()
			.withType(InstructionType.BALANCE_ENQUIRY)
			.build();
		
		InstructionStrategy instructionStrategy = test.get(instruction);
		
		assertEquals(balanceInstructionStrategy, instructionStrategy);
	}
	
	@Test
	public void shouldReturnWithdrawalInstructionStrategy() throws Exception {
		Instruction instruction = instruction()
			.withType(InstructionType.WITHDRAWAL)
			.build();
		
		InstructionStrategy instructionStrategy = test.get(instruction);
		
		assertEquals(withdrawalInstructionStrategy, instructionStrategy);
	}
	
}
