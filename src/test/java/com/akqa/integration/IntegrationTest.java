package com.akqa.integration;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.akqa.TestHelper;
import com.akqa.atm.instruction.Instruction;
import com.akqa.atm.instruction.InstructionType;
import com.akqa.atm.model.AtmContext;
import com.akqa.atm.model.Statement;
import com.akqa.atm.model.UserSession;
import com.akqa.atm.service.AtmService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class IntegrationTest extends TestHelper {
	
	//TODO NOTE: The test requirements state that a third party team will provide a file in the format
	//specified.  I have written this application to run independent of the file.  It will basically run
	//on an AtmContext which includes UserSessions which in turn include one or more Instructions.
	
	//I envisage that some sort of client will be written to parse the file into the above AtmContext which
	//will then be passed to the code I have written.  I am not going to spend time write such a client as I
	//I don't see the value.  You should be able to see my coding style from what has been done.
	
	//Instead I am writing a unit test here that will create the AtmContext as it should look from the parsed file
	//and it will be passes to my application code and the output will be printed to the console.

	@Resource
	private AtmService test;
	
	@Test
	public void shouldProcessAtmContextAndOutputResultsAsPerInputDataFileReceivedByThirdParty() throws Exception {
		AtmContext context = new AtmContext(new BigDecimal("8000.00"));
		context.addUserSession(createFirstUserSession());
		context.addUserSession(createSecondUserSession());
		context.addUserSession(createThirdUserSession());
		
		printInputToConsole(context);
		
		List<Statement> statements = test.processUserSessions(context);
		
		printOutputToConsole(statements);
	}

	private void printOutputToConsole(List<Statement> statements) {
		System.out.println("Test Output");
		
		for (Statement statement : statements) {
			for (String item : statement.getItems()) {
				System.out.println(item);
			}
		}
		
		System.out.println("");
	}

	private void printInputToConsole(AtmContext context) {
		System.out.println("Test Input");
		System.out.println("");
		System.out.println(context.getBalance());
		System.out.println("");
		
		for (UserSession session : context.getUserSessions()) {
			System.out.println(session.getAccountNumber() + " " + session.getActualPin() + " " + session.getEnteredPin());
			System.out.println(session.getInitialBalance() + " " + session.getOverdraft());
			
			for (Instruction instruction : session.getInstructions()) {
				System.out.println(instruction.getType().getDescription() + " " + (instruction.getAmount() == null ? "" : instruction.getAmount()));
			}
			
			System.out.println("");
		}
		
	}

	private UserSession createFirstUserSession() {
		int accountNumber = 12345678;
		short actualPin = 1234;
		short enteredPin = 1234;
		BigDecimal balance = new BigDecimal("500.00");
		BigDecimal overdraft = new BigDecimal("100.00");
		
		Instruction instruction1 = instruction()
			.withAccountNumber(accountNumber)
			.withType(InstructionType.BALANCE_ENQUIRY)
			.build();
		
		Instruction instruction2 = instruction()
			.withAccountNumber(accountNumber)
			.withType(InstructionType.WITHDRAWAL)
			.withAmount(new BigDecimal("100.00"))
			.build();
		
		UserSession session = new UserSession(accountNumber, actualPin, enteredPin, balance, overdraft);
		session.addInstruction(instruction1);
		session.addInstruction(instruction2);
		
		return session;
	}
	
	private UserSession createSecondUserSession() {
		int accountNumber = 87654321;
		short actualPin = 4321;
		short enteredPin = 4321;
		BigDecimal balance = new BigDecimal("100.00");
		BigDecimal overdraft = new BigDecimal("0.00");
		
		Instruction instruction = instruction()
			.withAccountNumber(accountNumber)
			.withType(InstructionType.WITHDRAWAL)
			.withAmount(new BigDecimal("10.00"))
			.build();
		
		UserSession session = new UserSession(accountNumber, actualPin, enteredPin, balance, overdraft);
		session.addInstruction(instruction);
		
		return session;
	}
	
	private UserSession createThirdUserSession() {
		int accountNumber = 87654321;
		short actualPin = 4321;
		short enteredPin = 4321;
		BigDecimal balance = new BigDecimal("0.00");
		BigDecimal overdraft = new BigDecimal("0.00");

		Instruction instruction1 = instruction()
			.withAccountNumber(accountNumber)
			.withType(InstructionType.WITHDRAWAL)
			.withAmount(new BigDecimal("10.00"))
			.build();
		
		Instruction instruction2 = instruction()
			.withAccountNumber(accountNumber)
			.withType(InstructionType.BALANCE_ENQUIRY)
			.build();
		
		UserSession session = new UserSession(accountNumber, actualPin, enteredPin, balance, overdraft);
		session.addInstruction(instruction1);
		session.addInstruction(instruction2);
		
		return session;
	}
	
}
