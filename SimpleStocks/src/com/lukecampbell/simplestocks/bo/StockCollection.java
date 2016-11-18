package com.lukecampbell.simplestocks.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.lukecampbell.simplestocks.exceptions.CannotCalculateException;

public class StockCollection {
	private Logger LOGGER = Logger.getLogger(getClass().getName());
	private ArrayList<StockSymbol> listStocks;
	private HashMap<String, Double> stocks;
	private Double totalValue;

	public StockCollection() {
		listStocks = new ArrayList<StockSymbol>();
		stocks = new HashMap<String, Double>();
		totalValue = 0.0;
	}

	public void addStock(StockSymbol s) throws IllegalArgumentException {

		String symbol = s.getSymbol();
		Double parValue = s.getParValue();
		if (stocks.containsKey(symbol)) {
			throw new IllegalArgumentException("Stock exists in portfolio");
		}
		totalValue += parValue;
		listStocks.add(s);
		stocks.put(symbol, parValue);
	}

	public Double getStockShareIndex(TradesContainer trades) throws CannotCalculateException {
		Double indexValue = 0.0;
		if (totalValue == 0.0) {
			return indexValue;
		}

		if (listStocks.size() == 0) {
			throw new CannotCalculateException("No Stocks To Calculate");
		}

		for (StockSymbol s : listStocks) {
			Double parValue = s.getParValue();
			ArrayList<Double> prices = trades.getTradePricesForStock(s);
			if (!(null == prices) && prices.size() > 0) {
				Double geoMeanPrice = s.getGeometricMean(prices);
				Double weighting = parValue / totalValue;
				Double weightedPrice = (geoMeanPrice * weighting);
				indexValue += weightedPrice;
			}
		}
		return indexValue;
	}

	public void addAllStocks(ArrayList<StockSymbol> s) {
		for (StockSymbol stock : s) {
			addStock(stock);
		}

	}

	public int numberStocksInCollection() {
		return listStocks.size();
	}

	public boolean doesntExist(String symbol) {
		for (StockSymbol s : listStocks) {
			if (s.getSymbol().equals(symbol)) {
				return false;
			}
		}
		return true;
	}

	public StockSymbol locate(String symbol) {
		for (StockSymbol s : listStocks) {
			if (s.getSymbol().equals(symbol)) {
				return s;
			}
		}
		return null;
	}

	public ArrayList<StockSymbol> getListStocks() {
		return listStocks;
	}

	public void setListStocks(ArrayList<StockSymbol> listStocks) {
		this.listStocks = listStocks;
	}

}
