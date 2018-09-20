<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>添加来账标识</title>
</head>
<body>
<form id="markForm">
    <input type="hidden" id="id" name="id" class="easyui-textbox form-input"/>
    <table>
        <tr>
            <td class="form-label">报文编号：</td>
            <td>
                <input id="msgCodeAdd" name="msgCode" class="easyui-textbox form-input" style="width: 250px;" readonly="readonly"/>
            </td>
        </tr>
        <tr>
            <td class="form-label">报文名称：</td>
            <td>
                <input id="msgNameAdd" name="msgName" class="easyui-textbox form-input" style="width: 250px;" readonly="readonly"/>
            </td>
        </tr>
        <tr>
            <td class="form-label">报文标准：</td>
            <td>
                <input class="easyui-combobox" id="standardAdd" name="standard" style="width: 250px"
                       valueField="id" textField="text" limitToList="true" url="${ctx}/generic/dict/STANDARD" panelHeight="auto"
                       readonly="readonly"/>
            </td>
        </tr>
        <tr>
            <td class="form-label">域路径：</td>
            <td>
                <input id="fieldIdAdd" name="fieldId" class="easyui-textbox form-input" style="width: 250px;" required/>
            </td>
        </tr>
    </table>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
           onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onCancel();">取消</a>
    </div>
</form>
<script>
    //编辑
    function setData(data){
        $("#markForm").form('load', data);
    }
    function getData(){
        var formData = {};
        formData.id = $("#id").textbox('getValue');
        formData.msgCode = $("#msgCodeAdd").textbox('getValue');
        formData.msgName = $("#msgNameAdd").textbox('getValue');
        formData.standard = $("#standardAdd").combobox('getValue');
        formData.fieldId = $("#fieldIdAdd").textbox('getValue');
        return formData;
    }
    function onOk() {
        var form = $('#markForm');
        var flag = form.form('validate');
        if (!flag) return;
        var formData = getData();
        $.messager.progress();
        $.ajax({
            url: '${ctx}/func/epcc/add',
            type: 'POST',
            data: formData,
            success: function(data){
                $.messager.progress('close');
                if(data.success){
                    showMsg("提示", "添加成功", true);
                    $('#dg').datagrid('reload');
                    onCancel();
                } else {
                    showMsg("提示", data.msg, true);
                }
            }
        })
    }
    function onCancel() {
        $('#window-mark-add-panel').window('close');
    }
</script>
</body>
</html>