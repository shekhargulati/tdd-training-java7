package org.xebia.trainings.bookstore.coupon;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.xebia.trainings.bookstore.model.DiscountCoupon;

public class DiscountServiceTest {

	private DiscountService discountService = new InMemoryDiscountService();

	@Test
	public void createNewDiscountCouponWithUniqueCouponCode() {
		Date start = new Date();
		Date end = new Date(start.getTime() + 24L * 60 * 60 * 1000);
		String couponCode = discountService.create(new DiscountCoupon(20, start, end));
		assertNotNull(couponCode);
	}

	@Test(expected = InvalidDiscountCouponException.class)
	public void throwExceptionWhenDiscountCouponDoesNotExist() throws Exception {
		discountService.find("invalid_coupon_code");
	}

}
