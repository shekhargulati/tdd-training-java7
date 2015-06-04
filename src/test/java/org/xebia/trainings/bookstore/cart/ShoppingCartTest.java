package org.xebia.trainings.bookstore.cart;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xebia.trainings.bookstore.exceptions.EmptyShoppingCartException;
import org.xebia.trainings.bookstore.model.Book;

public class ShoppingCartTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void emptyCartHasSizeZero() throws Exception {
		ShoppingCart cart = new ShoppingCart();

		assertThat(cart.size(), is(equalTo(0)));
	}

	@Test
	public void cartWithSizeOneWhenOneBookIsAddedToTheShoppingCart() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		cart.add(new Book("Effective Java", 40));

		assertThat(cart.size(), is(equalTo(1)));
	}

	@Test
	public void cartWithSizeTwoWhenTwoBooksAreAddedToTheShoppingCart() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		cart.add(new Book("Effective Java", 40));
		cart.add(new Book("Clean Code", 50));

		assertThat(cart.size(), is(equalTo(2)));
	}

	@Test
	public void cartItemsOrderedByInsertionWhenItemsAreAddedToCart() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		cart.add(new Book("Effective Java", 40));
		cart.add(new Book("Clean Code", 50));
		cart.add(new Book("Head First Java", 30));

		assertThat(cart.items(), is(equalTo(asList(new Book("Effective Java", 40), new Book("Clean Code", 50), new Book("Head First Java", 30)))));
	}

	@Test
	public void cartAmountEqualsToSumOfAllBookPricesWhenCheckout() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		cart.add(new Book("Effective Java", 40));
		cart.add(new Book("Clean Code", 50));
		cart.add(new Book("Head First Java", 30));

		int amount = cart.checkout();

		assertThat(amount, is(equalTo(120)));
	}

	@Test
	public void throwExceptionWhenCheckoutIsCalledOnEmptyCart() throws Exception {
		ShoppingCart cart = new ShoppingCart();

		expectedException.expect(EmptyShoppingCartException.class);
		expectedException.expectMessage("You can't checkout an empty cart!!");
		cart.checkout();
	}

}
