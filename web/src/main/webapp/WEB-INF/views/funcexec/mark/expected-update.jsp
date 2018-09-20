<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>修改预期值</title>
</head>
<body>
<form id="expectedUpdateForm">
    <input id="expectedUpdateId" name="id" type="hidden" class="easyui-textbox form-input"/>
    <table>
        <tr>
            <td class="form-label">域ID：</td>
            <td>
                <input id="fieldIdname" name="fieldId" class="easyui-textbox form-input" disabled="disabled" style="width: 250px;" required vtype="maxLength:100"/>
            </td>
        </tr>
        <tr>
            <td class="form-label">域名称：</td>
            <td>
                <input id="nameZhname" name="nameZh" class="easyui-textbox form-input" disabled="disabled" style="width: 250px;" required vtype="maxLength:100"/>
            </td>
        </tr>
        <tr>
            <td class="form-label">预期值：</td>
            <td>
                <input id="expectedValuename" name="expectedValue" class="easyui-textbox form-input" style="width: 250px;" required vtype="maxLength:100"/>
            </td>
        </tr>
    </table>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
           onclick="onExpectedUpdateOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onExpectedUpdateCancel();">取消</a>
    </div>
</form>
<script>
    //修改功能测试时获取当前功能测试项信息
    function setData(data){
        $('#expectedUpdateForm').form('load', data);
    }
    function getData(){
        var formData = {};
        formData.id = $("#expectedUpdateId").textbox('getValue');
        formData.nameZh = $("#nameZhname").textbox('getValue');
        formData.fieldId = $("#fieldIdname").textbox('getValue');
        formData.expectedValue = $("#expectedValuename").textbox('getValue');
        return formData;
    }
    function onExpectedUpdateOk() {
        var form = $('#expectedUpdateForm');
        var flag = form.form('validate');
        if (!flag) return;
        var formData = getData();
        if(formData.id == '' || formData.id == null){
            var rows = $('#dg-expected-list').datagrid('getRows');
            for(var i=0; i<rows.length; i++){
                if(rows[i].fieldId == formData.fieldId){
                    rows[i].expectedValue = formData.expectedValue;
                    $('#dg-expected-list').datagrid('loadData', rows);
                    onExpectedUpdateCancel();
                    return;
                }
            }
        } else {
            $.messager.progress();
            $.ajax({
                url: '${ctx}/func/exec/updateExpected',
                type: 'post',
                data: {id: formData.id,expectedValue:formData.expectedValue},
                cache: false,
                async: false,
                success: function (data) {
                    $.messager.progress('close');
                    $("#dg-expected-list").datagrid('reload');
                    onExpectedUpdateCancel();
                }
            });
        }
    }
    function onExpectedUpdateCancel() {
        $('#window-field-list').window('close');
    }
</script>
</body>
</html>
