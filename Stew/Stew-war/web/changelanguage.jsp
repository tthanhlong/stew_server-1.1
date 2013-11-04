<%-- 
    Document   : changelanguage
    Created on : Sep 4, 2013, 4:47:28 PM
    Author     : tthanhlong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <title>JSP Page</title>
        <script type="text/javascript">
            $(document).ready(function(){
                var languageID = queryString()["language"];
                $.ajax({
                    type: "POST",
                    url: "api/ChangeLanguage",
                    data: "language="+languageID,
                    dataType: "json",
                    success: function(data){
						//Change from master
                    },
                    error: function(){
                        alert('error');
                    }
                });
            });
        </script>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
