<%@ page session="false"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.User"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  User loggedUser = (User) request.getAttribute("loggedUser");
  String applicationMessage = (String) request.getAttribute("applicationMessage");
  String menuActiveLink = "Home";
%>

<!DOCTYPE html>
<html>
<head>
  <%@include file="/include/htmlHead.jsp"%>
</head>
<body>
  <%@include file="/include/header.jsp"%>
  <main>
    <%if (loggedOn){%>
      Benvenuto <%=loggedUser.getUsername()%>! <br/>
    <%} else {%>
      Benvenuto. Loggati.
    <%}%>
  </main>
  <%@include file="/include/footer.inc"%>
</body>
</html>
