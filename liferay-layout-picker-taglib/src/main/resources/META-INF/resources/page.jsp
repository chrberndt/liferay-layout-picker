<%@ include file="/init.jsp" %>

<div>
	<react:component
		module="js/CustomSelectLayout.es"
		props='<%= (Map<String, Object>)request.getAttribute("layout-picker:select-layout:data") %>'
	/>
</div>