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
  <script>
    function buyPremium(){
      document.buyPremiumForm.submit();
    }
  </script>
</head>
<body>
  <%@include file="/include/header.jsp"%>
  <main>
    <%if (loggedOn){%>
      Benvenuto <%=loggedUser.getUsername()%>! <br/>
      Sei un <%=loggedUser.getRole()%>
      <%if (loggedUser.getRole().equals("Default")){%>
      <a href="javascript:buyPremium()">
        <span>Compra premium</span>
      </a>
      <%}%>
    <%}%>

    <%if (!loggedOn){%>
      Benvenuto. Loggati.
    <%}%>

    <form name="buyPremiumForm" method="post" action="Dispatcher">
      <input type="hidden" name="controllerAction" value="OrderManagement.buyPremiumView"/>
    </form>
  </main>
  <%@include file="/include/footer.inc"%>
</body>
</html>
