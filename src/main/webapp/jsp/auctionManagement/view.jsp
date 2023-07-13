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
                document.querySelector("#newAuctionButton").addEventListener("click",insertAuction);
            }
            function deleteAuction(auctionID){
                document.deleteForm.auctionID.value = auctionID;
                if(confirm("Attenzione! Questa azione e' irreversibile. Vuoi procedere?")){
                    document.deleteForm.submit();
                }
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
            #products article{
                float: left;
                width: 250px;
                border-width: 1px;
                border-style: solid;
                border-radius: 10px;
                border-color: #1f9233;
                padding: 10px 8px 10px 20px;
                margin: 0 18px 16px 0;
                background: linear-gradient(to right, #28a745, #39ce29);
                box-shadow: 0 3px 2px #777;
            }
            #products article a{
                color: #28a745;
            }
            #trashcan{
                float: right;
            }
            #productListBreak hr{
                width: auto;
                color: #39ce29;
                height: auto;
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
            <input type="button" id="insertAuction" name="insertAuctionButton"
                   class="button" value="Inizia una nuova Asta" onclick="insertAuction()"/>
        </section>

        <section id="auctionListBreak">
            <hr>
        </section>

        <%if(auctions.length > 0){%>
            <section id="auctions" class="clearfix">
                <%for (i = 0; i < auctions.length; i++){%>
                    <%if(!auctions[i].isProduct_sold() && !auctions[i].getProduct_auctioned().getOwner().equals(loggedUser))%>
                        <article>
                            <b><span class="description"><%=auctions[i].getProduct_auctioned().getDescription()%></span></b>
                            <br/>
                            <span class="current_price">&euro;<%=auctions[i].getProduct_auctioned().getCurrent_price()%></span>
                        </article>
                    <%%>
                <%}%>
            </section>
        <%}%>

        <form name="insertForm" method="post" action="Dispatcher">
            <input type="hidden" name="controllerAction" value="AuctionManagement.insertView"/>
        </form>

        <form name="deleteForm" method="post" action="Dispatcher">
            <input type="hidden" name="auctionID"/>
            <input type="hidden" name="controllerAction" value="AuctionManagement.delete"/>
        </form>
    </main>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
