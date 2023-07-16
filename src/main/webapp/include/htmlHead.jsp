<meta charset="utf-8"/>

<!-- Linking styles -->
<link rel="stylesheet" href="css/flyingdutchman_v0.7.css" type="text/css" media="screen">
<title>Flying Dutchman: <%=menuActiveLink%></title>
<script>
  var applicationMessage;
  <%if (applicationMessage != null) {%>
    applicationMessage="<%=applicationMessage%>";
  <%}%>
  function onLoadHandler() {
    headerOnLoadHandler();
    try { mainOnLoadHandler(); } catch (e) {}
    if (applicationMessage!=undefined) alert(applicationMessage);
  }

  window.addEventListener("load", onLoadHandler);

  function formatFloat(value) {
      var number = parseFloat(value);
      if (!isNaN(number)) {
          return '\u20AC' + number.toFixed(2);
      } else {
          return value;
      }
  }

  // Funzione per formattare tutti i numeri float nella pagina
  function formatAllFloats() {
      var floatElements = document.getElementsByClassName('float-value');
      for (var i = 0; i < floatElements.length; i++) {
          var element = floatElements[i];
          var value = element.textContent;
          element.textContent = formatFloat(value);
    }
  }

  // Chiamata alla funzione formatAllFloats al caricamento della pagina
      window.onload = function() {
      formatAllFloats();
  };

</script>