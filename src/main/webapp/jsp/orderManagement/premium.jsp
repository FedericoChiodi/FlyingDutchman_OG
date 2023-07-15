<%@ page session="false"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.User"%>

<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Ordini";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/include/htmlHead.jsp"%>
    <script>
        function tryThresholds(){
            document.tryForm.submit();
        }
    </script>
    <style>
        #tryButton{
            padding: 10px 20px;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 10px;
            font-size: larger;
        }

        main{
            flex-direction: column;
            align-items: center;
            font-size: x-large;
        }
    </style>
</head>
<body>
    <%@include file="/include/header.jsp"%>
    <main>
        <section id="pageTitle">
            <h1>Grazie, <%=loggedUser.getUsername()%>!</h1>
        </section>

        <section id="buttonContainer">
            <button id="tryButton" onclick="tryThresholds()">
                Prova subito la nuova funzionalit&agrave; che hai sbloccato!
            </button>
        </section>

        <form name="tryForm" method="post" action="Dispatcher">
            <input type="hidden" name="controllerAction" value="ThresholdManagement.view">
        </form>
    </main>
    <%@include file="/include/footer.inc"%>
</body>
</html>
