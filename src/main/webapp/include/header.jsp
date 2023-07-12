<script>
  function headerOnLoadHandler() {
    var usernameTextField = document.querySelector("#username");
    var usernameTextFieldMsg = "Lo username \xE8 obbligatorio.";
    var passwordTextField = document.querySelector("#password");
    var passwordTextFieldMsg = "La password \xE8 obbligatoria.";

    if (usernameTextField != undefined && passwordTextField != undefined ) {
      usernameTextField.setCustomValidity(usernameTextFieldMsg);
      usernameTextField.addEventListener("change", function () {
        this.setCustomValidity(this.validity.valueMissing ? usernameTextFieldMsg : "");
      });
      passwordTextField.setCustomValidity(passwordTextFieldMsg);
      passwordTextField.addEventListener("change", function () {
       this.setCustomValidity(this.validity.valueMissing ? passwordTextFieldMsg : "");
      });
    }
  }
</script>

<header class="clearfix"><!-- Defining the header section of the page -->

  <h1 class="logo"><!-- Defining the logo element -->
    Flying Dutchman
  </h1>

  <form name="logoutForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value="HomeManagement.logout"/>
  </form>

  <nav><!-- Defining the navigation menu -->
    <ul>
      <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
        <a href="Dispatcher?controllerAction=HomeManagement.view">Home</a>
      </li>
      <%if (!loggedOn) {%>
        <li <%=menuActiveLink.equals("Registrati")?"class=\"active\"":""%>>
          <a href="Dispatcher?controllerAction=UserManagement.insertView">Registrati</a>
        </li>
      <%}%>
      <%if (loggedOn){%>
        <li <%=menuActiveLink.equals("Utente")?"class=\"active\"":""%>>
          <a href="Dispatcher?controllerAction=UserManagement.view">Utente</a>
        </li>
        <li <%=menuActiveLink.equals("Catalogo")?"class=\"active\"":""%>>
          <a href="Dispatcher?controllerAction=AuctionManagement.view">Catalogo</a>
        </li>
        <li <%=menuActiveLink.equals("Ordini")?"class=\"active\"":""%>>
          <a href="Dispatcher?controllerAction=OrderManagement.view">Ordini</a>
        </li>
        <%String role = loggedUser.getRole();%>
        <%if(role.equals("Premium") || role.equals("Admin") || role.equals("SuperAdmin")){%>
          <li <%=menuActiveLink.equals("Prenotazioni")?"class=\"active\"":""%>>
            <a href="Dispatcher?controllerAction=ThresholdManagement.view">Prenotazioni</a>
          </li>
        <%}%>
        <%if(role.equals("Admin") || role.equals("SuperAdmin")){%>
        <li <%=menuActiveLink.equals("Banna")?"class=\"active\"":""%>>
          <a href="Dispatcher?controllerAction=UserManagement.view">Banna</a>
        </li>
        <%}%>
        <%if(role.equals("SuperAdmin")){%>
        <li <%=menuActiveLink.equals("Abbassa!")?"class=\"active\"":""%>>
          <a href="Dispatcher?controllerAction=ProductManagement.view">Abbassa!</a>
        </li>
        <%}%>
        <li><a href="javascript:logoutForm.submit()">Logout</a></li>
      <%}%>
    </ul>
  </nav>

  <%if (!loggedOn) {%>
    <section id="login" class="clearfix">
      <form name="logonForm" action="Dispatcher" method="post">
        <label for="username">Utente</label>
        <input type="text" id="username"  name="username" maxlength="40" required>
        <label for="password">Password</label>
        <input type="password" id="password" name="password" maxlength="40" required>
        <input type="hidden" name="controllerAction" value="HomeManagement.logon"/>
        <input type="submit" value="Ok">
      </form>
    </section>
  <%}%>

</header>