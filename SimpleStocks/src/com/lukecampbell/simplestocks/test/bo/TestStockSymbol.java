package com.lukecampbell.simplestocks.test.bo;

import org.junit.Test;

import com.lukecampbell.simplestocks.bo.StockSymbol;
import com.lukecampbell.simplestocks.enums.StockType.StockTypeEnum;

import junit.framework.TestCase;

public class TestStockSymbol extends TestCase {
	private final String STOCK_NAME = "BOB";
	private final String STOCK_NAME_2 = "BOB2";
	
	
	@Test
	public void test1() {
		StockSymbol stock = new StockSymbol();
		assertEquals("", stock.getSymbol());
		stock.setSymbol(STOCK_NAME);
		assertEquals(STOCK_NAME, stock.getSymbol());
		stock.setSymbol(STOCK_NAME_2);
		assertEquals(STOCK_NAME_2, stock.getSymbol());
	}
	
	@Test
	public void testStockPrice() {
		StockSymbol stock = new StockSymbol();
		double parValue = 15.0;
		stock.setParValue(parValue);
		assertEquals(parValue, stock.getParValue());
	}
	
	@Test
	public void testStockFunctions() {
		StockSymbol stock = new StockSymbol();
		double parValue = 15;
		double dividend = 1.55;
		double marketPrice = 17.5;
		double fixedDividend = 0.02;
		stock.setParValue(parValue);
		assertEquals(parValue, stock.getParValue());
		stock.setLastDividend(dividend);
		assertEquals(dividend, stock.getLastDividend());
		stock.setMarketPrice(marketPrice);
		double expectedCommonYield = dividend / marketPrice;
		assertEquals(expectedCommonYield, stock.getCommonDividendYield());
		stock.setFixedDividend(fixedDividend);
		assertEquals(fixedDividend, stock.getFixedDividend());
		double expectedPreferredYield = (fixedDividend * parValue) / marketPrice;
		assertEquals(expectedPreferredYield, stock.getPreferredDividendYield());
		double thisDividend = 1.25;
		double expectedEarningsRatio = marketPrice / thisDividend;
		assertEquals(expectedEarningsRatio, stock.getProfitToEarningsRatio(thisDividend));
	}
	
	@Test
	public void testStockFunctionsZero() {
		StockSymbol stock = new StockSymbol();
		double parValue = 15;
		double dividend = 1.55;
		double marketPrice = 0;
		double fixedDividend = 0.02;
		stock.setParValue(parValue);
		assertEquals(parValue, stock.getParValue());
		stock.setLastDividend(dividend);
		assertEquals(dividend, stock.getLastDividend());
		stock.setMarketPrice(marketPrice);
		double expectedCommonYield = dividend / marketPrice;
		try {
			assertEquals(expectedCommonYield, stock.getCommonDividendYield());
			assertTrue(false);
		}
		catch(IllegalArgumentException e) {
			
		}
		stock.setFixedDividend(fixedDividend);
		assertEquals(fixedDividend, stock.getFixedDividend());
		double expectedPreferredYield = (fixedDividend * parValue) / marketPrice;
		try {
			assertEquals(expectedPreferredYield, stock.getPreferredDividendYield());
			assertTrue(false);
		}
		catch(IllegalArgumentException e) {
			
		}
		double thisDividend = 0;
		double expectedEarningsRatio = marketPrice / thisDividend;
		try {
			assertEquals(expectedEarningsRatio, stock.getProfitToEarningsRatio(thisDividend));
			assertEquals(true, false);
		}
		catch(IllegalArgumentException e) {
			
		}
	}
	
	@Test
	public void testConstructor() {
		double parValue = 15;
		double dividend = 1.55;
		double marketPrice = 0;
		double fixedDividend = 0.02;
		String symbol = "STOCK";
		StockTypeEnum type = StockTypeEnum.COMMON;
		StockSymbol stock = new StockSymbol(symbol, type, dividend, fixedDividend, parValue, marketPrice);
		assertEquals(symbol, stock.getSymbol());
		assertTrue(stock.isType(StockTypeEnum.COMMON));
		assertFalse(stock.isType(StockTypeEnum.PREFERRED));
		assertFalse(stock.isType(StockTypeEnum.UNSET));
		assertEquals(parValue, stock.getParValue());
		assertEquals(dividend, stock.getLastDividend());
		assertEquals(marketPrice, stock.getMarketPrice());
		assertEquals(fixedDividend, stock.getFixedDividend());
	}
	
}
