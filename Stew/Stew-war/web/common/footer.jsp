<%-- 
    Document   : footer
    Created on : Aug 28, 2013, 7:06:54 PM
    Author     : tthanhlong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <footer class="footer">
            <div class="container">
                <div class="row">
                    <div class="span12 foot_links">
                        <a id="about_bottom_menu" href="about.jsp"><s:text name="about"/></a>
                        <a id="terms_bottom_menu" href="terms_and_conditions.jsp"><s:text name="termandcond"/></a>
                        <a id="how_bottom_menu" href="how_it_work.jsp"><s:text name="howitwork"/></a>
                        <a id="privacy_menu" href="privacy_policy.jsp"><s:text name="privacypolicy"/></a>
                        <a id="contact_bottom_menu" href="contact.jsp"><s:text name="contact"/></a>
                    </div>
                </div>	
                <div class="row">
                    <div class="span12 foot_text">
                        <p><s:text name="copyright"/></p>
                    </div>
                </div>
            </div>
        </footer>   
    </body>
</html>
