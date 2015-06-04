package org.xebia.trainings.bookstore.cart;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xebia.trainings.bookstore.cart.exceptions.EmptyShoppingCartException;
import org.xebia.trainings.bookstore.inventory.Inventory;
import org.xebia.trainings.bookstore.inventory.exceptions.BookNotInInventoryException;
import org.xebia.trainings.bookstore.model.Book;

public class ShoppingCartTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private ShoppingCart cart;
	private String effectiveJava;
	private String cleanCode;
	private String headFirstJava;

	private Inventory inventory;

	@Before
	public void createShoppingCart() {
		inventory = mock(Inventory.class);
		cart = new ShoppingCart(inventory);
	}

	@Before
	public void createBooks() {
		effectiveJava = "Effective Java";
		cleanCode = "Clean Code";
		headFirstJava = "Head First Java";
	}

	@Test
	public void emptyCartHasSizeZero() throws Exception {
		assertThat(cart.size(), is(equalTo(0)));
	}

	@Test
	public void cartWithSizeOneWhenOneBookIsAddedToTheShoppingCart() throws Exception {
		when(inventory.exists("Effective Java")).thenReturn(true);

		cart.add(effectiveJava);

		assertThat(cart.size(), is(equalTo(1)));

		verify(inventory, times(1)).exists("Effective Java");
		verifyNoMoreInteractions(inventory);
	}

	@Test
	public void cartWithSizeTwoWhenTwoBooksAreAddedToTheShoppingCart() throws Exception {
		when(inventory.exists("Effective Java")).thenReturn(true);
		when(inventory.exists("Clean Code")).thenReturn(true);

		cart.add(effectiveJava);
		cart.add(cleanCode);

		assertThat(cart.size(), is(equalTo(2)));
		verify(inventory, times(2)).exists(anyString());
		verifyNoMoreInteractions(inventory);
	}

	@Test
	public void cartItemsOrderedByInsertionWhenItemsAreAddedToCart() throws Exception {

		when(inventory.exists("Effective Java")).thenReturn(true);
		when(inventory.exists("Clean Code")).thenReturn(true);
		when(inventory.exists("Head First Java")).thenReturn(true);

		cart.add(effectiveJava);
		cart.add(cleanCode);
		cart.add(headFirstJava);

		when(inventory.find(effectiveJava)).thenReturn(new Book(effectiveJava, 40));
		when(inventory.find(cleanCode)).thenReturn(new Book(cleanCode, 60));
		when(inventory.find(headFirstJava)).thenReturn(new Book(headFirstJava, 30));
		
		List<Book> items = cart.items();
		
		assertEquals(effectiveJava, items.get(0).getTitle());
		assertEquals(cleanCode, items.get(1).getTitle());
		assertEquals(headFirstJava, items.get(2).getTitle());

		verify(inventory, times(3)).exists(anyString());
		verify(inventory, times(3)).find(anyString());
		verifyNoMoreInteractions(inventory);
	}

	@Test
	public void cartAmountEqualsToSumOfAllBookPricesWhenCheckout() throws Exception {
		when(inventory.exists("Effective Java")).thenReturn(true);
		when(inventory.exists("Clean Code")).thenReturn(true);
		when(inventory.exists("Head First Java")).thenReturn(true);

		cart.add(effectiveJava);
		cart.add(cleanCode);
		cart.add(headFirstJava);

		when(inventory.find(effectiveJava)).thenReturn(new Book(effectiveJava, 40));
		when(inventory.find(cleanCode)).thenReturn(new Book(cleanCode, 60));
		when(inventory.find(headFirstJava)).thenReturn(new Book(headFirstJava, 30));

		int amount = cart.checkout();

		assertThat(amount, is(equalTo(130)));

		verify(inventory, times(3)).exists(anyString());
		verify(inventory, times(3)).find(anyString());
		verifyNoMoreInteractions(inventory);
	}

	@Test
	public void throwExceptionWhenCheckoutIsCalledOnEmptyCart() throws Exception {
		expectedException.expect(EmptyShoppingCartException.class);
		expectedException.expectMessage("You can't checkout an empty cart!!");

		cart.checkout();
	}

	@Test
	public void checkoutAmountEqualsToBookPriceWhenOneCopyOfBookAddedToCart() throws Exception {
		when(inventory.exists("Effective Java")).thenReturn(true);

		cart.add(effectiveJava, 1);
		
		when(inventory.find(effectiveJava)).thenReturn(new Book(effectiveJava, 40));

		int amount = cart.checkout();

		assertThat(amount, is(equalTo(40)));
		verify(inventory, times(1)).exists(anyString());
		verify(inventory, times(1)).find(anyString());
		verifyNoMoreInteractions(inventory);
	}

	@Test
	public void checkoutAmountEqualsToBookPriceTimeNumberOfCopiesOfBooksInCartWhenMultipleCopiesOfBookAddedToCart() throws Exception {
		when(inventory.exists("Effective Java")).thenReturn(true);

		cart.add(effectiveJava, 3);

		when(inventory.find(effectiveJava)).thenReturn(new Book(effectiveJava, 40));
		
		int amount = cart.checkout();

		assertThat(amount, is(equalTo(120)));
		verify(inventory, times(1)).exists(anyString());
		verify(inventory, times(1)).find(anyString());
		verifyNoMoreInteractions(inventory);
	}

	@Test
	public void cartSizeIsTotalNumberOfCopiesAddedToCart() throws Exception {
		when(inventory.exists("Effective Java")).thenReturn(true);

		cart.add(effectiveJava, 1);
		cart.add(effectiveJava, 1);

		int size = cart.size();

		assertThat(size, is(equalTo(2)));

		verify(inventory, times(2)).exists(anyString());
		verifyNoMoreInteractions(inventory);
	}

	@Test
	public void throwExceptionWhenBookAddedToTheCartDoesNotExistInInventory() throws Exception {

		when(inventory.exists("Effective Java")).thenReturn(false);

		try {
			cart.add(effectiveJava);
			fail("add should throw exception as 'Effective Java' not in inventory");
		} catch (BookNotInInventoryException e) {
			assertThat(e.getMessage(), is(equalTo("Sorry, 'Effective Java' not in stock!!")));
		}

		verify(inventory, times(1)).exists("Effective Java");
		verifyNoMoreInteractions(inventory);
	}
}
