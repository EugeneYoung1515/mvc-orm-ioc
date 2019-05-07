<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="baobaotao" tagdir="/WEB-INF/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>论坛版块页面</title>
	</head>
	<body>
	<%@ include file="includeTop.jsp"%>
	<script src="http://code.jquery.com/jquery.js"></script>
	<script>
        (function ($) {
            $.fn.extend({
                restfulizer: function (options) {
                    var defaults = $.extend({
                        parse: false,
                        target: null,
                        method: "POST"
                    }, options);

                    return $(this).each(function () {
                        var options = $.extend({}, defaults);
                        var self = $(this);

                        // Try to get data-param into options
                        if (typeof(self.attr('data-method')) != "undefined") {
                            options.method = self.attr('data-method').toUpperCase();
                        }

                        if (typeof(self.attr('href')) != "undefined") {
                            options.target = self.attr('href');
                        }

                        // Parse href parameters and create an input for each parameter
                        var inputs = "";

                        if (options.parse) {
                            var paramsIndex = options.target.indexOf("?");
                            var hasParams = (paramsIndex > -1);

                            if (hasParams) {
                                var params = options.target.substr(paramsIndex + 1).split('#')[0].split('&');
                                options.target = options.target.substr(0, paramsIndex);

                                for (var i = 0; i < params.length; i++) {
                                    var pair = params[i].split('=');
                                    inputs += "	<input type='hidden' name='" + decodeURIComponent(pair[0]) + "' value='" + decodeURIComponent(pair[1]) + "'>\n";
                                }
                            }
                        }

                        if (options.method == 'GET' || options.method == 'POST') {
                            var form = "\n" +
                                "<form action='" + options.target + "' method='" + options.method + "' style='display:none'>\n" +
                                inputs +
                                "</form>\n";
                        }
                        else {
                            var form = "\n" +
                                "<form action='" + options.target + "' method='POST' style='display:none'>\n" +
                                "	<input type='hidden' name='_method' value='" + options.method + "'>\n" +
                                inputs +
                                "</form>\n";
                        }

                        self.append(form)
                            .removeAttr('href')
                            .attr('style', 'cursor:pointer;')
                            .attr('onclick', '$(this).find("form").submit();');
                    });
                }
            });
        })(jQuery);
	</script>
	<div>
		<table border="1px" width="100%">
			<c:set var="isboardManager" value="${false}" />
			<c:forEach items="${USER_CONTEXT.manBoards}" var="manBoard">
				<c:if test="${manBoard.boardId == board.boardId}">
					<c:set var="isboardManager" value="${true}" />
				</c:if>
			</c:forEach>
			<tr>
			   <c:if test="${USER_CONTEXT.userType == 2 || isboardManager}">
			     <td></td>
			   </c:if>
				<td bgcolor="#EEEEEE">
					${board.boardName}
				</td>
					<!--/boards/id/topics post-->
				<td colspan="4" bgcolor="#EEEEEE" align="right">
					<a href="<c:url value="/boards/${board.boardId}/topics"/>" data-method="POST" class="rest">发表新话题</a>
				</td>
			</tr>
			<tr>
			   <c:if test="${USER_CONTEXT.userType == 2 || isboardManager}">
			     <td>
			        <script>
			            function switchSelectBox(){
			                var selectBoxs = document.all("topicIds");//这里能all topicIds 是因为下面有name是topicIds的元素
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
			        <input type="checkbox" onclick="switchSelectBox()"/><!-- 和下面的标题同行的-->
			     </td>
			   </c:if>
				<td width="50%">
					标题
				</td>
				<td width="10%">
					发表人
				</td>
				<td width="10%">
					回复数
				</td>
				<td width="15%">
					发表时间
				</td>
				<td width="15%">
					最后回复时间
				</td>
			</tr>
			<c:set var="isboardManager" value="${false}" />
			<c:forEach items="${USER_CONTEXT.manBoards}" var="manBoard">
				<c:if test="${manBoard.boardId == board.boardId}">
					<c:set var="isboardManager" value="${true}" />
				</c:if>
			</c:forEach> 
			<c:forEach var="topic" items="${pagedTopic.result}">
				<tr>
				    <c:if test="${USER_CONTEXT.userType == 2 || isboardManager}">
			          <td><input type="checkbox" name="topicIds" value="${topic.topicId}"/></td>
			        </c:if>
					<td>
						<a  href="<c:url value="/boards/${board.boardId}/topics/${topic.topicId}"/>">
							<!--/boards/id/topics/id get-->
							<c:if test="${topic.digest > 0}">
							  <font color=red>★</font>
							</c:if>
							${topic.topicTitle} 
							</a>

					</td>
					<td>
						${topic.user.userName}
						<br>
						<br>
					</td>
					<td>
						${topic.replies}
						<br>
						<br>
					</td>
					<td>
						<fmt:formatDate pattern="yyyy-MM-dd HH:mm"
							value="${topic.createTime}" />
					</td>
					<td>
						<fmt:formatDate pattern="yyyy-MM-dd HH:mm"
							value="${topic.lastPost}" />
					</td>
				</tr>
			</c:forEach>
		</table>
		</div>
		<baobaotao:PageBar
			   pageUrl="/boards/${board.boardId}"
			   pageAttrKey="pagedTopic"/>
	    <c:if test="${USER_CONTEXT.userType == 2 || isboardManager}">
	         <script>
	            function getSelectedTopicIds(){
	                var selectBoxs = document.all("topicIds");
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
	            function deleteTopics(){
	               var ids = getSelectedTopicIds();
	               if(ids){
	                  var url = "<c:url value="/boards/"/>${board.boardId}/topics/"+ids;
	                  //alert(url);
	                  //location.href = url;

					   var method = "DELETE";
					   autosubmit(url,method);
	               }
	            }
	            // /boards/id/topics/id delete

                function autosubmit(url,method) {
                    var form = document.createElement("form");
                    document.body.appendChild(form);
                    form.method = "POST";
                    form.action = url;
                    var element1 = document.createElement("INPUT");
                    element1.name="_method"
                    element1.value = method;
                    element1.type = 'hidden'
                    form.appendChild(element1);
                    form.submit();
                }//直接从listAllBoards.jsp里复制过来的

                function setDefinedTopis(){
	               var ids = getSelectedTopicIds();
	               if(ids){
	                  //var url = "<c:url value="/board/makeDigestTopic.html"/>?topicIds="+ids+"&boardId=${board.boardId}";
                       var url = "<c:url value="/boards/"/>${board.boardId}/topics/"+ids;
	                  //location.href = url;
					   var method = "PATCH";
					   autosubmit(url,method);
	               }	            
	            }// /boards/id/topics/id patch
	         </script>
			<input type="button" value="删除" onclick="deleteTopics()">
			<input type="button" value="置精华帖" onclick="setDefinedTopis()">
		</c:if>
	<script>
        //$(function () {//$(".rest").restfulizer();
        //alert('1');});
        $(document).on('ready', function () {$(".rest").restfulizer();
        //alert('1');
        });
	</script>
	</body>
</html>
