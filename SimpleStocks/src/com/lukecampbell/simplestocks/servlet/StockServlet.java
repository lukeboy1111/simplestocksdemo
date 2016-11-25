package com.lukecampbell.simplestocks.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lukecampbell.simplestocks.bo.StockSymbol;
import com.lukecampbell.simplestocks.bo.StockTradeContainer;
import com.lukecampbell.simplestocks.enums.BuyOrSell.BuySellEnum;
import com.lukecampbell.simplestocks.enums.StockType.StockTypeEnum;
import com.lukecampbell.simplestocks.exceptions.CannotCalculateException;
import com.lukecampbell.simplestocks.service.ServiceLocator;
import com.lukecampbell.simplestocks.service.iface.IStockService;
import com.lukecampbell.simplestocks.support.StockConstants;

public class StockServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger LOGGER = Logger.getLogger(getClass().getName());
	private IStockService stockService;

	private ArrayList<StockSymbol> stockSymbols;

	private StockSymbol stockTea = new StockSymbol(StockConstants.TEA, StockTypeEnum.COMMON, 0, 0, 100);
	private StockSymbol stockPop = new StockSymbol(StockConstants.POP, StockTypeEnum.COMMON, 8, 0, 100);
	private StockSymbol stockAle = new StockSymbol(StockConstants.ALE, StockTypeEnum.COMMON, 23, 0, 60);
	private StockSymbol stockGin = new StockSymbol(StockConstants.GIN, StockTypeEnum.PREFERRED, 8, 0.02, 100);
	private StockSymbol stockJoe = new StockSymbol(StockConstants.JOE, StockTypeEnum.COMMON, 13, 0, 250);

	private double teaPrice = 100;
	private double popPrice = 104.5;
	private double popPrice2 = 103.5;
	private double alePrice = 110;
	private double ginPrice = 130;
	private double ginPriceSell = 131.5;
	private double joePrice = 108;
	private double joePriceLarge = 103.76;
	private double expectedWeightedPrice = 0;

	public void initialise() {
		stockService = ServiceLocator.getStockService();
		stockSymbols = new ArrayList<StockSymbol>();
		stockSymbols.add(stockTea);
		stockSymbols.add(stockPop);
		stockSymbols.add(stockAle);
		stockSymbols.add(stockGin);
		stockSymbols.add(stockJoe);
		stockService.addAllStocksToCollection(stockSymbols);

	}

	public void recordStartingTrades() {

		Calendar cal = Calendar.getInstance();
		Date rightNow = cal.getTime();
		cal.add(Calendar.HOUR, -1);
		Date rightNowMinusAnHour = cal.getTime();

		stockService.recordTrade(stockGin, 100, BuySellEnum.SELL, ginPriceSell, rightNow);
		stockService.recordTrade(stockJoe, 1500, BuySellEnum.SELL, joePrice, rightNow);
		stockService.recordTrade(stockJoe, 10, BuySellEnum.SELL, joePriceLarge, rightNowMinusAnHour);
		stockService.recordTrade(stockJoe, 150, BuySellEnum.SELL, joePriceLarge, rightNowMinusAnHour);

	}

	private void getDividendsAndStockPrice(HttpServletRequest request, HttpSession session) {

		Double currentTeaPrice = getLatest(session, StockConstants.LAST_PRICE_TEA, teaPrice);
		Double currentPopPrice = getLatest(session, StockConstants.LAST_PRICE_POP, popPrice);
		Double currentAlePrice = getLatest(session, StockConstants.LAST_PRICE_ALE, alePrice);
		Double currentGinPrice = getLatest(session, StockConstants.LAST_PRICE_GIN, ginPrice);
		Double currentJoePrice = getLatest(session, StockConstants.LAST_PRICE_JOE, joePrice);

		request.setAttribute("currentTeaPrice", currentTeaPrice);
		request.setAttribute("currentPopPrice", currentPopPrice);
		request.setAttribute("currentAlePrice", currentAlePrice);
		request.setAttribute("currentGinPrice", currentGinPrice);
		request.setAttribute("currentJoePrice", currentJoePrice);

		double dividendYieldForTea = stockService.getDividendYield(stockTea, currentTeaPrice);
		double dividendYieldForPop = stockService.getDividendYield(stockPop, currentPopPrice);
		double dividendYieldForAle = stockService.getDividendYield(stockAle, currentAlePrice);
		double dividendYieldForGin = stockService.getDividendYield(stockGin, currentGinPrice);
		double dividendYieldForJoe = stockService.getDividendYield(stockJoe, currentJoePrice);

		request.setAttribute("dividendYieldForTea", dividendYieldForTea);
		request.setAttribute("dividendYieldForPop", dividendYieldForPop);
		request.setAttribute("dividendYieldForAle", dividendYieldForAle);
		request.setAttribute("dividendYieldForGin", dividendYieldForGin);
		request.setAttribute("dividendYieldForJoe", dividendYieldForJoe);

		double peRatioPop = stockService.getProfitToEarningsRatio(stockPop, currentPopPrice);
		double peRatioAle = stockService.getProfitToEarningsRatio(stockAle, currentAlePrice);
		double peRatioGin = stockService.getProfitToEarningsRatio(stockGin, currentGinPrice);
		double peRatioJoeLarge = stockService.getProfitToEarningsRatio(stockJoe, joePriceLarge);
		double peRatioJoe = stockService.getProfitToEarningsRatio(stockJoe, currentJoePrice);
		double peRatioTea = stockService.getProfitToEarningsRatio(stockTea, currentTeaPrice);

		request.setAttribute("peRatioJoeLarge", peRatioJoeLarge);
		request.setAttribute("peRatioJoe", peRatioJoe);
		request.setAttribute("peRatioPop", peRatioPop);
		request.setAttribute("peRatioGin", peRatioGin);
		request.setAttribute("peRatioAle", peRatioAle);
		request.setAttribute("peRatioTea", peRatioTea);
		double weightedPrice = 0;
		try {
			weightedPrice = stockService.getVolumeWeightedStockPrice();
		}
		catch (IllegalArgumentException e) {
			weightedPrice = 0;
		}

		request.setAttribute("weightedPrice", weightedPrice);

		Double allSharePrice = new Double(0);

		try {
			allSharePrice = stockService.getStockShareIndex();
			request.setAttribute("allShareValid", 1);
		}
		catch (CannotCalculateException e) {
			request.setAttribute("allShareValid", 0);
		}

		request.setAttribute("allSharePrice", allSharePrice);

	}

	private Double getLatest(HttpSession session, String sessionString, Double defaultPrice) {
		Double value = (Double) session.getAttribute(sessionString);
		if (null == value) {
			value = defaultPrice;
		}
		return value;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		initialise();

		ArrayList<StockTradeContainer> tradesList;

		tradesList = (ArrayList<StockTradeContainer>) session.getAttribute("tradesList");

		if (tradesList == null) {
			recordStartingTrades();
			tradesList = stockService.getTradesList();
			session.setAttribute("tradesList", tradesList);
		}
		stockService.setTradesList(tradesList);

		request.setAttribute("tradesList", tradesList);

		getDividendsAndStockPrice(request, session);

		String message = (String) request.getAttribute("message");

		if (!(null == message)) {
			request.setAttribute("showMessageStatus", 1);
			request.setAttribute("showMessage", message);
			request.removeAttribute("message");
		} else {
			request.setAttribute("showMessageStatus", 0);
		}

		ArrayList<StockSymbol> collection = stockService.getMyCollection();
		request.setAttribute("portfolio", collection);

		String nextJsp = "/WEB-INF/pages/index.jsp";
		request.getRequestDispatcher(nextJsp).forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		request.setAttribute("showMessageStatus", 0);
		request.setAttribute("message", "");
		initialise();
		ArrayList<StockTradeContainer> tradesList;
		tradesList = (ArrayList<StockTradeContainer>) session.getAttribute("tradesList");

		if (tradesList == null) {
			recordStartingTrades();
			tradesList = stockService.getTradesList();
		}
		stockService.setTradesList(tradesList);

		String symbol = request.getParameter("symbol");
		String price = request.getParameter("price");
		String buy = request.getParameter("sell");
		String qty = request.getParameter("qty");

		Boolean ok = false;
		try {
			ok = stockService.checkTrade(symbol, price, buy, qty);
			if (ok) {
				request.setAttribute("showMessageStatus", 1);
				request.setAttribute("message", "Trade was added");
				session.setAttribute("tradesList", tradesList);
				stockService.setLastTradePrice(session);
			} else {
				request.setAttribute("showMessageStatus", 1);
				request.setAttribute("message", "Trade was not added, there was an error in your submission.");
			}
		}
		catch (IllegalArgumentException e) {
			LOGGER.warning("Illegal Exception " + e.getMessage());
			request.setAttribute("showMessageStatus", 1);
			request.setAttribute("message", "Trade was not added, there was an error in your submission: " + e.getMessage());

		}
		tradesList = stockService.getTradesList();

		request.setAttribute("tradesList", tradesList);

		ArrayList<StockSymbol> collection = stockService.getMyCollection();
		request.setAttribute("portfolio", collection);

		getDividendsAndStockPrice(request, session);

		String nextJsp = "/WEB-INF/pages/index.jsp";
		request.getRequestDispatcher(nextJsp).forward(request, response);

	}
}
