<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" class="easyui-form" method="post">
    <input type="hidden" id="id" name="id"/>
    <div class="popup">
    <table>
        <tr>
            <td class="popup_label">域id:</td>
            <td class="popup_input"><input class="easyui-textbox" id="fieldId" name="fieldId" required="true"/></td>
            <td class="popup_label">域英文名称:</td>
            <td class="popup_input"><input class="easyui-textbox" id="nameEn" name="nameEn" required="true"/></td>
        </tr>
        <tr>
            <td class="popup_label">域名称:</td>
            <td class="popup_input"><input class="easyui-textbox" id="nameZh" name="nameZh" required="true"/></td>
            <td  class="popup_label">数据类型:</td>
            <td class="popup_input"><input id="dataType" name="dataType" required="true"></td>
        </tr>
        <tr>
            <td class="popup_label">起始位置:</td>
            <td class="popup_input"><input  class="easyui-textbox" id="startPos" name="startPos"/></td>
            <td class="popup_label">长度:</td>
            <td class="popup_input"><input class="easyui-textbox" id="length" name="length"/></td>
        </tr>
        <tr>
            <td class="popup_label">长度的长度:</td>
            <td class="popup_input"><input class="easyui-textbox" id="lengthLength" name="lengthLength" /></td>
            <td class="popup_label">默认值:</td>
            <td class="popup_input"><input class="easyui-textbox" id="defaultValue" name="defaultValue"/></td>
        </tr>
        <tr><td class="popup_label">域类型:</td>
            <td class="popup_input"><input id="fieldType" name="fieldType" required="true" ></td>
            <td class="popup_label">报文类型:</td>
            <td class="popup_input"><input id="msgType" name="msgType" required="true" ></td>
        </tr>
    </table>
        </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onCancel();">取消</a>
    </div>
</form>
<script>
    $("#dataType").combogrid({
        panelWidth: 200,
        idField: 'id',
        textField: 'code',
        url: '${ctx}/msg/datatype/getAll',
        method: 'get',
        columns: [[
            {field:'id',title:'id', hidden: true},
            {field:'code',title:'数据类型',width:80},
            {field:'serialDis',title:'数据类型标准',width:120}
        ]],
        fitColumns: true
    });
    $("#fieldType").combobox({
        url:'${ctx}/generic/dict/FIELDTYPE',
        valueField:'id',
        textField:'text',
        panelHeight:'auto',
        limitToList:true
    });
    $("#msgType").combobox({
        url:'${ctx}/generic/dict/MSGTYPE',
        valueField:'id',
        textField:'text',
        panelHeight:'auto',
        limitToList:true
    });
    function setData(param){
        if (param.action == 'update'){
            $.ajax({
                url: "${ctx}/msg/field/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load',vo);
                }
            });
        }
    }
    function getData(){
        var formData = {};
        formData.dataTypeId = $('#dataType').combogrid('getValue');
        formData.id = $('#id').val();
        formData.nameEn = $('#nameEn').textbox('getValue');
        formData.nameZh = $('#nameZh').textbox('getValue');
        formData.fieldId = $('#fieldId').textbox('getValue');
        formData.startPos = $('#startPos').textbox('getValue');
        formData.length = $('#length').textbox('getValue');
        formData.lengthLength = $('#lengthLength').textbox('getValue');
        formData.defaultValue = $('#defaultValue').textbox('getValue');
        formData.fieldType = $('#fieldType').combobox('getValue');
        formData.msgType = $('#msgType').combobox('getValue');
        return formData;
    }
    function onOk() {
        commonSubmitForm('${ctx}/msg/field/add');
    }
    function onCancel() {
        closeWindow();
    }
</script>
