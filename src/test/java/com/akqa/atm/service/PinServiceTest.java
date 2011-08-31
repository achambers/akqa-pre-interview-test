package com.akqa.atm.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.akqa.atm.model.Account;

public class PinServiceTest {

	private PinService test;
	
	private AccountService mockAccountService;
	
	private static final int ACCOUNT_NUMBER = 123456789;
	
	private static final BigDecimal BALANCE = BigDecimal.ZERO.setScale(2);
	
	private static final short ACTUAL_PIN = 1234;
	private static final short CORRECT_ENTERED_PIN = 1234;
	private static final short INCORRECT_ENTERED_PIN = 6789;
	
	@Before
	public void setUp() {
		mockAccountService = mock(AccountService.class);
		
		test = new PinService(mockAccountService);
	}
	
	@Test
	public void shouldReturnTrueWhenTheEnteredPinIsValid() {
		Account account = new Account(ACCOUNT_NUMBER, BALANCE, ACTUAL_PIN);
		
		when(mockAccountService.getAccount(anyInt())).thenReturn(account);
		
		boolean validateResult = test.validatePin(ACCOUNT_NUMBER, CORRECT_ENTERED_PIN);
		
		assertTrue(validateResult);
	}
	
	@Test
	public void shouldReturnFalseWhenTheEnteredPinIsInvalid() throws Exception {
		Account account = new Account(ACCOUNT_NUMBER, BALANCE, ACTUAL_PIN);
		
		when(mockAccountService.getAccount(anyInt())).thenReturn(account);
		
		boolean validateResult = test.validatePin(ACCOUNT_NUMBER, INCORRECT_ENTERED_PIN);
		
		assertFalse(validateResult);
	}
	
}
