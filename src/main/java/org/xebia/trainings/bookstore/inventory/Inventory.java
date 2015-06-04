package org.xebia.trainings.bookstore.inventory;

import org.xebia.trainings.bookstore.model.Book;

public interface Inventory {

	boolean exists(String title);

	void add(Book book);

	Book find(String title);

	boolean hasEnoughCopies(String title, int quantity);

}
