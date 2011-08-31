package com.akqa.atm.instruction;

public enum InstructionType {

	BALANCE_ENQUIRY("B"), WITHDRAWAL("W");
	
	private final String description;
	
	private InstructionType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
}
