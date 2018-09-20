<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="fieldQueryForm">
    <table>
        <tr>
            <td>域Id:</td>
            <td>
                <input class="easyui-textbox" type="text" id="fieldId" name="fieldId" style="width: 100px"
                       onenter="onSearch"/>
            </td>
            <td>域名称:</td>
            <td>
                <input class="easyui-textbox" type="text" id="nameZh" name="nameZh" style="width: 100px"
                       onenter="onSearch"/>
            </td>
            <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                   onclick="onMySearch();">查询</a></td>
        </tr>
    </table>
</form>
<div style="height: 300px">
<table id="dg-field-select"></table>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onSetOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onSetCancel();">取消</a>
</div>

<script>
    var data;
    function setData(param){
        data = param;
        $("#dg-field-select").datagrid({
            fit:true,
            title: '域列表',
            fitColumns: true,
            url:'${ctx}/func/exec/fieldList/'+param.messageId,
            height: 300,
            singleSelect:true,
            pagination: true,
            pageSize: 20,
            pageList: [10, 20, 30, 50],
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {field: 'field_id', title: '域Id', width: 100},
                {field: 'name_zh', title: '域名称', width: 100},
                {field: 'msgCode', title: '报文编号', width: 100}
            ]]
        });
    }

    function onMySearch(){
        var fieldId = $("#fieldId").textbox("getValue");
        var nameZh = $("#nameZh").textbox("getValue");
        var p = {};
        if (fieldId != null && fieldId != "") {
            p['fieldId'] = fieldId;
        }
        if (nameZh != null && nameZh != "") {
            p['nameZh'] = nameZh;
        }
        $("#dg-field-select").datagrid('load', p);
    }

    function onSetCancel(){
        $('#window-field-list').window('close');
    }
    function onSetOk(){
        var rows = $("#dg-field-select").datagrid('getSelections');
        if(rows.length == 0) {
            showMsg('提示','请选择域',true);
            return;
        }
        $('#window-expected-set').window({
            title: '预期值',
            width: 400,
            height: 150,
            href: '${ctx}/func/exec/addExcepted',
            modal: true,
            minimizable: false,
            maximizable: false,
            shadow: false,
            cache: false,
            closed: false,
            collapsible: false,
            resizable: false,
            loadingMessage: '正在加载数据，请稍等......',
            onLoad: function () {
                data.fieldId = rows[0].id;
                data.fieldIdName = rows[0].field_id;
                data.fieldName = rows[0].name_zh;
                setParamData(data)
            },
            onClose: function () {
                $("#dg-field-list").datagrid('reload');
            }
        });
        onSetCancel();
    }

</script>
