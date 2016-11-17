<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="Test Page">
    <meta name="author" content="Luke Campbell">
    <link rel="icon" href="../../favicon.ico">

    <title>Simple Stocks</title>
	
    <!-- Bootstrap core CSS -->
    <link href="https://getbootstrap.com/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="https://getbootstrap.com/assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet">
    
    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.0/themes/smoothness/jquery-ui.css">

    <!-- Custom styles for this template -->
    <link href="/SimpleStocks/assets/css/starter-template.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="https://getbootstrap.com/assets/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
  <c:if test="${showMessageStatus > 0}">
   <p><c:out value="${showMessage}"/><p>
  </c:if>
  <h2> Portfolio </h2>
  <table class="table">
    <tr>
    	<th> Symbol </th>
    	<th> Last Dividend </th>
    	<th> Fixed Dividend</th>
    	<th> parValue </th>
    </tr>
  <c:forEach items="${portfolio}" var="stock">
  	<tr>
  		<td> ${stock.symbol } </td>
  		<td> <fmt:formatNumber value="${stock.lastDividendPence}" type="currency"/>&nbsp;(${stock.lastDividend}p) </td>
  		<td> <fmt:formatNumber value="${stock.fixedDividend }" type="percent"/> </td>
  		<td> <fmt:formatNumber value="${stock.parValuePence}" type="currency"/>&nbsp;(${stock.parValue}p)  </td>
  	</tr>
  </c:forEach>
  </table>
  <form action="index.html" method="post">
  	<select name="sell"><option value="buy">Buy</option><option value="sell">Sell</option></select>
  	&nbsp;<label for="symbol">Stock:</label>&nbsp;
  	<select name="symbol">
  		<option value="">--Choose--</option>
  		<c:forEach items="${portfolio}" var="stock">
  			<option value="${stock.symbol }">${stock.symbol }</option>
  		</c:forEach>
  	</select>
  	&nbsp;<label for="qty">Qty:</label>&nbsp;<input name="qty" value="10" size="3" maxsize="5" />
  	&nbsp;<label for="price">Price:</label>&nbsp;<input name="price" value="100.0" size="6" maxsize="8" />
  	&nbsp;<button type="submit">Add Trade</button>
  </form>
  <h2> Current Trades </h2>
  <table class="table">
   <tr>
    	<th> Date </th>
    	<th> Symbol </th>
    	<th> Quantity </th>
    	<th> Price Traded </th>
    	<th> Type </th>
    </tr>
  <c:forEach items="${tradesList}" var="trade">
    <tr>
    	<td> <fmt:formatDate type="both" value="${trade.dateOfTrade}" /> </td>
    	<td> ${trade.stockTraded.symbol} </td>
    	<td> ${trade.quantity} </td>
    	<td> <fmt:formatNumber value="${trade.priceTraded}" type="currency"/> </td>
    	<td> ${trade.soldOrBought}</td>
    </tr>
    
  </c:forEach>
  </table>
  <br />
  <p>
  <strong>Volume Weighted Stock Price</strong> = <fmt:formatNumber value="${weightedPrice}" type="currency"/>
  </p>
  <h3> Dividend Yields </h3>
  <ul>
  	<li> Tea: <fmt:formatNumber value="${dividendYieldForTea }" type="currency"/>&nbsp;(${currentTeaPrice }p)</li>
  	<li> Pop: <fmt:formatNumber value="${dividendYieldForPop }" type="currency"/>&nbsp;(${currentPopPrice }p)</li>
  	<li> Ale: <fmt:formatNumber value="${dividendYieldForAle }" type="currency"/>&nbsp;(${currentAlePrice }p)</li>
  	<li> Gin: <fmt:formatNumber value="${dividendYieldForGin }" type="currency"/>&nbsp;(${currentGinPrice }p)</li>
  	<li> Joe: <fmt:formatNumber value="${dividendYieldForJoe }" type="currency"/>&nbsp;(${currentJoePrice }p)</li>
  </ul>
  
  <h3> Profit / Earnings (P/E) Ratios </h3>
  <ul>
  	<li> Tea: <fmt:formatNumber type="number" maxFractionDigits="2" value="${peRatioTea }" /> </li>
  	<li> Pop: <fmt:formatNumber type="number" maxFractionDigits="2" value="${peRatioPop }" /> </li>
  	<li> Ale: <fmt:formatNumber type="number" maxFractionDigits="2" value="${peRatioAle }" /> </li>
  	<li> Gin: <fmt:formatNumber type="number" maxFractionDigits="2" value="${peRatioGin }" /> </li>
  	<li> Joe: <fmt:formatNumber type="number" maxFractionDigits="2" value="${peRatioJoe }" /> (Large: <fmt:formatNumber type="number" maxFractionDigits="2" value="${peRatioJoeLarge }" />) </li>
  </ul>
  
  </body>
</html>