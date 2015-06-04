package org.xebia.trainings.bookstore.model;

import java.util.Date;

public class DiscountCoupon {

	private int percentageDiscount;
	private Date start;
	private Date end;

	public DiscountCoupon(int percentageDiscount, Date start, Date end) {
		this.percentageDiscount = percentageDiscount;
		this.start = start;
		this.end = end;
	}

	public int getPercentageDiscount() {
		return percentageDiscount;
	}

	public Date getStart() {
		return new Date(start.getTime());
	}

	public Date getEnd() {
		return new Date(end.getTime());
	}

	public boolean isExpired() {
		return end.before(new Date());
	}

}
