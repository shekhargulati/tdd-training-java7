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
		inventory.add(new Book("Effective Java", 40, 1));

		assertTrue(inventory.exists("Effective Java"));
	}

	@Test
	public void bookDoesNotExistWhenNotAddedToInventory() throws Exception {
		assertFalse(inventory.exists("Effective Java"));
	}

	@Test
	public void bookFoundWhenAddedtoInventory() throws Exception {
		inventory.add(new Book("Effective Java", 40, 1));
		assertThat(inventory.find("Effective Java"), equalTo(new Book("Effective Java", 40, 1)));
	}

	@Test
	public void trueWhenHasEnoughCopiesInInventory() throws Exception {
		inventory.add(new Book("Effective Java", 40, 2));
		inventory.add(new Book("OpenShift Cookbook", 44, 2));

		boolean enoughCopies = inventory.hasEnoughCopies("OpenShift Cookbook", 2);

		assertTrue(enoughCopies);
	}

	@Test
	public void falseWhenNotEnoughCopiesInInventory() throws Exception {
		inventory.add(new Book("Effective Java", 40, 2));
		inventory.add(new Book("OpenShift Cookbook", 44, 2));

		boolean enoughCopies = inventory.hasEnoughCopies("OpenShift Cookbook", 5);

		assertFalse(enoughCopies);
	}

}
