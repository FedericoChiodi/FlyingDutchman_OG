<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ingsw.flyingdutchman.model.mo.User" %>

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
  </head>
  <script>
    var status  = "<%=action%>";
    
    function submitUser(){
      var f;
      f = document.insModForm;
      f.controllerAction.value = "UserManagement."+status;
    }
    function goBack(){
      document.backForm.submit();
    }
    function mainOnLoadHandler(){
      document.insModForm.addEventListener("submit",submitUser);
      document.insModForm.backButton.addEventListener("click", goBack);
    }
  </script>
  <body>
    <%@include file="/include/header.jsp"%>
    <main>
    <section id="pageTitle">
      <h1>
        <%=(action.equals("modify")) ? "Modifica i tuoi dati" : "Inserisci i tuoi dati"%>
      </h1>
    </section>
    
    <section id="insModFormSection">
      <form name="insModForm" action="Dispatcher" method="post">
        
        <div class="field clearfix">
          <label for="username">Username</label>
          <input type="text" id="username" name="username"
                 value="<%=(action.equals("modify")) ? user.getUsername() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="password">Password</label>
          <input type="password" id="password" name="password"
                 value="<%=(action.equals("modify")) ? user.getPassword() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="firstname">Nome</label>
          <input type="text" id="firstname" name="firstname"
            value="<%=(action.equals("modify")) ? user.getFirstname() : ""%>"
            required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="surname">Cognome</label>
          <input type="text" id="surname" name="surname"
                 value="<%=(action.equals("modify")) ? user.getSurname() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="birthdate">Data di Nascita</label>
          <input type="date" id="birthdate" name="birthdate"
                 value="<%=(action.equals("modify")) ? user.getBirthdate() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="address">Indirizzo</label>
          <input type="text" id="address" name="address"
                 value="<%=(action.equals("modify")) ? user.getAddress() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="civic_number">Numero Civico</label>
          <input type="text" id="civic_number" name="civic_number"
                 value="<%=(action.equals("modify")) ? user.getCivic_number() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="cap">CAP</label>
          <input type="text" id="cap" name="cap"
                 value="<%=(action.equals("modify")) ? user.getCap() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="city">Città</label>
          <input type="text" id="city" name="city"
                 value="<%=(action.equals("modify")) ? user.getCity() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="state">Stato</label>
          <input type="text" id="state" name="state"
                 value="<%=(action.equals("modify")) ? user.getState() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="email">Email</label>
          <input type="email" id="email" name="email"
                 value="<%=(action.equals("modify")) ? user.getEmail() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="cel_number">Numero di Telefono</label>
          <input type="tel" id="cel_number" name="cel_number"
                 value="<%=(action.equals("modify")) ? user.getCel_number() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <input type="hidden" id="role" name="role"
                 value="Default"/>
        </div>
        <div class="field clearfix">
          <input type="hidden" id="deleted" name="deleted"
                 value="N"/>
        </div>
        <div class="field clearfix">
          <label>&#160;</label>
          <input type="submit" class="button" value="Invia"/>
          <input type="button" name="backButton" class="button" value="Annulla"/>
        </div>

        <input type="hidden" name="userID"/>
        <input type="hidden" name="controllerAction"/>
      </form>
    </section>
    
    <form name="backForm" method="post" action="Dispatcher">
      <input type="hidden" name="controllerAction" value="HomeManagement.view">
    </form>
    
  </main>
  <%@include file="/include/footer.inc"%>
  </body>
</html>
