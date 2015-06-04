package org.xebia.trainings.bookstore.model;

import java.util.Date;

import org.xebia.trainings.bookstore.coupon.InvalidDiscountCouponException;

public class CashDiscountCoupon extends DiscountCoupon {

	private final int cashDiscount;

	public CashDiscountCoupon(int cashDiscount, Date start, Date end) {
		super(start, end);
		this.cashDiscount = cashDiscount;
	}

	public int getCashDiscount() {
		return cashDiscount;
	}

	@Override
	public int calculateDiscountAmount(int checkountAmount) {
		if (checkountAmount - cashDiscount < 0.6 * checkountAmount) {
			throw new InvalidDiscountCouponException("This coupon is not applicable for this checkout amount.");
		}
		return cashDiscount;
	}

}
