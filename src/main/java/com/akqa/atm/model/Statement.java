package com.akqa.atm.model;

import java.util.ArrayList;
import java.util.List;

public class Statement {

	private final List<String> items = new ArrayList<String>();
	
	public void addItem(String item) {
		items.add(item);
	}
	
	public List<String> getItems() {
		return items;
	}
	
}
