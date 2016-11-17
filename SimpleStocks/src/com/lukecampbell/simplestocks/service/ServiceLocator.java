package com.lukecampbell.simplestocks.service;

import com.lukecampbell.simplestocks.service.iface.IStockService;
import com.lukecampbell.simplestocks.service.iface.impl.StockService;

public class ServiceLocator {

	public static IStockService getStockService() {
		return new StockService();
	}

}
