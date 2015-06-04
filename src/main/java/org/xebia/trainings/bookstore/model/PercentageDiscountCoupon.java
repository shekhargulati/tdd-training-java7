package org.xebia.trainings.bookstore.model;

import java.util.Date;

import org.xebia.trainings.bookstore.coupon.InvalidDiscountCouponException;

public class PercentageDiscountCoupon extends DiscountCoupon {

	private final int percentageDiscount;

	public PercentageDiscountCoupon(int percentageDiscount, Date start, Date end) {
		super(start, end);
		if (percentageDiscount < 0) {
			throw new InvalidDiscountCouponException("'discount' can not be negative.");
		}
		this.percentageDiscount = percentageDiscount;
	}

	public int getPercentageDiscount() {
		return percentageDiscount;
	}

	@Override
	public int calculateDiscountAmount(int checkoutAmount) {
		return (checkoutAmount * this.getPercentageDiscount()) / 100;
	}

}
