package com.akqa.atm.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyShort;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.akqa.TestHelper;
import com.akqa.atm.exception.AtmServiceException;
import com.akqa.atm.instruction.Instruction;
import com.akqa.atm.instruction.InstructionStrategy;
import com.akqa.atm.instruction.InstructionStrategyFactory;
import com.akqa.atm.instruction.InstructionType;
import com.akqa.atm.instruction.WithdrawalInstructionStrategy;
import com.akqa.atm.model.Account;
import com.akqa.atm.model.AtmContext;
import com.akqa.atm.model.ErrorCode;
import com.akqa.atm.model.Statement;
import com.akqa.atm.model.UserSession;
import com.akqa.atm.persistence.AtmDao;

public class AtmServiceTest extends TestHelper {

	private AtmService test;
	
	private PinService mockPinService;
	private AccountService mockAccountService;
	private AtmDao mockAtmDao;
	
	private InstructionStrategyFactory instructionStrategyFactory;

	private InstructionStrategy balanceInstructionStrategy;
	private InstructionStrategy withdrawalInstructionStrategy;
	
	private static final int ACCOUNT_NUMBER = 123456789;
	
	private static final BigDecimal ATM_BALANCE = new BigDecimal("10.00");
	
	private static final short PIN = 1234;
	
	@Before
	public void setUp() {
		mockPinService = mock(PinService.class);
		mockAccountService = mock(AccountService.class);
		mockAtmDao = mock(AtmDao.class);
		
		withdrawalInstructionStrategy = new WithdrawalInstructionStrategy(mockAccountService, mockAtmDao);
		instructionStrategyFactory = new InstructionStrategyFactory(balanceInstructionStrategy, withdrawalInstructionStrategy);
		
		test = new AtmService(mockPinService, instructionStrategyFactory);
	}
	
	@Test
	public void atmShouldNotDispenseMoreMoneyThanItHolds() throws Exception {
		Instruction instruction = instruction()
			.withType(InstructionType.WITHDRAWAL)
			.withAccountNumber(ACCOUNT_NUMBER)
			.withAmount(new BigDecimal("15.00"))
			.build();
		
		UserSession session = createUserSession();
		session.addInstruction(instruction);
		
		AtmContext context = new AtmContext(ATM_BALANCE);
		context.addUserSession(session);
		
		when(mockPinService.validatePin(anyInt(), anyShort())).thenReturn(true);
		when(mockAtmDao.getBalance()).thenReturn(ATM_BALANCE);
		
		List<Statement> statements = test.processUserSessions(context);
		
		assertEquals(1, statements.size());
		assertEquals(1, statements.get(0).getItems().size());
		assertEquals(ErrorCode.ATM_ERR.toString(), statements.get(0).getItems().get(0));
	}
	
	@Test
	public void customerCannotWithdrawMoreFundsThanTheyHaveAccessTo() throws Exception {
		Instruction instruction = instruction()
			.withType(InstructionType.WITHDRAWAL)
			.withAccountNumber(ACCOUNT_NUMBER)
			.withAmount(new BigDecimal("5.00"))
			.build();
		
		UserSession session = createUserSession();
		session.addInstruction(instruction);
		
		AtmContext context = new AtmContext(ATM_BALANCE);
		context.addUserSession(session);
		
		when(mockPinService.validatePin(anyInt(), anyShort())).thenReturn(true);
		when(mockAccountService.withdraw(anyInt(), any(BigDecimal.class))).thenThrow(new AtmServiceException(ErrorCode.FUNDS_ERR));
		when(mockAtmDao.getBalance()).thenReturn(ATM_BALANCE);
		
		List<Statement> statements = test.processUserSessions(context);
		
		assertEquals(1, statements.size());
		assertEquals(1, statements.get(0).getItems().size());
		assertEquals(ErrorCode.FUNDS_ERR.toString(), statements.get(0).getItems().get(0));
	}
	
	@Test
	public void atmShouldNotDispenseFundsIfPinIsIncorrect() throws Exception {
		UserSession session = createUserSession();
		
		AtmContext context = new AtmContext(ATM_BALANCE);
		context.addUserSession(session);
		
		when(mockPinService.validatePin(anyInt(), anyShort())).thenReturn(false);
		
		List<Statement> statements = test.processUserSessions(context);
		
		assertEquals(1, statements.size());
		assertEquals(1, statements.get(0).getItems().size());
		assertEquals(ErrorCode.PIN_ERR.toString(), statements.get(0).getItems().get(0));
	}
	
	@Test
	public void atmShouldNotExposeBalanceIfPinIsIncorrect() throws Exception {
		Instruction instruction = instruction()
			.withType(InstructionType.BALANCE_ENQUIRY)
			.withAccountNumber(ACCOUNT_NUMBER)
			.build();
		
		UserSession session = createUserSession();
		session.addInstruction(instruction);
		
		AtmContext context = new AtmContext(ATM_BALANCE);
		context.addUserSession(session);
		
		when(mockPinService.validatePin(anyInt(), anyShort())).thenReturn(false);
		
		List<Statement> statements = test.processUserSessions(context);
		
		assertEquals(1, statements.size());
		assertEquals(1, statements.get(0).getItems().size());
		assertEquals(ErrorCode.PIN_ERR.toString(), statements.get(0).getItems().get(0));
	}
	
	@Test
	public void atmShouldOnlyDispenseTheExactAmountsRequested() throws Exception {
		Instruction instruction = instruction()
			.withType(InstructionType.WITHDRAWAL)
			.withAccountNumber(ACCOUNT_NUMBER)
			.withAmount(new BigDecimal("5.01"))
			.build();
		
		UserSession session = createUserSession();
		session.addInstruction(instruction);
		
		AtmContext context = new AtmContext(ATM_BALANCE);
		context.addUserSession(session);
		
		when(mockPinService.validatePin(anyInt(), anyShort())).thenReturn(true);
		
		//I know this is stupid to be mocking out the return value here but the withdrawal code is tested in
		//the AccountServiceTest.  What we are really testing here is that the AtmService returns the correct 
		//balance statement
		when(mockAccountService.withdraw(anyInt(), any(BigDecimal.class))).thenReturn(new Account(ACCOUNT_NUMBER, new BigDecimal("4.99"), PIN));
		when(mockAtmDao.getBalance()).thenReturn(ATM_BALANCE);
		
		List<Statement> statements = test.processUserSessions(context);
		
		assertEquals(1, statements.size());
		assertEquals(1, statements.get(0).getItems().size());
		assertEquals("4.99", statements.get(0).getItems().get(0));
	}
	
	private UserSession createUserSession() {
		return userSession()
			.withAccountNumber(ACCOUNT_NUMBER)
			.withActualPin(PIN)
			.withEnteredPin(PIN)
			.build();
	}
	
}
