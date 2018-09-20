<%--
  批量执行缓冲时间（毫秒）
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<table align="center">
    <tr>
        <td>
            缓冲时间:
            <input id="sendInternal" class="easyui-textbox" style="width: 120px;">单位:秒
        </td>
    </tr>
</table>

</form>

<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
       onclick="onSendInternalOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
       onclick="onSendInternalCancel();">取消</a>
</div>
<script>
    var o;
    function setData(param) {
        o = param;
    }

    function onSendInternalOk() {
        var sendInternal = $("#sendInternal").textbox("getValue");
        if (sendInternal == '') {
            sendInternal = 0;
        }
        o['sendInternal'] = sendInternal;
        $.messager.progress();
        $.ajax({
            type: "POST",
            url: "${ctx}/func/usecase/execUsecaseProject",
            data: JSON.stringify(o),
            cache: false,
            async: true,
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                $.messager.progress('close');
                if (data.success) {
                    var m = '用例开始执行(' + data.data.execTime + ')</br>执行结果请通过功能菜单中的</br>"执行结果统计"查看';
                    showMsg("提示", m, true);
                    $("#usecaseList").datagrid('reload');
                } else {
                    showMsg("提示", data.msg, true);
                }
                onSendInternalCancel();
            }
        });
    }
    function onSendInternalCancel() {
        $('#myWindow').window('close');
    }
</script>