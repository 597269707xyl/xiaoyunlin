<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="id" value="${sm.id}"/>
<c:set var="type" value="${type}"/>
<c:if test="${type == 'xml'}">
    <c:if test="${sm.modelFileContent != ''}">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
           onclick="uploadFileContent();">下载</a>
    </c:if>
    <pre name="code" class="java"><c:out value="${sm.modelFileContent}" escapeXml="true"/></pre>
</c:if>
<c:if test="${type == 'schema'}">
    <c:if test="${sm.schemaFileContent != ''}">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
           onclick="uploadFileContent();">下载</a>
    </c:if>
    <pre name="code" class="java"><c:out value="${sm.schemaFileContent}" escapeXml="true"/></pre>
</c:if>
<script>
    function uploadFileContent(){
        var type = '${type}';
        location.href='${ctx}/sim/instance/message/uploadXml/${id}/'+type;
    }
</script>
