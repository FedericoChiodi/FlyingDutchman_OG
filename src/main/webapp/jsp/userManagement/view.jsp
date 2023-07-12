<%@ page session="false"%>
<%@ page import="com.ingsw.flyingdutchman.model.mo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Registrati";
    User user = (User) request.getAttribute("user");
    String action = (user!=null) ? "modify" : "insert";
%>
<!DOCTYPE html>
    <html>
    <head>
        <%@include file="/include/htmlHead.jsp"%>
        <style>
            #newUserButtonSelection{
                margin: 12px 0;
            }
            #deleteUserButtonSelection{
                margin: 12px 0;
            }
            #modifyUserButtonSelection{
                margin: 12px 0;
            }
        </style>
        <script>
            function insertUser(){
                document.insertForm.submit();
            }
            function mainOnLoadHandler(){
                document.querySelector("#newUserButton").addEventListener("click",insertUser);
            }
            function modifyUser(userID){
                document.modifyForm.userID.value = userID;
                document.modifyForm.submit();
            }
            function deleteUser(userID){
                document.deleteForm.userID.value = userID;
                document.deleteForm.submit();
            }
        </script>
    </head>
    <body>
        <%@include file="/include/header.jsp"%>
    <main>
        <section id="pageTitle">
            <h1>Centro Gestione degli Utenti</h1>
        </section>

        <section id="insertUserButtonSelection">
            <input type="button" id="insertUserButton" name="insertUserButton"
                   class="button" value="Registrati" onclick="insertUser()"/>
        </section>

        <%if (loggedOn){%>
            <section id="modifyUserButtonSelection">
                <input type="button" id="modifyUserButton" name="modifyUserButton"
                       class="button" value="Modifica i miei dati" onclick="modifyUser(<%=loggedUser.getUserID()%>)"/>
            </section>

            <section id="deleteUserButtonSelection">
                <input type="button" id="deleteUserButton" name="deleteUserButton"
                       class="button" value="Cancella il mio Account" onclick="deleteUser(<%=loggedUser.getUserID()%>)"/>
            </section>
        <%}%>
        <form name="insertForm" method="post" action="Dispatcher">
            <input type="hidden" name="controllerAction" value="UserManagement.insertView"/>
        </form>

        <form name="modifyForm" method="post" action="Dispatcher">
            <input type="hidden" name="userID"/>
            <input type="hidden" name="controllerAction" value="UserManagement.modifyView"/>
        </form>

        <form name="deleteForm" method="post" action="Dispatcher">
            <input type="hidden" name="userID"/>
            <input type="hidden" name="controllerAction" value="UserManagement.delete"/>
        </form>
    </main>
    <%@include file="/include/footer.inc"%>
    </body>
</html>
