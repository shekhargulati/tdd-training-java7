package org.xebia.trainings.bookstore.inventory.internal;

import java.util.HashMap;
import java.util.Map;

import org.xebia.trainings.bookstore.inventory.Inventory;
import org.xebia.trainings.bookstore.model.Book;

public class InMemoryInventory implements Inventory {

	private Map<String, Book> inventory = new HashMap<>();

	@Override
	public boolean exists(String title) {
		return inventory.containsKey(title);
	}

	@Override
	public void add(Book book) {
		inventory.put(book.getTitle(), book);
	}

	@Override
	public Book find(String title) {
		return inventory.get(title);
	}

	@Override
	public boolean hasEnoughCopies(String title, int quantity) {
		return inventory.get(title).getQuantity() >= quantity;
	}

}
