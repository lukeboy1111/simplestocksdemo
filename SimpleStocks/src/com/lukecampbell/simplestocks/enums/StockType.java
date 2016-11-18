package com.lukecampbell.simplestocks.enums;

import com.lukecampbell.simplestocks.support.StockConstants;

public class StockType {

	/**
	 * Matches.
	 *
	 * @param statusReceived
	 *            the status received
	 * @param thisService
	 *            the this service
	 * @return the boolean
	 */
	public static Boolean matches(Integer statusReceived, StockTypeEnum thisService) {
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
	public static Boolean matches(int statusReceived, StockTypeEnum thisService) {
		int thisStatus = thisService.getValue();
		if (thisStatus == statusReceived) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * The Enum StockTypeEnum.
	 */
	public enum StockTypeEnum {
		
		/** Common Stock. */
		UNSET(StockConstants.STOCK_UNSET),
		/** Common Stock. */
		COMMON(StockConstants.STOCK_COMMON),

		/** Preferred Stock. */
		PREFERRED(StockConstants.STOCK_PREFERRED);

		/** The value. */
		private final int value;

		
		StockTypeEnum(final Integer newValue) {
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
