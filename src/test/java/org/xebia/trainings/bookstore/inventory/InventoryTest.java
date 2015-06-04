package org.xebia.trainings.bookstore.inventory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;
import org.xebia.trainings.bookstore.inventory.internal.InMemoryInventory;
import org.xebia.trainings.bookstore.model.Book;

public class InventoryTest {

	private Inventory inventory = new InMemoryInventory();
	@Test
	public void bookExistsWhenAddedToInventory() throws Exception {
		inventory.add(new Book("Effective Java", 40));

		assertTrue(inventory.exists("Effective Java"));
	}

	@Test
	public void bookDoesNotExistWhenNotAddedToInventory() throws Exception {
		assertFalse(inventory.exists("Effective Java"));
	}

	@Test
	public void bookFoundWhenAddedtoInventory() throws Exception {
		inventory.add(new Book("Effective Java", 40));
		assertThat(inventory.find("Effective Java"), equalTo(new Book("Effective Java", 40)));
	}
}
