<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/asset/js/jquery.form.min.js"></script>
<form id="fm" method="post" action="#">
    <input type="hidden" id="id" name="id"/>
    <div class="popup">
        <table class="single">
            <tr>
                <td>成员行:</td>
                <td>
                    <input class="easyui-combobox" id="itemId" name="itemId" style="width: 200px"
                           valueField="id" textField="name" limitToList="true" url="${ctx}/func/config/getAll" panelHeight="150"
                           required="true"/>
                </td>
            </tr>
            <tr>
                <td>名称:</td>
                <td><input class="easyui-textbox" type="text" id="variableZh" name="variableZh" style="width: 200px"/></td>
            </tr>
            <tr>
                <td>标识:</td>
                <td><input class="easyui-textbox" type="text" id="variableEn" name="variableEn" style="width: 200px"
                           required="true"/></td>
            </tr>
            <tr>
                <td>值:</td>
                <td><input class="easyui-textbox" type="text" id="variableValue" name="variableValue" style="width: 200px"/></td>
            </tr>
        </table>
    </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onCancel();">取消</a>
    </div>
</form>

<script>

    function setData(param){
        if(param != null){
            $.ajax({
                url: "${ctx}/func/config/get/" + param.id,
                success: function(data){
                    $("#fm").form("load", data);
                }
            });
        }
    }

    function getData() {
        var formData = {};
        formData.id = $("#id").val();
        formData.itemId = $("#itemId").combobox('getValue');
        formData.variableZh = $("#variableZh").textbox('getValue');
        formData.variableEn = $("#variableEn").textbox('getValue');
        formData.variableValue = $("#variableValue").textbox('getValue');
        return formData;
    }
    function onOk() {
        var form = $('#fm');
        var flag = form.form('validate');
        if (!flag) return;
        var formData = form.serialize();
        formData = getData();
        $.messager.progress();
        $.ajax({
            url: '${ctx}/func/config/add',
            type: 'post',
            data: formData,
            cache: false,
            async: false,
            success: function (data) {
                $.messager.progress('close');
                if(data.success){
                    showMsg("提示", data.msg, true);
                    closeWindow();
                    $("#dg").datagrid('reload');
                } else {
                    showMsg("提示", data.msg, true);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $.messager.progress('close');
                alert(XMLHttpRequest.responseText);
                closeWindow();
            }
        });
    }
    function onCancel() {
        closeWindow();
    }
</script>
