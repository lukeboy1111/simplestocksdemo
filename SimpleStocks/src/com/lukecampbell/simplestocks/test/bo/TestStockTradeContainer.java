package com.lukecampbell.simplestocks.test.bo;

import java.util.Date;

import com.lukecampbell.simplestocks.bo.StockSymbol;
import com.lukecampbell.simplestocks.bo.StockTradeContainer;
import com.lukecampbell.simplestocks.enums.BuyOrSell.BuySellEnum;
import com.lukecampbell.simplestocks.enums.StockType.StockTypeEnum;
import com.lukecampbell.simplestocks.support.StockConstants;

import junit.framework.TestCase;

public class TestStockTradeContainer extends TestCase {

	private final StockSymbol stockTea = new StockSymbol(StockConstants.TEA, StockTypeEnum.COMMON, 0, 0, 100);

	public void testGetSet() {
		StockTradeContainer trade = new StockTradeContainer();
		// public StockTradeContainer(StockSymbol stockTraded, double
		// priceTraded, long quantity, BuySellEnum buyOrSell) {
		Date rightNow = new Date();
		double price = 1.0;
		long quantity = 2;
		BuySellEnum type = BuySellEnum.BUY;
		String symbol = stockTea.getSymbol();
		trade.setStockTraded(stockTea);
		trade.setDateOfTrade(rightNow);
		trade.setPriceTraded(price);
		trade.setQuantity(quantity);
		trade.setBuyOrSell(type);
		trade.setDateOfTrade(rightNow);

		assertEquals(symbol, trade.getStockTraded().getSymbol());
		assertEquals(price, trade.getPriceTraded());
		assertEquals(quantity, trade.getQuantity());
		assertEquals(type, trade.getBuyOrSell());
		assertEquals(rightNow, trade.getDateOfTrade());

	}

	public void testConstructorWithoutDate() {
		Date rightNow = new Date();
		double price = 1.0;
		long quantity = 2;
		BuySellEnum type = BuySellEnum.BUY;
		String symbol = stockTea.getSymbol();
		StockTradeContainer trade = new StockTradeContainer(stockTea, price, quantity, type);

		assertEquals(symbol, trade.getStockTraded().getSymbol());
		assertEquals(price, trade.getPriceTraded());
		assertEquals(quantity, trade.getQuantity());
		assertEquals(type, trade.getBuyOrSell());

	}

	public void testConstructorWithDate() {
		Date rightNow = new Date();
		double price = 1.0;
		long quantity = 2;
		BuySellEnum type = BuySellEnum.BUY;
		String symbol = stockTea.getSymbol();
		StockTradeContainer trade = new StockTradeContainer(stockTea, price, quantity, type, rightNow);

		assertEquals(symbol, trade.getStockTraded().getSymbol());
		assertEquals(price, trade.getPriceTraded());
		assertEquals(quantity, trade.getQuantity());
		assertEquals(type, trade.getBuyOrSell());
		assertEquals(rightNow, trade.getDateOfTrade());
	}
}
