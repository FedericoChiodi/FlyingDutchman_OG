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
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.jsp"%>
        <script>
            function buyProduct(auctionID){
                document.buyForm.auctionID.value = auctionID;
                document.buyForm.submit();
            }
            function goBack(){
                document.backForm.submit();
            }
            function insertThreshold(auctionID){
                document.insertThresholdForm.auctionID.value = auctionID;
                document.insertThresholdForm.submit();
            }
        </script>
        <style>
            #productContainer {
                display: flex;
                align-items: center;
                flex-direction: row;
            }
            #productInfoContainer{
                display: flex;
                align-items: center;
                flex-direction: column;
                margin-bottom: 15px;
                margin-top: 25px;
            }
            #productDescription, #productPrice{
                display: block;
                margin-top: 15px;
                font-size: xx-large;
            }
            #productSeller{
                font-size: medium;
            }
            #productInfoContainer button{
                font-size: large;
                margin-bottom: 10px;
            }
            #productImage {
                width: 200px;
                height: 200px;
                margin-right: 20px;
                margin-top: 25px;
            }
            #buyProductButton{
                padding: 10px 20px;
                background-color: #337ab7;
                color: #fff;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                margin-right: 10px;
                margin-top: 10px;
            }
            #backButton{
                padding: 10px 20px;
                background-color: #dc3545;
                color: #fff;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                margin-right: 10px;
            }
            #thresholdButton{
                padding: 10px 20px;
                background-color: darkorange;
                color: #fff;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                margin-right: 10px;
            }

        </style>
    </head>
    <body>
        <%@include file="/include/header.jsp"%>
        <main>
            <section id="productContainer">
                <article id="productImageContainer">
                    <img id="productImage" src="images/trashcan.png" alt="Product_Image">
                </article>
                <article id="productInfoContainer">
                    <span id="productDescription"><%=auction.getProduct_auctioned().getDescription()%></span>
                    <br/>
                    <span id="productSeller">Venduto da: <%=auction.getProduct_auctioned().getOwner().getUsername()%></span>
                    <br/><br/>
                    <span id="productPrice">&euro;<%=auction.getProduct_auctioned().getCurrent_price()%></span>
                    <br/>
                    <button id="buyProductButton" onclick="buyProduct(<%=auction.getAuctionID()%>)">Compra questo Prodotto</button>
                    <%if(!loggedUser.getRole().equals("Default")){%>
                        <button id="thresholdButton" onclick="insertThreshold(<%=auction.getAuctionID()%>)">Prenota</button>
                    <%}%>
                    <button id="backButton" onclick="goBack()">Torna Indietro</button>
                </article>
            </section>

            <form name="buyForm" method="post" action="Dispatcher">
                <input type="hidden" name="auctionID"/> <!-- TODO: qua ho tolto l'id, se qualcosa si rompe nelle aste guarda qua -->
                <input type="hidden" name="controllerAction" value="AuctionManagement.buyProductAuctioned"/>
            </form>

            <form name="insertThresholdForm" method="post" action="Dispatcher">
                <input type="hidden" name="auctionID"/>
                <input type="hidden" name="controllerAction" value="ThresholdManagement.insertView"/>
            </form>

            <form name="backForm" method="post" action="Dispatcher">
                <input type="hidden" name="controllerAction" value="AuctionManagement.view">
            </form>
        </main>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
