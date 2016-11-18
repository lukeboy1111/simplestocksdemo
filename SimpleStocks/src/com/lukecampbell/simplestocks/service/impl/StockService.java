package com.lukecampbell.simplestocks.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.lukecampbell.simplestocks.bo.StockCollection;
import com.lukecampbell.simplestocks.bo.StockSymbol;
import com.lukecampbell.simplestocks.bo.StockTradeContainer;
import com.lukecampbell.simplestocks.bo.TradesContainer;
import com.lukecampbell.simplestocks.enums.BuyOrSell.BuySellEnum;
import com.lukecampbell.simplestocks.enums.StockType.StockTypeEnum;
import com.lukecampbell.simplestocks.exceptions.CannotCalculateException;
import com.lukecampbell.simplestocks.service.iface.IStockService;
import com.lukecampbell.simplestocks.support.StockConstants;

public class StockService implements IStockService {

	/** The logger. */
	private Logger LOGGER = Logger.getLogger(getClass().getName());

	private TradesContainer trades;
	private StockCollection myCollection;
	private StockSymbol lastItemTraded;
	private Double lastPriceTraded;

	public StockService() {
		trades = new TradesContainer();
		myCollection = new StockCollection();
	}

	@Override
	public double getVolumeWeightedStockPrice() {
		return trades.getVolumeWeightedStockPriceOverall(StockConstants.MINUTES);
	}

	@Override
	public double getProfitToEarningsRatio(StockSymbol s, double marketPrice) {
		try {
			return s.getProfitToEarningsRatioForMarketPrice(marketPrice);
		}
		catch (IllegalArgumentException ne) {
			// This is the default setting for profit to earning ratio.
			return 0.0;
		}
	}

	@Override
	public double getDividendYield(StockSymbol s, double marketPrice) throws IllegalArgumentException {
		if (s.isType(StockTypeEnum.COMMON)) {
			return s.getCommonDividendYieldForMarketPrice(marketPrice);
		} else if (s.isType(StockTypeEnum.PREFERRED)) {
			return s.getPreferredDividendYieldForMarketPrice(marketPrice);
		} else {
			throw new IllegalArgumentException("Stock does not have type set. Cannot calculate");
		}
	}

	@Override
	public void recordTradeWithoutDate(StockSymbol s, long quantity, BuySellEnum buyOrSell, double tradePrice) {
		StockTradeContainer trade = new StockTradeContainer(s, tradePrice, quantity, buyOrSell);
		trades.addTrade(trade);
	}

	@Override
	public void recordTrade(StockSymbol s, long quantity, BuySellEnum buyOrSell, double tradePrice, Date dateOfTrade) {

		StockTradeContainer trade = new StockTradeContainer(s, tradePrice, quantity, buyOrSell, dateOfTrade);
		trades.addTrade(trade);

	}

	@Override
	public void addStockToCollection(StockSymbol s) {
		myCollection.addStock(s);
	}

	@Override
	public void addAllStocksToCollection(ArrayList<StockSymbol> s) {
		myCollection.addAllStocks(s);
	}

	@Override
	public int numberStocksInCollection() {
		return myCollection.numberStocksInCollection();
	}

	@Override
	public double getAllShareIndexPrice() throws CannotCalculateException {
		return myCollection.getStockShareIndex(trades);

	}

	@Override
	public ArrayList<StockTradeContainer> getTradesList() {
		return trades.getTrades();
	}

	@Override
	public void setTradesList(ArrayList<StockTradeContainer> tradesList) {
		trades.setTrades(tradesList);
	}

	@Override
	public Boolean checkTrade(String symbol, String price, String buy, String qty) {
		Boolean ok = true;
		if (myCollection.doesntExist(symbol)) {
			ok = false;
		}

		Double thePrice = new Double(0);
		try {
			thePrice = Double.parseDouble(price);
		}
		catch (NumberFormatException e) {
			ok = false;
		}

		Long theQty = new Long(0);
		try {
			theQty = Long.parseLong(qty);
		}
		catch (NumberFormatException e) {
			ok = false;
		}

		if (buy.equals(StockConstants.STRING_BUY) || buy.equals(StockConstants.STRING_SELL)) {
			ok = true;
		}

		if (ok) {
			Calendar cal = Calendar.getInstance();
			Date rightNow = cal.getTime();
			BuySellEnum tradeType = BuySellEnum.BUY;
			if (buy.equals(StockConstants.STRING_SELL)) {
				tradeType = BuySellEnum.SELL;
			}
			lastItemTraded = myCollection.locate(symbol);
			lastPriceTraded = thePrice;
			recordTrade(lastItemTraded, theQty, tradeType, thePrice, rightNow);
		}

		return ok;
	}

	@Override
	public ArrayList<StockSymbol> getMyCollection() {
		return myCollection.getListStocks();
	}

	@Override
	public void setLastTradePrice(HttpSession session) {
		String symbol = lastItemTraded.getSymbol();
		String key = symbol + "-last";
		session.setAttribute(key, lastPriceTraded);
	}

	@Override
	public Double getStockShareIndex() throws CannotCalculateException {
		return myCollection.getStockShareIndex(trades);
	}

}
