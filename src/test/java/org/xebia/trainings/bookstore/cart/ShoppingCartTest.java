package org.xebia.trainings.bookstore.cart;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xebia.trainings.bookstore.exceptions.EmptyShoppingCartException;
import org.xebia.trainings.bookstore.model.Book;

public class ShoppingCartTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	private ShoppingCart cart;
	private Book effectiveJava;
	private Book cleanCode;
	private Book headFirstJava;

	@Before
	public void createShoppingCart() {
		cart = new ShoppingCart();
	}

	@Before
	public void createBooks() {
		effectiveJava = new Book("Effective Java", 40);
		cleanCode = new Book("Clean Code", 50);
		headFirstJava = new Book("Head First Java", 30);
	}

	@Test
	public void emptyCartHasSizeZero() throws Exception {
		assertThat(cart.size(), is(equalTo(0)));
	}

	@Test
	public void cartWithSizeOneWhenOneBookIsAddedToTheShoppingCart() throws Exception {
		cart.add(effectiveJava);

		assertThat(cart.size(), is(equalTo(1)));
	}

	@Test
	public void cartWithSizeTwoWhenTwoBooksAreAddedToTheShoppingCart() throws Exception {
		cart.add(effectiveJava);
		cart.add(cleanCode);

		assertThat(cart.size(), is(equalTo(2)));
	}

	@Test
	public void cartItemsOrderedByInsertionWhenItemsAreAddedToCart() throws Exception {
		cart.add(effectiveJava);
		cart.add(cleanCode);
		cart.add(headFirstJava);

		assertThat(cart.items(), is(equalTo(asList(effectiveJava, cleanCode, headFirstJava))));
	}

	@Test
	public void cartAmountEqualsToSumOfAllBookPricesWhenCheckout() throws Exception {
		cart.add(effectiveJava);
		cart.add(cleanCode);
		cart.add(headFirstJava);

		int amount = cart.checkout();

		assertThat(amount, is(equalTo(120)));
	}

	@Test
	public void throwExceptionWhenCheckoutIsCalledOnEmptyCart() throws Exception {
		expectedException.expect(EmptyShoppingCartException.class);
		expectedException.expectMessage("You can't checkout an empty cart!!");

		cart.checkout();
	}

	@Test
	public void checkoutAmountEqualsToBookPriceWhenOneCopyOfBookAddedToCart() throws Exception {
		cart.add(effectiveJava, 1);

		int amount = cart.checkout();

		assertThat(amount, is(equalTo(40)));
	}

	@Test
	public void checkoutAmountEqualsToBookPriceTimeNumberOfCopiesOfBooksInCartWhenMultipleCopiesOfBookAddedToCart() throws Exception {
		cart.add(effectiveJava, 3);

		int amount = cart.checkout();

		assertThat(amount, is(equalTo(120)));
	}

	@Test
	public void cartSizeIsTotalNumberOfCopiesAddedToCart() throws Exception {
		cart.add(effectiveJava, 1);
		cart.add(effectiveJava, 1);

		int size = cart.size();

		assertThat(size, is(equalTo(2)));
	}
}
