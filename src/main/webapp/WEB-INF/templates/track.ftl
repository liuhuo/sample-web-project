<html>
<head>
  <meta http-equiv="Content-type" content="text/html; charset=UTF-8">
  <title>dummy user list example</title>
  <script type="text/javascript" src="/data-cruncher/resources/js/jquery-2.0.3.js"></script>
  <!-- <script type="text/javascript" src="/data-cruncher/resources/js/highcharts.js"></script> -->
  <script type="text/javascript" src="/data-cruncher/resources/js/highstock.js"></script>
  <script type="text/javascript" src="/data-cruncher/resources/js/jquery.handsontable.full.js"></script>
  <script type="text/javascript" src="/data-cruncher/resources/js/jquery-ui.js"></script>
  <script type="text/javascript" src="/data-cruncher/resources/js/jquery.ui.datepicker-zh-CN.js"></script>
  <script type="text/javascript" src="/data-cruncher/resources/js/jquery.dataTables.min.js"></script>
  <script type="text/javascript" src="/data-cruncher/resources/js/track.js"></script>

  <link rel="stylesheet" media="screen" href="/data-cruncher/resources/css/jquery.handsontable.full.css"/>
  <link rel="stylesheet" href="/data-cruncher/resources/css/jquery-ui.css">
  <link rel="stylesheet" href="/data-cruncher/resources/css/jquery.dataTables.css">

  <style type="text/css">
    .error {
    word-wrap: break-word;
    max-width: 150px;
    }
  </style>
    <!-- <style type="text/css"> -->
    <!--     #connect-container { -->
    <!--         float: left; -->
    <!--         width: 400px -->
    <!--     } -->

    <!--     #connect-container div { -->
    <!--         padding: 5px; -->
    <!--     } -->

    <!--     #console-container { -->
    <!--         float: left; -->
    <!--         margin-left: 15px; -->
    <!--         width: 400px; -->
    <!--     } -->

    <!--     #console { -->
    <!--         border: 1px solid #CCCCCC; -->
    <!--         border-right-color: #999999; -->
    <!--         border-bottom-color: #999999; -->
    <!--         height: 170px; -->
    <!--         overflow-y: scroll; -->
    <!--         padding: 5px; -->
    <!--         width: 100%; -->
    <!--     } -->

    <!--     #console p { -->
    <!--         padding: 0; -->
    <!--         margin: 0; -->
    <!--     } -->
    <!-- </style> -->
</head>

<body>

  <div id="trace-mode-container">
    <button id="button1" type="button">realtime track</button>
    <button id="button2" type="button">all records</button>
  </div>

    <!-- <div id="connect-container"> -->
    <!--   <label>websocket</label> -->
    <!--     <div> -->
    <!--         <button id="connect">Connect</button> -->
    <!--         <button id="disconnect" disabled="disabled">Disconnect</button> -->
    <!--     </div> -->
    <!--     <div> -->
    <!--         <textarea id="message" style="width: 350px">Here is a message!</textarea> -->
    <!--     </div> -->
    <!--     <div> -->
    <!--         <button id="echo" disabled="disabled">send message</button> -->
    <!--     </div> -->
    <!-- </div> -->

    <div id="console-container">
      <div id="console">
      </div>

      <!-- <table id="table1" border="1"> -->
      <!-- </table> -->

      <div id="all-table-container">

      </div>
    </div>



</body>
</html>
