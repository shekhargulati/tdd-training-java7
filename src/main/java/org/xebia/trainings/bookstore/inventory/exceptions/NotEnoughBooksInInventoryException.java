package org.xebia.trainings.bookstore.inventory.exceptions;

public class NotEnoughBooksInInventoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotEnoughBooksInInventoryException(String tittle) {
		super(String.format("There are not enough copies of '%s' in the inventory.", tittle));
	}

}
