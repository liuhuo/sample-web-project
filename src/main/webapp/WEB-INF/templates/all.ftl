<html>
<head>
  <meta http-equiv="Content-type" content="text/html; charset=UTF-8">
  <title>dummy user list example</title>
  <script type="text/javascript" src="resources/js/jquery-2.0.3.js"></script>
  <!-- <script type="text/javascript" src="resources/js/highcharts.js"></script> -->
  <script type="text/javascript" src="resources/js/highstock.js"></script>
  <script type="text/javascript" src="resources/js/jquery.handsontable.full.js"></script>
  <script type="text/javascript" src="resources/js/jquery-ui.js"></script>
  <script type="text/javascript" src="resources/js/jquery.ui.datepicker-zh-CN.js"></script>
  <script type="text/javascript" src="resources/js/underscore-min.js"></script>
  <script type="text/javascript" src="resources/js/backbone-min.js"></script>

  <script type="text/javascript" src="resources/js/dummyuser.js"></script>
  <script type="text/javascript" src="resources/js/typeahead.min.js"></script>

  <link rel="stylesheet" media="screen" href="resources/css/jquery.handsontable.full.css"/>
  <link rel="stylesheet" href="resources/css/jquery-ui.css">
  <link rel="stylesheet" href="resources/css/examples.css">
  <style>
  #feedback { font-size: 1.4em; }
  #servicenames .ui-selecting { background: #FECA40; }
  #servicenames .ui-selected { background: #F39814; color: white; }
  #servicenames list-style-type: none; margin: 0; padding: 0; width: 15%; }
  #servicenames li { margin: 3px; padding: 0.4em; font-size: 1.4em; height: 18px; }


  </style>
  <script type="text/javascript" src="http://mobads.baidu.com/jssdk/js_siteapp.js"></script>
  <script type="text/javascript" src="http://mobads.baidu.com/jssdk/js_api.js"></script>

    <style type="text/css">
    .form1 {
        width: 300px;
        clear: both;
    }
    .form1 input {
        width: 100%;
        clear: both;
    }
    </style>
</head>
<body>
<div id="header">
<h2>
    FreeMarker Spring MVC Example示例
</h2>
</div>

<div id="content">

  <!-- <br/> -->
  <!-- <table class="datatable"> -->
  <!--   <tr> -->
  <!--     <th>id</th> -->
  <!--     <th>name</th>n -->
  <!--     <th>role</th> -->
  <!--   </tr> -->
  <!--   <#list users as user> -->
  <!--   <tr> -->
  <!--     <td>${user.id}</td> -->
  <!--     <td>${user.name}</td> -->
  <!--     <td>${user.role}</td> -->
  <!--   </tr> -->
  <!--   </#list> -->
  <!-- </table> -->


  <!-- <button id="button1" type="button">snhow me显示</button> -->
  <!-- <br/> -->
  <!-- <div class="container"> -->
  <!--   <div class="example"> -->
  <!--       <h2 class="example-name">names</h2> -->
  <!--       <div class="demo"> -->
  <!--         <input class="typeahead" type="text" id="ac"> -->
  <!--       </div> -->
  <!--   </div> -->
  <!-- </div> -->


  <!-- <div id="ad1" style="min-width: 480px; height: 64px; margin: 0 auto"> -->
  <!-- </div> -->

  <!-- <br/> -->
  <!-- <br/> -->
  <!-- <br/> -->
  <!-- <div id="ad2" style="min-width: 480px; height: 64px; margin: 0 auto"> -->
  <!-- </div> -->

  <!-- <br/> -->
  <!-- <br/> -->
  <!-- <br/> -->
  <!-- <div style="width:480px;height:64px;position:relative;" id="banner-iframe"> -->
  <!--   <iframe id="bd_ad_iframe" style="width:100%;height:100%;border:none;position:absolute;" src="http://mobads.baidu.com/jssdk/banner.html?appsec=debug&appsid=debug&ap=false" frameborder="0"></iframe> -->
  <!-- </div> -->

  <!-- <button id="button4" type="button">submit</button> -->
  <!-- <div id="dataTable"></div> -->

  <!-- <div id="form2"> -->
  <!--   <h1>My service</h1> -->
  <!--   <ul id="services"> -->

  <!--   </ul> -->
  <!--   <p id="total">total: <span>$0</span></p> -->
  <!-- </div> -->

  <!-- <div class="form1"> -->
  <!--   <label for="isp">Isp</label> -->
  <!--   <input type="text" id="isp" name="isp"/> -->
  <!--   <br/> -->

  <!--   <label for="website">Website</label> -->
  <!--   <input type="text" id="website" name="website"/> -->
  <!--   <br/> -->

  <!--   <label for="date">Date</label> -->
  <!--   <input type="text" id="date" name="date"/> -->
  <!--   <br/> -->

  <!--   <label for="clicks">Clicks</label> -->
  <!--   <input type="text" id="clicks" name="clicks"/> -->
  <!--   <br/> -->
  <!--   <button id="botton3" type="button">submit</button> -->
  <!-- </div> -->

  <!-- <br/> -->
  <!-- <br/> -->
  <label for="from">From</label>
  <input type="text" id="from" name="from">
  <label for="to">to</label>
  <input type="text" id="to" name="to">
  <button id="botton2" type="button">submit</button>
  <br/>
  <label for="allisp">isps</>
  <select id="allisp">
  </select>

  <div class="parent">
    <div id="sns" style="float:left; width: 15%;">
      <ol id="servicenames">
      </ol>
    </div>

    <div id="container" style="float:right; width:85%;min-width: 310px; height: 400px; margin: 0 auto">
    </div>
  </div>

  <!-- <div id="container1" style="left:85%;right:0;min-width: 310px; height: 400px; margin: 0 auto"> -->
  <!-- </div> -->

  <!-- <div id="container2" style="height: 500px; min-width: 500px"></div> -->
  <!-- <div id = "ltc"></div> -->
</div>
</body>
</html>
