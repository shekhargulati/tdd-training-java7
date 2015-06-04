package org.xebia.trainings.bookstore.cart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xebia.trainings.bookstore.cart.exceptions.EmptyShoppingCartException;
import org.xebia.trainings.bookstore.coupon.DiscountService;
import org.xebia.trainings.bookstore.coupon.ExpiredDisountCouponException;
import org.xebia.trainings.bookstore.inventory.Inventory;
import org.xebia.trainings.bookstore.inventory.exceptions.BookNotInInventoryException;
import org.xebia.trainings.bookstore.inventory.exceptions.NotEnoughBooksInInventoryException;
import org.xebia.trainings.bookstore.model.Book;
import org.xebia.trainings.bookstore.model.DiscountCoupon;

public class ShoppingCart {

	private Inventory inventory;
	private DiscountService discountService;

	private Map<String, Integer> itemsInCart = new LinkedHashMap<>();


	public ShoppingCart(Inventory inventory, DiscountService discountService) {
		this.inventory = inventory;
		this.discountService = discountService;
	}

	public void add(String book) {
		add(book, 1);
	}

	public void add(String book, int quantity) {
		if (!inventory.exists(book)) {
			throw new BookNotInInventoryException(book);
		}
		if (!inventory.hasEnoughCopies(book, quantity)) {
			throw new NotEnoughBooksInInventoryException(book);
		}
		if (itemsInCart.containsKey(book)) {
			itemsInCart.put(book, itemsInCart.get(book) + quantity);
		} else {
			itemsInCart.put(book, quantity);
		}
	}

	public int size() {
		int cartSize = 0;
		for (Integer quantity : itemsInCart.values()) {
			cartSize += quantity;
		}
		return cartSize;
	}

	public List<Book> items() {
		List<Book> items = new ArrayList<>();
		for (String title : itemsInCart.keySet()) {
			items.add(inventory.find(title));
		}
		return Collections.unmodifiableList(items);
	}

	private boolean isEmpty() {
		return size() == 0;
	}

	public int checkout(final String couponCode) {
		DiscountCoupon coupon = discountService.find(couponCode);
		if (coupon.isExpired()) {
			throw new ExpiredDisountCouponException();
		}
		int checkoutAmount = checkout();
		return checkoutAmount - coupon.calculateDiscountAmount(checkoutAmount);

	}

	public int checkout() {
		if (isEmpty()) {
			throw new EmptyShoppingCartException();
		}
		int checkoutAmount = 0;
		for (Entry<String, Integer> entry : itemsInCart.entrySet()) {
			checkoutAmount += inventory.find(entry.getKey()).getPrice() * entry.getValue();
		}
		return checkoutAmount;
	}
}
