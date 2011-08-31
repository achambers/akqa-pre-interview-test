package com.akqa.atm.persistence;

import java.math.BigDecimal;

import com.akqa.atm.model.Atm;

public class AtmDao {

	public BigDecimal getBalance() {
		return Atm.getBalance();
	}
	
}
