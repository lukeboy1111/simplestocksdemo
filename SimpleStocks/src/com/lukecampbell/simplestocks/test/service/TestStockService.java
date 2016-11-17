package com.lukecampbell.simplestocks.test.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import com.lukecampbell.simplestocks.bo.StockSymbol;
import com.lukecampbell.simplestocks.bo.StockTradeContainer;
import com.lukecampbell.simplestocks.bo.TradesContainer;
import com.lukecampbell.simplestocks.enums.BuyOrSell.BuySellEnum;
import com.lukecampbell.simplestocks.enums.StockType.StockTypeEnum;
import com.lukecampbell.simplestocks.exceptions.CannotCalculateException;
import com.lukecampbell.simplestocks.service.ServiceLocator;
import com.lukecampbell.simplestocks.service.iface.IStockService;
import com.lukecampbell.simplestocks.support.StockConstants;

import junit.framework.TestCase;

public class TestStockService extends TestCase {
	private Logger LOGGER = Logger.getLogger(TradesContainer.class.getName());

	private final IStockService stockService = ServiceLocator.getStockService();
	private ArrayList<StockSymbol> stockSymbols;
	private StockSymbol stockTea = new StockSymbol(StockConstants.TEA, StockTypeEnum.COMMON, 0, 0, 100);
	private StockSymbol stockPop = new StockSymbol(StockConstants.POP, StockTypeEnum.COMMON, 8, 0, 100);
	private StockSymbol stockAle = new StockSymbol(StockConstants.ALE, StockTypeEnum.COMMON, 23, 0, 60);
	private StockSymbol stockGin = new StockSymbol(StockConstants.GIN, StockTypeEnum.PREFERRED, 8, 0.02, 100);
	private StockSymbol stockJoe = new StockSymbol(StockConstants.JOE, StockTypeEnum.COMMON, 13, 0, 250);

	private double popPrice = 104.5;
	private double popPrice2 = 103.5;
	private double alePrice = 110;
	private double ginPrice = 130;
	private double ginPriceSell = 131.5;
	private double joePrice = 101;
	private double joePriceLarge = 103.76;
	private double expectedWeightedPrice = 0;

	public void setUp() {
		stockSymbols = new ArrayList<StockSymbol>();
		stockSymbols.add(stockTea);
		stockSymbols.add(stockPop);
		stockSymbols.add(stockAle);
		stockSymbols.add(stockGin);
		stockSymbols.add(stockJoe);
		stockService.addAllStocksToCollection(stockSymbols);
	}

	public void tearDown() {

	}

	public void testAddTrades() {

		int size = stockSymbols.size();
		int expected = 5;
		assertEquals(expected, size);

		int collectionSize = stockService.numberStocksInCollection();
		assertEquals(expected, collectionSize);
		stockService.recordTradeWithoutDate(stockTea, 10, BuySellEnum.SELL, 101.5);

		Calendar cal = Calendar.getInstance();
		Date rightNow = cal.getTime();
		cal.add(Calendar.HOUR, -1);
		Date rightNowMinusAnHour = cal.getTime();

		stockService.recordTrade(stockPop, 1000, BuySellEnum.BUY, popPrice, rightNow);
		stockService.recordTrade(stockPop, 50, BuySellEnum.SELL, popPrice2, rightNow);
		stockService.recordTrade(stockAle, 500, BuySellEnum.BUY, alePrice, rightNow);
		stockService.recordTrade(stockGin, 10, BuySellEnum.BUY, ginPrice, rightNow);
		stockService.recordTrade(stockGin, 100, BuySellEnum.SELL, ginPriceSell, rightNow);
		stockService.recordTrade(stockJoe, 10, BuySellEnum.SELL, joePriceLarge, rightNowMinusAnHour);
		stockService.recordTrade(stockJoe, 150, BuySellEnum.SELL, joePriceLarge, rightNowMinusAnHour);
		stockService.recordTrade(stockJoe, 1500, BuySellEnum.SELL, joePrice, rightNow);

		double dividendYieldForPop = stockService.getDividendYield(stockPop, popPrice);
		System.out.println("dividendYieldForPop : " + dividendYieldForPop);

		double dividendYieldForAle = stockService.getDividendYield(stockAle, alePrice);
		System.out.println("dividendYieldForAle : " + dividendYieldForAle);

		double dividendYieldForGin = stockService.getDividendYield(stockGin, ginPrice);
		System.out.println("dividendYieldForGin : " + dividendYieldForGin);

		double dividendYieldForJoe = stockService.getDividendYield(stockJoe, joePriceLarge);
		System.out.println("dividendYieldForJoe : " + dividendYieldForJoe);

		ArrayList<StockTradeContainer> tradesList = stockService.getTradesList();
		System.out.println("# Trades: " + tradesList.size());
		for (StockTradeContainer tradeDetail : tradesList) {
			System.out.println("Trade: " + tradeDetail.toString());
		}

		double peRatioAle = stockService.getProfitToEarningsRatio(stockAle, alePrice);
		System.out.println("* peRatioAle " + peRatioAle);
		double peRatioGin = stockService.getProfitToEarningsRatio(stockGin, ginPrice);
		System.out.println("* peRatioGin " + peRatioGin);
		double peRatioJoeLarge = stockService.getProfitToEarningsRatio(stockJoe, joePriceLarge);
		System.out.println("* peRatioJoeLarge " + peRatioJoeLarge);
		double peRatioJoe = stockService.getProfitToEarningsRatio(stockJoe, joePrice);
		System.out.println("* peRatioJoe " + peRatioJoe);

		double weightedPrice = stockService.getVolumeWeightedStockPrice();
		System.out.println("* Volume Weighted Price is " + weightedPrice);
		// assertEquals(expectedWeightedPrice, weightedPrice);
		try {
			double stockIndexPrice = stockService.getAllShareIndexPrice();
			System.out.println("* stockIndexPrice is " + stockIndexPrice);
		}
		catch (CannotCalculateException e) {
			LOGGER.warning("* Could not calculate stock index price");
			assertTrue(false);
		}

	}

}
