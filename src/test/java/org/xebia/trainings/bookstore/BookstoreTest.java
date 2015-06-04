package org.xebia.trainings.bookstore;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xebia.trainings.bookstore.cart.ShoppingCart;
import org.xebia.trainings.bookstore.model.Book;

public class BookstoreTest {
	
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private ShoppingCart cart = new ShoppingCart();
	
	@Test
	public void projectInitialSetupShouldWork() {
		assertTrue("project dependency should be met.", true);
	}
	
	/*
	 * As a customer
	 * I want the ability to add one or more books to the shopping cart
	 * So that I can checkout them at their actual base price.
	 */

	@Test
	public void givenCartHasOneBook_WhenCustomerCheckoutTheCart_ThenCheckoutAmountShouldBeEqualToBookActualPrice() throws Exception {
		cart.add(new Book("Effective Java", 40));

		int amount = cart.checkout();

		assertThat(amount, is(equalTo(40)));
	}

	@Test
	public void givenCartHasThreeBooks_WhenCustomerCheckoutTheCart_ThenCheckoutAmountIsEqualToSumOfAllBookPrices() throws Exception {
		cart.add(new Book("Effective Java", 40));
		cart.add(new Book("Clean Code", 60));
		cart.add(new Book("Head First Java", 30));

		int amount = cart.checkout();

		assertThat(amount, is(equalTo(130)));
	}
	
	@Test
	public void whenUserCheckoutABookThatDoesNotExistInInventory_ThenExceptionIsThrown() throws Exception {
		expectedException.expectMessage(equalTo("You can't checkout an empty cart!!"));
		
		cart.checkout();
	}
	
}
