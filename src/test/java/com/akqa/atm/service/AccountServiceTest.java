package com.akqa.atm.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.akqa.atm.exception.AtmServiceException;
import com.akqa.atm.model.Account;
import com.akqa.atm.model.ErrorCode;
import com.akqa.atm.persistence.AccountDao;

public class AccountServiceTest {

	//I know this goes against the idea of well named variable but I like to use the convention of 
	//calling the class under test 'test'.
	private AccountService test;
	
	private AccountDao mockAcccountDao;
	
	private static final int ACCOUNT_NUMBER = 123456789; 
	
	private static final short PIN = 1234;
	
	private static final BigDecimal ACCOUNT_BALANCE = new BigDecimal("10.21");
	private static final BigDecimal ACCOUNT_BALANCE_GREATER_THAN_WITHDRAWAL = new BigDecimal("10.00");
	private static final BigDecimal ACCOUNT_BALANCE_LESS_THAN_WITHDRAWAL = new BigDecimal("3.00");
	private static final BigDecimal WITHDRAWAL_AMOUNT = new BigDecimal("5.00");
	
	@Before
	public void setUp() {
		mockAcccountDao = mock(AccountDao.class);
		
		test = new AccountService(mockAcccountDao);
	}
	
	@Test
	public void shouldReturnTheBalanceForTheSuppliedAccount() throws Exception {
		Account account = new Account(ACCOUNT_NUMBER, ACCOUNT_BALANCE, PIN);
		
		when(mockAcccountDao.get(anyInt())).thenReturn(account);
		
		Account retrievedAccount = test.getAccount(ACCOUNT_NUMBER);
		
		assertEquals(ACCOUNT_BALANCE, retrievedAccount.getBalance());
	}
	
	@Test(expected=AtmServiceException.class)
	public void shouldThrowAtmServiceExceptionWhenWithdrawalAmountIsGreaterThanAccountBalance() throws Exception {
		Account accountWithFundsLessThanWithdrawalAmount = new Account(ACCOUNT_NUMBER, ACCOUNT_BALANCE_LESS_THAN_WITHDRAWAL, PIN);
		
		when(mockAcccountDao.get(anyInt())).thenReturn(accountWithFundsLessThanWithdrawalAmount);
		
		try {
			test.withdraw(ACCOUNT_NUMBER, WITHDRAWAL_AMOUNT);
		} catch (AtmServiceException e) {
			assertEquals(ErrorCode.FUNDS_ERR, e.getErrorCode());
			throw e;
		}
		
	}
	
	@Test
	public void shouldReturnAccountWithUpdatedPositiveAccountBalanceAfterSuccessfulWithdrawal() throws Exception {
		Account accountWithFundsGreaterThanWithdrawalAmount = new Account(ACCOUNT_NUMBER, ACCOUNT_BALANCE_GREATER_THAN_WITHDRAWAL, PIN);
		
		when(mockAcccountDao.get(anyInt())).thenReturn(accountWithFundsGreaterThanWithdrawalAmount);
		
		Account updatedAccount = test.withdraw(ACCOUNT_NUMBER, WITHDRAWAL_AMOUNT);
		
		assertEquals(accountWithFundsGreaterThanWithdrawalAmount.getAccountNumber(), updatedAccount.getAccountNumber());
		assertEquals(accountWithFundsGreaterThanWithdrawalAmount.getBalance().subtract(WITHDRAWAL_AMOUNT), updatedAccount.getBalance());
	}
	
	@Test
	public void shouldReturnAccountWithUpdatedZeroAccountBalanceAfterSuccessfulWithdrawal() throws Exception {
		Account accountWithFundsEqualToWithdrawalAmount = new Account(ACCOUNT_NUMBER, WITHDRAWAL_AMOUNT, PIN);
		
		when(mockAcccountDao.get(anyInt())).thenReturn(accountWithFundsEqualToWithdrawalAmount);
		
		Account updatedAccount = test.withdraw(ACCOUNT_NUMBER, WITHDRAWAL_AMOUNT);
		
		assertEquals(accountWithFundsEqualToWithdrawalAmount.getAccountNumber(), updatedAccount.getAccountNumber());
		assertEquals(accountWithFundsEqualToWithdrawalAmount.getBalance().subtract(WITHDRAWAL_AMOUNT), updatedAccount.getBalance());
		assertEquals(updatedAccount.getBalance(), BigDecimal.ZERO.setScale(2));
	}
	
}
