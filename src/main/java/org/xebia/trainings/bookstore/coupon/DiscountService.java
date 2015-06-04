package org.xebia.trainings.bookstore.coupon;

import org.xebia.trainings.bookstore.model.DiscountCoupon;

public interface DiscountService {

	DiscountCoupon find(String couponCode) throws InvalidDiscountCouponException;

	String create(DiscountCoupon discountCoupon);


}
