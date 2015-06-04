package org.xebia.trainings.bookstore.model;

import java.util.Date;

import org.xebia.trainings.bookstore.coupon.InvalidDiscountCouponException;

public abstract class DiscountCoupon {

	private String couponCode;

	private final Date start;
	private final Date end;

	public DiscountCoupon(Date start, Date end) {
		if (start.after(end)) {
			throw new InvalidDiscountCouponException("'start' can not be greater than 'end'.");
		}
		this.start = start;
		this.end = end;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public boolean isExpired() {
		return end.before(new Date());
	}

	public abstract int calculateDiscountAmount(int checkoutAmount);

}
