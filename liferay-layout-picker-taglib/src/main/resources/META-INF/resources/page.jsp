<%@ include file="/init.jsp" %>

<%
Map<String, Object> props = (Map<String, Object>)request.getAttribute("layout-picker:select-layout:data");
%>

<div class="alert alert-primary" role="alert">
	<%= props.get("message") %>
</div>