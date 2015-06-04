package org.xebia.trainings.bookstore.cart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xebia.trainings.bookstore.exceptions.EmptyShoppingCartException;
import org.xebia.trainings.bookstore.model.Book;

public class ShoppingCart {

	private List<Book> itemsInCart = new ArrayList<Book>();

	public void add(Book book) {
		itemsInCart.add(book);
	}

	public int checkout() {
		if (isEmpty()) {
			throw new EmptyShoppingCartException();
		}
		int checkoutAmount = 0;
		for (Book book : itemsInCart) {
			checkoutAmount += book.getPrice();
		}
		return checkoutAmount;
	}

	private boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return itemsInCart.size();
	}

	public List<Book> items() {
		return Collections.unmodifiableList(itemsInCart);
	}

}
