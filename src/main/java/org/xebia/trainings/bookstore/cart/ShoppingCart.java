package org.xebia.trainings.bookstore.cart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xebia.trainings.bookstore.exceptions.EmptyShoppingCartException;
import org.xebia.trainings.bookstore.model.Book;

public class ShoppingCart {

	private Map<Book, Integer> itemsInCart = new LinkedHashMap<>();

	public void add(Book book) {
		add(book, 1);
	}

	public void add(Book book, int quantity) {
		if (itemsInCart.containsKey(book)) {
			itemsInCart.put(book, itemsInCart.get(book) + quantity);
		} else {
			itemsInCart.put(book, quantity);
		}
	}

	public int checkout() {
		if (isEmpty()) {
			throw new EmptyShoppingCartException();
		}
		int checkoutAmount = 0;
		for (Entry<Book, Integer> entry : itemsInCart.entrySet()) {
			checkoutAmount += entry.getKey().getPrice() * entry.getValue();
		}
		return checkoutAmount;
	}

	private boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		int cartSize = 0;
		for (Integer quantity : itemsInCart.values()) {
			cartSize += quantity;
		}
		return cartSize;
	}

	public List<Book> items() {
		return Collections.unmodifiableList(new ArrayList<>(itemsInCart.keySet()));
	}

}
