package com.lukecampbell.simplestocks.bo;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.lukecampbell.simplestocks.enums.StockType;
import com.lukecampbell.simplestocks.enums.StockType.StockTypeEnum;

public class StockSymbol {

	/** The logger. */
	private Logger LOGGER = Logger.getLogger(getClass().getName());

	private String symbol;
	private StockTypeEnum type;
	private double lastDividend;
	private double lastDividendPence;
	private double fixedDividend;
	private double parValue;
	private double parValuePence;
	private double marketPrice;

	public StockSymbol() {
		this.symbol = "";
		this.type = StockTypeEnum.UNSET;
		this.lastDividend = 0;
		this.fixedDividend = 0 / 10;
		this.parValue = 1;
		this.marketPrice = 1;
	}

	public StockSymbol(String symbol, StockTypeEnum type, double lastDividend, double fixedDividend, double parValue, double market) {
		super();
		this.symbol = symbol;
		this.type = type;
		this.lastDividend = lastDividend;
		this.fixedDividend = fixedDividend;
		this.parValue = parValue;
		this.marketPrice = market;
	}

	public StockSymbol(String symbol, StockTypeEnum type, double lastDiv, double fixedDiv, double parValue) {
		this.symbol = symbol;
		this.type = type;
		this.lastDividend = lastDiv;
		this.fixedDividend = fixedDiv;
		this.parValue = parValue;
		this.marketPrice = parValue;
	}

	public double getCommonDividendYield() throws IllegalArgumentException {
		if (marketPrice == 0) {
			throw new IllegalArgumentException("Market price is zero");
		}
		double yield = lastDividend / marketPrice;

		return yield;
	}

	public double getCommonDividendYieldForMarketPrice(double marketPriceCurrent) throws IllegalArgumentException {
		if (marketPriceCurrent == 0) {
			throw new IllegalArgumentException("Market price is zero");
		}
		double yield = lastDividend / marketPriceCurrent;
		return yield;
	}

	public double getPreferredDividendYield() throws IllegalArgumentException {
		if (marketPrice == 0) {
			throw new IllegalArgumentException("Market price is zero");
		}
		double multiple = fixedDividend * parValue;
		double yield = multiple / marketPrice;
		return yield;
	}

	public double getPreferredDividendYieldForMarketPrice(double currentMarketPrice) throws IllegalArgumentException {
		if (currentMarketPrice == 0) {
			throw new IllegalArgumentException("Market price is zero");
		}
		double multiple = fixedDividend * parValue;
		double yield = multiple / marketPrice;
		return yield;
	}

	public double getProfitToEarningsRatio(double dividend) throws IllegalArgumentException {
		if (dividend == 0) {
			throw new IllegalArgumentException("Dividend is zero");
		}
		double ratio = marketPrice / dividend;
		return ratio;
	}

	public double getProfitToEarningsRatioForMarketPrice(double suppliedMarketPrice) throws IllegalArgumentException {
		if (lastDividend == 0) {
			throw new IllegalArgumentException("lastDividend is zero");
		}
		double ratio = suppliedMarketPrice / lastDividend;
		return ratio;
	}

	public double getGeometricMean(ArrayList<Double> prices) throws IllegalArgumentException {
		Double meanValue = calculateGeometricMean(prices);
		return meanValue;
	}

	private Double calculateGeometricMean(ArrayList<Double> list) {
		int n = list.size();
		Double GM_log = 0.0d;
		for (double price : list) {
			if (price == 0L) {
				return 0.0d;
			}
			Double item = Math.log(price);
			GM_log += item;
		}
		return Math.exp(GM_log / n);
	}

	public static double nthRoot(int n, double figure) {
		return Math.pow(figure, (1 / n));
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public StockTypeEnum getType() {
		return type;
	}

	public void setType(StockTypeEnum type) {
		this.type = type;
	}

	public double getLastDividend() {
		return lastDividend;
	}

	public void setLastDividend(double lastDividend) {
		this.lastDividend = lastDividend;
	}

	public double getFixedDividend() {
		return fixedDividend;
	}

	public void setFixedDividend(double fixedDividend) {
		this.fixedDividend = fixedDividend;
	}

	public double getParValue() {
		return parValue;
	}

	public void setParValue(double parValue) {
		this.parValue = parValue;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public double getParValuePence() {
		return parValue / 100;
	}

	public double getLastDividendPence() {
		return lastDividend / 100;
	}

	public void setParValuePence(double parValuePence) {
		this.parValuePence = parValuePence;
	}

	public boolean isType(int i) {
		boolean matches = StockType.matches(i, StockTypeEnum.COMMON);
		return matches;
	}

	public boolean isType(StockTypeEnum i) {
		int val = i.getValue();
		boolean matches = StockType.matches(val, StockTypeEnum.COMMON);
		return matches;
	}

	public boolean isActually(StockSymbol s) {
		if (this.getSymbol().equals(s.getSymbol())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Stock: " + this.getSymbol());
		sb.append(", parValue: " + this.getParValue());
		sb.append(", marketPrice: " + this.getMarketPrice());
		return sb.toString();
	}

}
