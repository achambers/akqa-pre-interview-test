package com.akqa.atm.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AtmContext {

	private final BigDecimal balance;
	
	private final List<UserSession> userSessions = new ArrayList<UserSession>();
	
	public AtmContext(BigDecimal balance) {
		this.balance = balance;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	public List<UserSession> getUserSessions() {
		return userSessions;
	}
	
	public void addUserSession(UserSession session) {
		userSessions.add(session);
	}
	
}
