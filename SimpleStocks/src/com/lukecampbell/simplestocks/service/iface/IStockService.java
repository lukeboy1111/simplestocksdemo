package com.lukecampbell.simplestocks.service.iface;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import com.lukecampbell.simplestocks.bo.StockSymbol;
import com.lukecampbell.simplestocks.bo.StockTradeContainer;
import com.lukecampbell.simplestocks.enums.BuyOrSell.BuySellEnum;
import com.lukecampbell.simplestocks.exceptions.CannotCalculateException;

public interface IStockService {
	public abstract double getVolumeWeightedStockPrice();

	public abstract double getProfitToEarningsRatio(StockSymbol s, double marketPrice);

	public abstract double getDividendYield(StockSymbol s, double marketPrice) throws IllegalArgumentException;

	public abstract void recordTradeWithoutDate(StockSymbol s, long quantity, BuySellEnum buyOrSell, double tradePrice);

	public abstract void recordTrade(StockSymbol s, long quantity, BuySellEnum buyOrSell, double tradePrice, Date dateOfTrade);

	public abstract void addStockToCollection(StockSymbol s);

	public abstract void addAllStocksToCollection(ArrayList<StockSymbol> s);

	public abstract int numberStocksInCollection();

	public abstract double getAllShareIndexPrice() throws CannotCalculateException;

	public abstract ArrayList<StockTradeContainer> getTradesList();

	public abstract void setTradesList(ArrayList<StockTradeContainer> tradesList);

	public abstract Boolean checkTrade(String symbol, String price, String buy, String qty);

	public abstract ArrayList<StockSymbol> getMyCollection();

	public abstract void setLastTradePrice(HttpSession session);
}
