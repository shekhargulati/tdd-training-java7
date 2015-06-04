package org.xebia.trainings.bookstore.coupon;

public class ExpiredDisountCouponException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExpiredDisountCouponException() {
		super("Sorry, the coupon code has expired.");
	}
}
