<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="id" value="${sm.id}"/>
<c:set var="type" value="${type}"/>
<c:if test="${type == 'xml'}">
    <%--<c:if test="${sm.modelFileContent != ''}">--%>
        <%--<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"--%>
           <%--onclick="uploadFileContent();">下载</a>--%>
    <%--</c:if>--%>
    <%--<pre name="code" class="java"><c:out value="${sm.modelFileContent}" escapeXml="true"/></pre>--%>
    <textarea id="modelFileContent" style="width: 100%; height: 90%">
        <c:out value="${sm.modelFileContent}" escapeXml="true"/>
    </textarea>
    <div class="buttonBar">
    <a href="#" style="text-align: center" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
       onclick="updateFileContent();">确定</a>
        <a href="#" style="text-align: center" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="updateFileContentCancel();">取消</a>
    </div>
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
        location.href='${ctx}/sim/message/uploadXml/${id}/'+type;
    }
    function updateFileContentCancel(){
        closeWindow();
    }

    function updateFileContent(){
        var msg = $("#modelFileContent").val();
        if(msg == ''){
            showMsg('提示', '请添加报文模板!', true);
            return;
        }
        $.messager.progress();
        $.ajax({
            url: '${ctx}/sim/message/updateBaseMsg',
            type: 'post',
            data: {id:'${sm.id}', msg:msg},
            cache: false,
            async: false,
            success: function (text) {
                $.messager.progress('close');
                if(text.success){
                    closeWindow();
                    showMsg("提示", "修改报文模板成功", true);
                } else {
                    showMsg("提示", text.msg, true);
                }
            }
        });
    }
</script>
