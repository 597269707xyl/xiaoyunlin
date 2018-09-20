<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>新增功能测试项</title>
</head>
<body>
<form id="itemForm">
    <input id="id" name="id" type="hidden" class="easyui-textbox form-input"/>
    <input id="itemType" name="type" type="hidden" value="${type}" class="easyui-textbox form-input"/>
    <input id="mark" name="mark" type="hidden" value="send" class="easyui-textbox form-input"/>
    <table>
        <tr>
            <td class="form-label">名 称：</td>
            <td>
                <input id="name" name="name" class="easyui-textbox form-input" style="width: 250px;" required vtype="maxLength:100"/>
            </td>
        </tr>

        <tr>
            <td class="form-label">描述：</td>
            <td>
                <input id="descript" name="descript" class="easyui-textbox form-input"
                       data-options="multiline:true" style="height:60px;width: 250px;"/>
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
    //修改功能测试时获取当前功能测试项信息
    function setData(param){
        $.ajax({
            url: '${ctx}/func/exec/item/' + param.id,
            type: 'GET',
            cache: false,
            success: function(data){
                $('#itemForm').form('load', data);
            }
        });
    }
    function getData(){
        var formData = {};
        formData.id = $("#id").textbox('getValue');
        formData.mark = $("#mark").textbox('getValue');
        formData.name = $("#name").textbox('getValue');
        formData.descript = $("#descript").textbox('getValue');
        formData.type = $("#itemType").textbox('getValue');
        return formData;
    }
    var type = 'update';
    function onOk() {
        var form = $('#itemForm');
        var flag = form.form('validate');
        if (!flag) return;
        var formData = getData();
        var node = $('#projectTree').tree('getSelected');
        if(formData.id == '' || formData.id == null){
            type = 'add';
            if(node.type == 'project'){
                formData.projectId = node.id;
            } else {
                formData.itemId = node.id;
            }
        }
        $.messager.progress();
        $.ajax({
            url: '${ctx}/func/exec/itemAdd',
            type: 'post',
            data: formData,
            cache: false,
            async: false,
            success: function (data) {
                $.messager.progress('close');
                reloadTree(node, type);
                onCancel()
            }
        });
    }
    function onCancel() {
        closeWindow();
    }
</script>
</body>
</html>
