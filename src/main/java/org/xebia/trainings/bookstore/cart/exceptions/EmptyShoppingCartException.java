package org.xebia.trainings.bookstore.cart.exceptions;

public class EmptyShoppingCartException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmptyShoppingCartException() {
		super("You can't checkout an empty cart!!");
	}

}
