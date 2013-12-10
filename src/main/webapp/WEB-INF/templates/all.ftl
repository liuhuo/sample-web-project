<html>
<head>
  <meta http-equiv="Content-type" content="text/html; charset=UTF-8">
  <title>dummy user list example</title>
  <script type="text/javascript" src="resources/js/jquery-2.0.3.js"></script>
  <script type="text/javascript" src="resources/js/highcharts.js"></script>
  <script type="text/javascript" src="resources/js/jquery.handsontable.full.js"></script>
  <link rel="stylesheet" media="screen" href="resources/css/jquery.handsontable.full.css">

  <script type="text/javascript" src="resources/js/dummyuser.js">


  </script>
</head>
<body>
<div id="header">
<h2>
    FreeMarker Spring MVC Example示例
</h2>
</div>

<div id="content">

  <br/>
  <table class="datatable">
    <tr>
      <th>id</th>
      <th>name</th>
      <th>role</th>
    </tr>
    <#list users as user>
    <tr>
      <td>${user.id}</td>
      <td>${user.name}</td>
      <td>${user.role}</td>
    </tr>
    </#list>
  </table>

  <button id="button1" type="button">show me显示</button>

  <div id="dataTable"></div>

  <div id="container" style="min-width: 310px; height: 400px; margin: 0 auto">
  </div>

  <div id = "ltc"></div>
</div>
</body>
</html>
