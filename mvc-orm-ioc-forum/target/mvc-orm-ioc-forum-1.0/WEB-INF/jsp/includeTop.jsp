<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<a href="<c:url value="/index.jsp"/>">首页</a>&nbsp;&nbsp;
<c:if test="${!empty USER_CONTEXT.userName}">
   ${USER_CONTEXT.userName}(${USER_CONTEXT.credit}),欢迎您的到来,<a href="<c:url value="/login/doLogout.html"/>">注销</a> ${USER_CONTEXT.userType}
</c:if>
&nbsp;&nbsp;
<c:if test="${empty USER_CONTEXT.userName}">
   <a href="<c:url value="/login.jsp"/>">登录</a>&nbsp;&nbsp;
   <a href="<c:url value="/register.jsp"/>">注册</a>
</c:if>
<c:if test="${USER_CONTEXT.userType == 2}">
   <!--
   <a href="<c:url value="/addBoardPage.html"/>">新建论坛版块</a>&nbsp;&nbsp;
   -->
   <!-- /boards post-->
   <script>
       function validateForm(form) {

           var boardName = form.boardName;
           var boardDesc = form.boardDesc;
           if (boardName.value == null || boardName.value.length == 0 || boardName.value == "") {
               alert("版块名称不能为空，请填上.");
               boardName.focus();
               return false;
           } else if (boardName.value.length > 150) {
               alert("版块名称最大长度不能超过150个字符，请调整.");
               boardName.focus();
               return false;
           }

           if (boardDesc.value != null && boardDesc.value.length > 255) {
               alert("版块描述最大长度不能超过255个字符，请调整.");
               boardDesc.focus();
               return false;
           }
           return true;
       }
   </script>
   <script type="text/javascript">
       function createFormAndTable(){
           var form1 = document.createElement("form");
           form1.action = "<c:url value="/boards"/>";
           form1.method = "post";
           form1.onsubmit = "return validateForm(this)";

           var body = document.getElementsByTagName('body')[0];
           var tbl = document.createElement('table');
           tbl.style.width = '100%';
           tbl.setAttribute('border', '1px');
           var tbdy = document.createElement('tbody');

           var tr1 = document.createElement('tr');
           var td11 = document.createElement('td');
           td11.width ='20%';
           td11.innerHTML = '版块名称';
           var td12 = document.createElement('td');
           td12.with = '80%';
           td12.innerHTML = '<input  type="text" name="boardName" style="width:60%;"/>';

           var tr2 = document.createElement('tr');
           var td21 = document.createElement('td');
           td21.width ='20%';
           td21.innerHTML = '版块简介';
           var td22 = document.createElement('td');
           td22.with = '80%';
           td22.innerHTML = '<textarea style="width:80%;height:120px"  name="boardDesc"></textarea>';

           var tr3 = document.createElement('tr');
           var td31 = document.createElement('td');
           td31.colSpan ='2';

           var input1 = document.createElement("input");
           input1.type = "submit";
           input1.value = "保存";
           var input2 = document.createElement("input");
           input2.type = "reset";
           input2.value = "重置";
           var input3 = document.createElement("input");
           input3.type = "hidden";
           input3.name = "_method";
           input3.value = "PUT";

           td31.appendChild(input1);
           td31.appendChild(input2);
           td31.appendChild(input3);

           body.appendChild(form1);
           form1.appendChild(tbl);
           tbl.appendChild(tbdy);

           tbdy.appendChild(tr1);
           tr1.appendChild(td11);
           tr1.appendChild(td12);
           tbdy.appendChild(tr2);
           tr2.appendChild(td21);
           tr2.appendChild(td22);
           tbdy.appendChild(tr3);
           tr3.appendChild(td31);
       }
   </script>
   <button onclick="createFormAndTable(test)">新建论坛版块</button>
   <!--
   <a href="<c:url value="/forum/setBoardManagerPage.html"/>">论坛版块管理员</a>&nbsp;&nbsp;
   -->
   <!-- /users patch-->
       <script type="text/javascript">
           function createFormAndTable2(text){
               var form1 = document.createElement("form");
               form1.action = "<c:url value="/users/managers" />";
               form1.method = "post";

               var body = document.getElementsByTagName('body')[0];
               var tbl = document.createElement('table');
               tbl.style.width = '100%';
               tbl.setAttribute('border', '1px');
               var tbdy = document.createElement('tbody');

               var tr1 = document.createElement('tr');
               var td11 = document.createElement('td');
               td11.width ='20%';
               td11.innerHTML = '论坛板块';
               var td12 = document.createElement('td');
               td12.with = '80%';
               //td12.innerHTML = '<select name="boardId"><option>请选择</option><c:forEach var="board" items="${boards}"><option value="${board.boardId}">${board.boardName}</option></c:forEach></select>';

               var obj = JSON.parse(text);
               var boards = obj.boards;
               //var boards = JSON.parse(text);
               var select1 = document.createElement('select');
               select1.name = 'boardId';
               var option1 = document.createElement('option');
               option1.innerHTML = '请选择';
               select1.appendChild(option1);

               for(var i=0;i<boards.length;i++){
                   var option2 = document.createElement('option');
                   option2.value = boards[i].boardId;
                   option2.innerHTML = boards[i].boardName;
                   select1.appendChild(option2);
               }
               td12.appendChild(select1);

               var tr2 = document.createElement('tr');
               var td21 = document.createElement('td');
               td21.width ='20%';
               td21.innerHTML = '用户';
               var td22 = document.createElement('td');
               td22.with = '80%';
               //td22.innerHTML = '<select name="userName"><option>请选择</option><c:forEach var="user" items="${users}"><option value="${user.userName}">${user.userName}</option></c:forEach></select>';

               var users = obj.users;
               var select2 = document.createElement('select');
               select2.name = 'userName';
               var option21 = document.createElement('option');
               option21.innerHTML = '请选择';
               select2.appendChild(option21);
               for(var i=0;i<users.length;i++){
                   var option22 = document.createElement('option');
                   option22.value = users[i];//.userName;
                   option22.innerHTML = users[i];//.userName;
                   select2.appendChild(option22);
               }
               td22.appendChild(select2);

               var tr3 = document.createElement('tr');
               var td31 = document.createElement('td');
               td31.colSpan ='2';

               var input1 = document.createElement("input");
               input1.type = "submit";
               input1.value = "保存";
               var input2 = document.createElement("input");
               input2.type = "reset";
               input2.value = "重置";
               var input3 = document.createElement("input");
               input3f
               //alert('1');

               td31.appendChild(input1);
               td31.appendChild(input2);
               td31.appendChild(input3);

               body.appendChild(form1);
               form1.appendChild(tbl);
               tbl.appendChild(tbdy);

               tbdy.appendChild(tr1);
               tr1.appendChild(td11);
               tr1.appendChild(td12);
               tbdy.appendChild(tr2);
               tr2.appendChild(td21);
               tr2.appendChild(td22);
               tbdy.appendChild(tr3);
               tr3.appendChild(td31);
           }
   </script>
   <script>
       function test() {
           var request = new XMLHttpRequest();
           //request.open('GET', '/forum/forum/setBoardManagerPage.json');
           request.open('GET', '<c:url value="/setBoardManagerPage.json"/>');
           request.setRequestHeader('Content-type','application/json');
           request.onreadystatechange = function () {
               if(request.readyState == 4) {
                   return createFormAndTable2(request.responseText);
               }
           };
           request.send();
       }
   </script>
   <button onclick="test()">论坛版块管理员</button><!--这里把同一个管理员重复设为会报错-->
   <!--
   <a href="<c:url value="/forum/userLockManagePage.html"/>">用户锁定/解锁</a>
   -->
   <!-- /users patch-->

   <script type="text/javascript">
       function createFormAndTable3(text){
           var form1 = document.createElement("form");
           form1.action = "<c:url value="/users/locks"/>";
           form1.method = "POST";

           var body = document.getElementsByTagName('body')[0];
           var tbl = document.createElement('table');
           tbl.style.width = '100%';
           tbl.setAttribute('border', '1px');
           var tbdy = document.createElement('tbody');

           var tr1 = document.createElement('tr');
           var td11 = document.createElement('td');
           td11.width ='20%';
           td11.innerHTML = '用户';
           var td12 = document.createElement('td');
           td12.with = '80%';

           var obj = JSON.parse(text);
           var users = obj.users;
           //var users = JSON.parse(text);
           var select1 = document.createElement('select');
           select1.name = 'userName';
           var option11 = document.createElement('option');
           option11.innerHTML = '请选择';
           select1.appendChild(option11);
           for(var i=0;i<users.length;i++){
               var option12 = document.createElement('option');
               option12.value = users[i];//.userName;
               option12.innerHTML = users[i];//.userName;
               select1.appendChild(option12);
           }
           td12.appendChild(select1);

           var tr2 = document.createElement('tr');
           var td21 = document.createElement('td');
           td21.width ='20%';
           td21.innerHTML = '锁定/解锁';
           var td22 = document.createElement('td');
           td22.with = '80%';
           var input3 = document.createElement('input');
           input3.type = "radio";
           input3.name = "locked";
           input3.value = "1";
           input3.innerHTML = "锁定";
           var input4 = document.createElement('input');
           input4.type = "radio";
           input4.name = "locked";
           input4.value = "0";
           input4.innerHTML = "解锁";
           td22.appendChild(input3);
           td22.appendChild(input4);

           var tr3 = document.createElement('tr');
           var td31 = document.createElement('td');
           td31.colSpan ='2';

           var input1 = document.createElement("input");
           input1.type = "submit";
           input1.value = "保存";
           var input2 = document.createElement("input");
           input2.type = "reset";
           input2.value = "重置";
           var input5 = document.createElement("input");
           input5.type = "hidden";
           input5.name = "_method";
           input5.value = "PATCH";

           //alert('1');

           td31.appendChild(input1);
           td31.appendChild(input2);
           td31.appendChild(input5);

           body.appendChild(form1);
           form1.appendChild(tbl);
           tbl.appendChild(tbdy);

           tbdy.appendChild(tr1);
           tr1.appendChild(td11);
           tr1.appendChild(td12);
           tbdy.appendChild(tr2);
           tr2.appendChild(td21);
           tr2.appendChild(td22);
           tbdy.appendChild(tr3);
           tr3.appendChild(td31);
       }
   </script>
   <script>
       function test2() {
           var request = new XMLHttpRequest();
           //request.open('GET', '/forum/forum/userLockManagePage.json');
           request.open('GET', '<c:url value="/userLockManagePage.json"/>');
           request.setRequestHeader('Content-type','application/json');
           request.onreadystatechange = function () {
               if(request.readyState == 4) {
                   return createFormAndTable3(request.responseText);
               }
           };
           request.send();
       }
   </script>
   <button onclick="test2()">用户锁定/解锁</button>
</c:if>
