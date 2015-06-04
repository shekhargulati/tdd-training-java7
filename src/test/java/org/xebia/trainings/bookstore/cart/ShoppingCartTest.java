package org.xebia.trainings.bookstore.cart;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xebia.trainings.bookstore.cart.exceptions.EmptyShoppingCartException;
import org.xebia.trainings.bookstore.coupon.DiscountService;
import org.xebia.trainings.bookstore.coupon.ExpiredDisountCouponException;
import org.xebia.trainings.bookstore.coupon.InvalidDiscountCouponException;
import org.xebia.trainings.bookstore.inventory.Inventory;
import org.xebia.trainings.bookstore.inventory.exceptions.BookNotInInventoryException;
import org.xebia.trainings.bookstore.inventory.exceptions.NotEnoughBooksInInventoryException;
import org.xebia.trainings.bookstore.model.Book;
import org.xebia.trainings.bookstore.model.PercentageDiscountCoupon;

public class ShoppingCartTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private ShoppingCart cart;
	private String effectiveJava;
	private String cleanCode;
	private String headFirstJava;

	private Inventory inventory;

	private DiscountService discountService;

	@Before
	public void createShoppingCart() {
		inventory = mock(Inventory.class);
		discountService = mock(DiscountService.class);
		cart = new ShoppingCart(inventory,discountService);
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
		when(inventory.hasEnoughCopies("Effective Java", 1)).thenReturn(true);

		cart.add(effectiveJava);

		assertThat(cart.size(), is(equalTo(1)));

		verify(inventory, times(1)).exists("Effective Java");
		verify(inventory, times(1)).hasEnoughCopies("Effective Java", 1);
		verifyNoMoreInteractions(inventory);
	}

	@Test
	public void cartWithSizeTwoWhenTwoBooksAreAddedToTheShoppingCart() throws Exception {
		when(inventory.exists("Effective Java")).thenReturn(true);
		when(inventory.exists("Clean Code")).thenReturn(true);

		when(inventory.hasEnoughCopies("Effective Java", 1)).thenReturn(true);
		when(inventory.hasEnoughCopies("Clean Code", 1)).thenReturn(true);

		cart.add(effectiveJava);
		cart.add(cleanCode);

		assertThat(cart.size(), is(equalTo(2)));
		verify(inventory, times(2)).exists(anyString());
		verify(inventory, times(2)).hasEnoughCopies(anyString(), anyInt());
		verifyNoMoreInteractions(inventory);
	}

	@Test
	public void cartItemsOrderedByInsertionWhenItemsAreAddedToCart() throws Exception {

		when(inventory.exists("Effective Java")).thenReturn(true);
		when(inventory.exists("Clean Code")).thenReturn(true);
		when(inventory.exists("Head First Java")).thenReturn(true);

		when(inventory.hasEnoughCopies("Effective Java", 1)).thenReturn(true);
		when(inventory.hasEnoughCopies("Clean Code", 1)).thenReturn(true);
		when(inventory.hasEnoughCopies("Head First Java", 1)).thenReturn(true);

		cart.add(effectiveJava);
		cart.add(cleanCode);
		cart.add(headFirstJava);
		verify(inventory, times(3)).hasEnoughCopies(anyString(), anyInt());

		when(inventory.find(effectiveJava)).thenReturn(effectiveJavaBook());
		when(inventory.find(cleanCode)).thenReturn(cleanCodeBook());
		when(inventory.find(headFirstJava)).thenReturn(headFirstJavaBook());

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

		when(inventory.hasEnoughCopies("Effective Java", 1)).thenReturn(true);
		when(inventory.hasEnoughCopies("Clean Code", 1)).thenReturn(true);
		when(inventory.hasEnoughCopies("Head First Java", 1)).thenReturn(true);

		cart.add(effectiveJava);
		cart.add(cleanCode);
		cart.add(headFirstJava);
		verify(inventory, times(3)).hasEnoughCopies(anyString(), anyInt());

		when(inventory.find(effectiveJava)).thenReturn(effectiveJavaBook());
		when(inventory.find(cleanCode)).thenReturn(cleanCodeBook());
		when(inventory.find(headFirstJava)).thenReturn(headFirstJavaBook());

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
		when(inventory.hasEnoughCopies("Effective Java", 1)).thenReturn(true);

		cart.add(effectiveJava, 1);
		verify(inventory, times(1)).hasEnoughCopies(anyString(), anyInt());

		when(inventory.find(effectiveJava)).thenReturn(effectiveJavaBook());

		int amount = cart.checkout();

		assertThat(amount, is(equalTo(40)));
		verify(inventory, times(1)).exists(anyString());
		verify(inventory, times(1)).find(anyString());
		verifyNoMoreInteractions(inventory);
	}

	@Test
	public void checkoutAmountEqualsToBookPriceTimeNumberOfCopiesOfBooksInCartWhenMultipleCopiesOfBookAddedToCart() throws Exception {
		when(inventory.exists("Effective Java")).thenReturn(true);
		when(inventory.hasEnoughCopies("Effective Java", 3)).thenReturn(true);

		cart.add(effectiveJava, 3);
		verify(inventory, times(1)).hasEnoughCopies(anyString(), anyInt());

		when(inventory.find(effectiveJava)).thenReturn(effectiveJavaBook());

		int amount = cart.checkout();

		assertThat(amount, is(equalTo(120)));
		verify(inventory, times(1)).exists(anyString());
		verify(inventory, times(1)).find(anyString());
		verifyNoMoreInteractions(inventory);
	}

	@Test
	public void cartSizeIsTotalNumberOfCopiesAddedToCart() throws Exception {
		when(inventory.exists("Effective Java")).thenReturn(true);

		when(inventory.hasEnoughCopies("Effective Java", 1)).thenReturn(true);

		cart.add(effectiveJava, 1);
		cart.add(effectiveJava, 1);

		verify(inventory, times(2)).hasEnoughCopies(anyString(), anyInt());

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

	@Test
	public void throwExceptionWhenMoreItemsAreAddedToTheCartThanAvailableInInventory() throws Exception {
		when(inventory.exists("OpenShift Cookbook")).thenReturn(true);
		when(inventory.hasEnoughCopies("OpenShift Cookbook", 5)).thenReturn(false);

		expectedException.expect(NotEnoughBooksInInventoryException.class);
		expectedException.expectMessage(is(equalTo("There are not enough copies of 'OpenShift Cookbook' in the inventory.")));

		cart.add("OpenShift Cookbook", 5);

		verify(inventory, times(1)).exists("OpenShift Cookbook");
		verify(inventory, times(1)).hasEnoughCopies("OpenShift Cookbook", 5);
		verifyNoMoreInteractions(inventory);
	}

	@Test
	public void applyDiscountWhenAValidDisountCouponIsUsedDuringCheckout() throws Exception {
		when(inventory.exists(anyString())).thenReturn(true);
		when(inventory.hasEnoughCopies(effectiveJava, 3)).thenReturn(true);
		when(inventory.hasEnoughCopies(cleanCode, 2)).thenReturn(true);

		cart.add(effectiveJava, 3);
		cart.add(cleanCode, 2);

		verify(inventory, times(2)).exists(anyString());

		when(inventory.find(effectiveJava)).thenReturn(effectiveJavaBook());
		when(inventory.find(cleanCode)).thenReturn(cleanCodeBook());

		Date start = new Date();
		Date end = new Date(start.getTime() + 24L * 60 * 60 * 1000);
		String couponCode = "valid_discount_coupon";

		when(discountService.find(couponCode)).thenReturn(new PercentageDiscountCoupon(20, start, end));
		int cartAmount = cart.checkout(couponCode);


		assertThat(cartAmount, is(equalTo(192)));

		verify(inventory, times(2)).find(anyString());
		verify(inventory, times(1)).hasEnoughCopies(effectiveJava, 3);
		verify(inventory, times(1)).hasEnoughCopies(cleanCode, 2);
		verifyNoMoreInteractions(inventory);
	}


	@Test
	public void throwExceptionWhenExpiredDisountCouponIsUsedDuringCheckout() throws Exception {
		when(inventory.exists(anyString())).thenReturn(true);
		when(inventory.hasEnoughCopies(effectiveJava, 3)).thenReturn(true);
		when(inventory.hasEnoughCopies(cleanCode, 2)).thenReturn(true);

		cart.add(effectiveJava, 3);
		cart.add(cleanCode, 2);

		verify(inventory, times(2)).exists(anyString());

		when(inventory.find(effectiveJava)).thenReturn(effectiveJavaBook());
		when(inventory.find(cleanCode)).thenReturn(cleanCodeBook());

		long now = new Date().getTime();
		Date start = new Date(now - 2*24L * 60 * 60 * 1000);
		Date end = new Date(now - 24L * 60 * 60 * 1000);
		String couponCode = "expired_discount_coupon";

		when(discountService.find(couponCode)).thenReturn(new PercentageDiscountCoupon(20, start, end));
		expectedException.expect(isA(ExpiredDisountCouponException.class));
		expectedException.expectMessage(is(equalTo("Sorry, the coupon code has expired.")));
		cart.checkout("expired_discount_coupon");

	}

	@Test
	public void throwExceptionWhenCouponCodeDoesNotExist() throws Exception {
		when(inventory.exists(anyString())).thenReturn(true);
		when(inventory.hasEnoughCopies("OpenShift Cookbook", 3)).thenReturn(true);
		when(inventory.hasEnoughCopies("Effective Java", 2)).thenReturn(true);

		cart.add("OpenShift Cookbook", 3);
		cart.add("Effective Java", 2);

		verify(inventory, times(2)).exists(anyString());
		verify(inventory, times(1)).hasEnoughCopies("OpenShift Cookbook", 3);
		verify(inventory, times(1)).hasEnoughCopies("Effective Java", 2);
		verifyNoMoreInteractions(inventory);

		String couponCode = "invalid_discount_coupon";

		when(discountService.find(couponCode)).thenThrow(new InvalidDiscountCouponException("Sorry, the coupon code you entered does not exist."));

		expectedException.expect(isA(InvalidDiscountCouponException.class));
		expectedException.expectMessage(is(equalTo("Sorry, the coupon code you entered does not exist.")));

		cart.checkout(couponCode);
	}


	private Book headFirstJavaBook() {
		return new Book(headFirstJava, 30, 10);
	}

	private Book cleanCodeBook() {
		return new Book(cleanCode, 60, 10);
	}

	private Book effectiveJavaBook() {
		return new Book(effectiveJava, 40, 10);
	}

}
