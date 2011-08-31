package com.akqa.instruction;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.akqa.TestHelper;
import com.akqa.atm.instruction.BalanceInstructionStrategy;
import com.akqa.atm.instruction.Instruction;
import com.akqa.atm.model.Account;
import com.akqa.atm.service.AccountService;

public class BalanceInstructionStrategyTest extends TestHelper {

	private BalanceInstructionStrategy test;
	
	private AccountService mockAccountService;
	
	private static final int ACCOUNT_NUMBER = 123456789;
	private static final short PIN = 1234;
	
	@Before
	public void setUp() {
		mockAccountService = mock(AccountService.class);
		
		test = new BalanceInstructionStrategy(mockAccountService);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenInstructionIsNull() throws Exception {
		test.invoke();
	}
	
	@Test
	public void shouldReturnAccount() throws Exception {
		Instruction instruction = instruction()
			.withAccountNumber(ACCOUNT_NUMBER)
			.build();
		
		test.setInstruction(instruction);
		
		Account expectedAccount = new Account(ACCOUNT_NUMBER, BigDecimal.ZERO, PIN);
		
		when(mockAccountService.getAccount(anyInt())).thenReturn(expectedAccount);
		
		Account returnedAccount = test.invoke();
		
		assertEquals(expectedAccount, returnedAccount);
	}
	
}
