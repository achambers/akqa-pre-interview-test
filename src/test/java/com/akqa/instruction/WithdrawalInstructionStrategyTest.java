package com.akqa.instruction;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.akqa.TestHelper;
import com.akqa.atm.exception.AtmServiceException;
import com.akqa.atm.instruction.Instruction;
import com.akqa.atm.instruction.WithdrawalInstructionStrategy;
import com.akqa.atm.model.Account;
import com.akqa.atm.model.ErrorCode;
import com.akqa.atm.persistence.AtmDao;
import com.akqa.atm.service.AccountService;

public class WithdrawalInstructionStrategyTest extends TestHelper {

	private WithdrawalInstructionStrategy test;
	
	private AccountService mockAccountService;
	private AtmDao mockAtmDao;
	
	private static final int ACCOUNT_NUMBER = 123456789;
	private static final short PIN = 1234;
	
	@Before
	public void setUp() {
		mockAccountService = mock(AccountService.class);
		mockAtmDao = mock(AtmDao.class);
		
		test = new WithdrawalInstructionStrategy(mockAccountService, mockAtmDao);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenInstructionIsNull() throws Exception {
		test.invoke();
	}
	
	@Test(expected=AtmServiceException.class)
	public void shouldThrowAtmServiceExceptionWhenAtmDoesNotHaveEnoughMoney() throws Exception {
		Instruction instruction = instruction()
			.withAmount(new BigDecimal("10.00"))
			.build();
	
		test.setInstruction(instruction);
		
		when(mockAtmDao.getBalance()).thenReturn(new BigDecimal("5.00"));
		
		try {
			test.invoke();
		} catch (AtmServiceException e) {
			assertEquals(ErrorCode.ATM_ERR, e.getErrorCode());
			throw e;
		}
	}
	
	@Test
	public void shouldReturnAccount() throws Exception {
		Instruction instruction = instruction()
			.withAccountNumber(ACCOUNT_NUMBER)
			.withAmount(new BigDecimal("5.00"))
			.build();

		test.setInstruction(instruction);
		
		when(mockAtmDao.getBalance()).thenReturn(new BigDecimal("10.00"));
		
		Account expectedAccount = new Account(ACCOUNT_NUMBER, BigDecimal.ZERO, PIN);
		
		when(mockAccountService.withdraw(anyInt(), any(BigDecimal.class))).thenReturn(expectedAccount);
		
		Account returnedAccount = test.invoke();
		
		assertEquals(expectedAccount, returnedAccount);
	}
	
}
