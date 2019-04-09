<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>论坛首页</title>
</head>
<body>
<%@ include file="includeTop.jsp" %>
<table border="1px" width="100%">
	<tr>
		<td colspan="4" bgcolor="#EEEEEE">所有论坛版块</td>
	</tr>
	<tr>
	<tr>
			   <c:if test="${USER_CONTEXT.userType == 2}">
			     <td>
			        <script>
			            function switchSelectBox(){
			                var selectBoxs = document.all("boardIds");
			                if(!selectBoxs) return ;
			                if(typeof(selectBoxs.length) == "undefined"){//only one checkbox
			                    selectBoxs.checked = event.srcElement.checked;
			                }else{//many checkbox ,so is a array 
			                  for(var i = 0 ; i < selectBoxs.length ; i++){
			                     selectBoxs[i].checked = event.srcElement.checked;
			                  }
			                } 
			            }
			        </script>
			        <input type="checkbox" onclick="switchSelectBox()"/>
			     </td><!-- 这个勾选框是和 板块名称同行的勾选框 表示全选 -->
			   </c:if>
		<td width="20%">版块名称</td>
		<td width="70%">版块简介</td>
		<td width="10%">主题帖数</td>
	</tr>
	<c:forEach var="board" items="${boards}"><!-- 集合里每一项设为board -->
		<tr>
	       	<c:if test="${USER_CONTEXT.userType == 2}">
			          <td><input type="checkbox" name="boardIds" value="${board.boardId}"/></td>
				<!-- 这个勾选框是每个板块响应的勾选框-->
			 </c:if>
			<td><a href="<c:url value="/boards/${board.boardId}"/>">${board.boardName}</a></td>
			<!--/boards/id -->
			<td>${board.boardDesc}</td>
			<td>${board.topicNum}</td>
		</tr>
	</c:forEach>
</table>
	    <c:if test="${USER_CONTEXT.userType == 2 || isboardManager}">
	         <script>
	            function getSelectedBoardIds(){
	                var selectBoxs = document.all("boardIds");
	                if(!selectBoxs) return null;
	                if(typeof(selectBoxs.length) == "undefined" && selectBoxs.checked){   
	                    return selectBoxs.value;
	                }else{//many checkbox ,so is a array 
	                  var ids = "";
	                  var split = ""
	                  for(var i = 0 ; i < selectBoxs.length ; i++){
	                     if(selectBoxs[i].checked){
	                        ids += split+selectBoxs[i].value;
	                        split = ",";
	                     }
	                  }
	                  return ids;
	                }
	            }
                function autosubmit(url) {
                    var form = document.createElement("form");
                    document.body.appendChild(form);
                    form.method = "POST";
                    form.action = url;
                    var element1 = document.createElement("INPUT");
                    element1.name="_method"
                    element1.value = "DELETE";
                    element1.type = 'hidden'
                    form.appendChild(element1);
                    form.submit();
                }
	            function deleteBoards(){
	               var ids = getSelectedBoardIds();
	               if(ids){
	                  var url = "<c:url value="/boards/"/>"+ids+"";
	                  //把上面的board改成Board就对了
	                  //alert(url);
	                  //location.href = url;
                       autosubmit(url);
	               }
	            }
	           
	         </script>
			<!--/boards/ids delete-->
			<input type="button" value="删除" onclick="deleteBoards()">
		</c:if>
</body>
</html>
