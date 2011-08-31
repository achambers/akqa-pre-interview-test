package com.akqa.atm.instruction;

public class InstructionStrategyFactory {

	private final InstructionStrategy balanceInstructionStrategy;
	private final InstructionStrategy withdrawalInstructionStrategy;
	
	public InstructionStrategyFactory(InstructionStrategy balanceInstructionStrategy, InstructionStrategy withdrawalInstructionStrategy) {
		this.balanceInstructionStrategy = balanceInstructionStrategy;
		this.withdrawalInstructionStrategy = withdrawalInstructionStrategy;
	}
	
	public InstructionStrategy get(Instruction instruction) {
		switch (instruction.getType()) {
		case BALANCE_ENQUIRY:
			balanceInstructionStrategy.setInstruction(instruction);
			return balanceInstructionStrategy;
			
		case WITHDRAWAL:
			withdrawalInstructionStrategy.setInstruction(instruction);
			return withdrawalInstructionStrategy;

		default:
			throw new IllegalArgumentException("Unknown instruction type");
		}
	}
	
}
