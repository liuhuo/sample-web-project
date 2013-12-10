<html>
    <body>
        <h2>dummy user ftl page in web-inf!!!</h2>

        <table border="1" cellspacing="10" cellpadding="5">
            <tr>
                <th>id</th>
                <th>name</th>
                <th>role</th>
            </tr>

            <#list result as element>
              <tr>
                <td>${element.id}</td>
                <td>${element.name}</td>
                <td>${element.role}</td>
              </tr>
            </#list>
        </table>
    </body>
</html>
