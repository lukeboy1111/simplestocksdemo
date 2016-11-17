package com.lukecampbell.simplestocks.bo;

import java.util.Calendar;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.ZoneOffset;
import java.util.Date;

import com.lukecampbell.simplestocks.enums.BuyOrSell;
import com.lukecampbell.simplestocks.enums.BuyOrSell.BuySellEnum;

public class StockTradeContainer {
	private StockSymbol stockTraded;
	private double priceTraded;
	private long quantity;
	private Date dateOfTrade;
	private String soldOrBought;

	private BuySellEnum buyOrSell;

	public StockTradeContainer() {

	}

	/*
	 * public StockTradeContainer(StockSymbol stockTraded, double priceTraded,
	 * long quantity, BuySellEnum buyOrSell, LocalDateTime dateOfTrade) {
	 * super(); this.stockTraded = stockTraded; this.priceTraded = priceTraded;
	 * this.quantity = quantity; this.dateOfTrade = dateOfTrade; this.buyOrSell
	 * = buyOrSell; }
	 */

	public StockTradeContainer(StockSymbol stockTraded, double priceTraded, long quantity, BuySellEnum buyOrSell, Date dateOfTrade) {
		super();
		this.stockTraded = stockTraded;
		this.priceTraded = priceTraded;
		this.quantity = quantity;
		this.dateOfTrade = dateOfTrade;
		this.buyOrSell = buyOrSell;
	}

	public StockTradeContainer(StockSymbol stockTraded, double priceTraded, long quantity, BuySellEnum buyOrSell) {
		super();
		this.stockTraded = stockTraded;
		this.priceTraded = priceTraded;
		this.quantity = quantity;
		Date rightNow = new Date();
		this.dateOfTrade = rightNow;
		this.buyOrSell = buyOrSell;
	}

	public BuySellEnum getBuyOrSell() {
		return buyOrSell;
	}

	public void setBuyOrSell(BuySellEnum buyOrSell) {
		this.buyOrSell = buyOrSell;
	}

	public StockSymbol getStockTraded() {
		return stockTraded;
	}

	public void setStockTraded(StockSymbol stockTraded) {
		this.stockTraded = stockTraded;
	}

	public double getPriceTraded() {
		return priceTraded;
	}

	public void setPriceTraded(double priceTraded) {
		this.priceTraded = priceTraded;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public Date getDateOfTrade() {
		return dateOfTrade;
	}

	public void setDateOfTrade(Date dateOfTrade) {
		this.dateOfTrade = dateOfTrade;
	}

	public String getSoldOrBought() {
		if (BuyOrSell.matches(buyOrSell.getValue(), BuySellEnum.BUY)) {
			return ("Buy");
		} else {
			return ("Sell");
		}
	}

	public void setSoldOrBought(String soldOrBought) {
		this.soldOrBought = soldOrBought;
	}

	public boolean isIncluded(Integer minutesMinus) {
		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.MINUTE, -1 * minutesMinus);
		Date dateBeforeXMins = cal.getTime();
		if (this.dateOfTrade.after(dateBeforeXMins)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("StockTrade: " + this.getStockTraded().toString());
		BuySellEnum buy = this.getBuyOrSell();

		if (BuyOrSell.matches(buy.getValue(), BuySellEnum.BUY)) {
			sb.append(" (bought) ");
		} else {
			sb.append(" (sold) ");
		}
		sb.append(", price: " + this.getPriceTraded());
		sb.append(", qty: " + this.getQuantity());
		sb.append(", date:" + this.getDateOfTrade().toString());
		return sb.toString();
	}

}
