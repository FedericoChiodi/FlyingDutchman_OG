<%@ page session="false"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.Auction"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.User"%>
<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Aste";
    Auction[] auctions = (Auction[]) request.getAttribute("auctions");
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.jsp"%>
        <script>
            function insertAuction(){
                document.insertForm.submit();
            }
            function mainOnLoadHandler(){
                document.querySelector("#insertAuctionButton").addEventListener("click",insertAuction);
            }
            function deleteAuction(auctionID){
                document.deleteForm.auctionID.value = auctionID;
                if(confirm("Attenzione! Questa azione e' irreversibile. Vuoi procedere?")){
                    document.deleteForm.submit();
                }
            }
            function inspectAuction(auctionID){
                document.inspectForm.auctionID.value = auctionID;
                document.inspectForm.submit();
            }

        </script>
        <style>
            /* Stile dei pulsanti */
            .button {
                padding: 12px 24px;
                font-size: 16px;
                cursor: pointer;
                transition: background-color 0.3s ease;
                background-color: #28a745;
            }
            #insertAuctionButtonSelection{
                margin: 12px 0;
            }
            #auctions{
                margin: 12px 0;
            }
            #auctions button{
                float: left;
                width: 250px;
                border-width: 1px;
                border-style: solid;
                border-radius: 10px;
                border-color: #1f9233;
                padding: 10px 10px 10px 10px;
                margin: 0 18px 16px 0;
                background: linear-gradient(to top, #4cf63b, #39ce29);
                box-shadow: 0 3px 2px #777;
            }
            #auctionButton:hover{
                background: #38b52a;
                cursor: pointer;
            }
            #auctionButton:active{
                background: #28a745;
                cursor: pointer;
            }
            #auctionListBreak hr{
                width: auto;
                color: #39ce29;
                height: auto;
            }
            #productImg{
                width: 70%;
                height: 70%;
                object-fit: contain;
            }
            #productDescription{
                font-size: large;
            }
            #productPrice{
                font-size: x-large;
            }
        </style>
    </head>
    <body>
        <%@include file="/include/header.jsp"%>
    <main>
        <section id="pageTitle">
            <h1>Aste in Corso</h1>
        </section>

        <section id="insertAuctionButtonSelection">
            <input type="button" id="insertAuctionButton" name="insertAuctionButton"
                   class="button" value="Metti in asta un Prodotto" onclick="insertAuction()"/>
        </section>

        <section id="auctionListBreak">
            <hr>
        </section>

        <%if(auctions.length > 0){%>
            <section id="auctions" class="clearfix">
                <%for (i = 0; i < auctions.length; i++){%>
                    <%if((auctions[i].getClosing_timestamp() == null) && (!auctions[i].getProduct_auctioned().getOwner().getUserID().equals(loggedUser.getUserID()))){%>
                        <button id="auctionButton" onclick="inspectAuction(<%=auctions[i].getAuctionID()%>)">
                            <b><span id="productDescription" class="description"><%=auctions[i].getProduct_auctioned().getDescription()%></span></b>
                            <span id="productPrice" class="current_price">&euro;<%=auctions[i].getProduct_auctioned().getCurrent_price()%></span>
                            <br/>
                            <img id="productImg" src="images/trashcan.png" alt="Immagine del Prodotto">
                        </button>
                    <%}%>
                <%}%>
            </section>
        <%}%>

        <form name="insertForm" method="post" action="Dispatcher">
            <input type="hidden" name="controllerAction" value="AuctionManagement.insertView"/>
        </form>

        <form name="inspectForm" method="post" action="Dispatcher">
            <input type="hidden" name="auctionID"/>
            <input type="hidden" name="controllerAction" value="AuctionManagement.inspectAuction"/>
        </form>

        <form name="deleteForm" method="post" action="Dispatcher">
            <input type="hidden" name="auctionID"/>
            <input type="hidden" name="controllerAction" value="AuctionManagement.delete"/>
        </form>
    </main>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
