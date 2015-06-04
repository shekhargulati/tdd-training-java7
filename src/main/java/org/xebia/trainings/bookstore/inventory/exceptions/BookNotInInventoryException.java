package org.xebia.trainings.bookstore.inventory.exceptions;

public class BookNotInInventoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final String SORRY_S_NOT_IN_STOCK = "Sorry, '%s' not in stock!!";

	public BookNotInInventoryException(String title) {
		super(String.format(SORRY_S_NOT_IN_STOCK, title));
	}

}
