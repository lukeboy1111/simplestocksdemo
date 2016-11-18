package com.lukecampbell.simplestocks.enums;

import com.lukecampbell.simplestocks.enums.StockType.StockTypeEnum;
import com.lukecampbell.simplestocks.support.StockConstants;

public class BuyOrSell {
	/**
	 * Matches.
	 *
	 * @param statusReceived
	 *            the status received
	 * @param thisService
	 *            the this service
	 * @return the boolean
	 */
	public static Boolean matches(Integer statusReceived, BuySellEnum thisService) {
		Integer thisStatus = thisService.getValue();
		if (thisStatus == statusReceived) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Matches.
	 *
	 * @param statusReceived
	 *            the status received
	 * @param thisService
	 *            the this service
	 * @return the boolean
	 */
	public static Boolean matches(int statusReceived, BuySellEnum thisService) {
		int thisStatus = thisService.getValue();
		if (thisStatus == statusReceived) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * The Enum BuySellEnum.
	 */
	public enum BuySellEnum {
		
		/** Unset. */
		UNSET(StockConstants.STOCK_UNSET),
		/** buy. */
		BUY(StockConstants.STOCK_BUY),

		/** sell. */
		SELL(StockConstants.STOCK_SELL);

		/** The value. */
		private final int value;

		BuySellEnum(final Integer newValue) {
			value = newValue;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public int getValue() {
			return value;
		}
	}
}
