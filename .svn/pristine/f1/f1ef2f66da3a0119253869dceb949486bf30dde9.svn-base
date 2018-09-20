<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/asset/js/jquery.form.min.js"></script>
<form method="post" action="#">
    <div class="popup">
        <table class="single">
            <tr>
                <td>测试项:</td>
                <td>
                    <input class="easyui-combobox" id="itemId" name="itemId" style="width: 200px"
                           valueField="id" textField="name" limitToList="true" url="${ctx}/func/config/getAll" panelHeight="150"
                           required="true"/>
                </td>
            </tr>
        </table>
    </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onDownLoadOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onDownLoadCancel();">取消</a>
    </div>
</form>

<script>

    function onDownLoadOk() {
        var form = $('#fm');
        var flag = form.form('validate');
        if (!flag) return;
        var itemId =  $("#itemId").combobox('getValue');
        if(itemId==''){
            showMsg("提示", "请选择一条测试项", true);
        }
        location.href = "${ctx}/func/config/download?itemId=" + itemId;
        closeWindow();
    }
    function onDownLoadCancel() {
        closeWindow();
    }
</script>
