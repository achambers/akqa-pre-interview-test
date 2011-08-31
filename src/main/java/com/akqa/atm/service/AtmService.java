package com.akqa.atm.service;

import java.util.ArrayList;
import java.util.List;

import com.akqa.atm.exception.AtmServiceException;
import com.akqa.atm.instruction.Instruction;
import com.akqa.atm.instruction.InstructionStrategy;
import com.akqa.atm.instruction.InstructionStrategyFactory;
import com.akqa.atm.model.Account;
import com.akqa.atm.model.Atm;
import com.akqa.atm.model.AtmContext;
import com.akqa.atm.model.Bank;
import com.akqa.atm.model.ErrorCode;
import com.akqa.atm.model.Statement;
import com.akqa.atm.model.UserSession;

public class AtmService {

	private final PinService pinService;
	private final InstructionStrategyFactory instructionStrategyFactory;
	
	public AtmService(PinService pinService, InstructionStrategyFactory instructionStrategyFactory) {
		this.pinService = pinService;
		this.instructionStrategyFactory = instructionStrategyFactory;
	}

	public List<Statement> processUserSessions(AtmContext context) {
		//TODO NOTE: Obviously this is crap.  In the real world, the front end would not be passing the ATM balance
		//to the back end.  It would be retrieved from the database.  But I'm developing this quickly and
		//within the bounds of the test requirements.  Hence this less than average way of storing the ATM balance
		Atm.setBalance(context.getBalance());
		
		List<Statement> statements = new ArrayList<Statement>();
		
		for(UserSession session : context.getUserSessions()) {
			
			//TODO NOTE: Again, this is due to the test requirements.  Obviously in the real world, this information
			//would already be saved in the database and the UI wouldn't be telling us
			Bank.createAccount(session.getAccountNumber(), session.getInitialBalance(), session.getOverdraft(), session.getActualPin());
			
			Statement statement = new Statement();
			
			if(!pinService.validatePin(session.getAccountNumber(), session.getEnteredPin())) {
				statement.addItem(ErrorCode.PIN_ERR.toString());
				statements.add(statement);
				continue;
			}
			
			for(Instruction instruction : session.getInstructions()) {
				InstructionStrategy strategy = instructionStrategyFactory.get(instruction);
				
				try {
					Account account = strategy.invoke();
					
					statement.addItem(account.getBalance().toString());
				} catch (AtmServiceException e) {
					statement.addItem(e.getErrorCode().toString());
				}
			}
			
			statements.add(statement);
		}
		
		return statements;
	}

}
