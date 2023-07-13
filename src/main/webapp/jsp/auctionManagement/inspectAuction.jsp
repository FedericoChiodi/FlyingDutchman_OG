<%@page session="false"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.Auction"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.User"%>
<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Aste";
    Auction auction = (Auction) request.getAttribute("auction");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%=auction.getProduct_auctioned().getDescription()%>
</body>
</html>
