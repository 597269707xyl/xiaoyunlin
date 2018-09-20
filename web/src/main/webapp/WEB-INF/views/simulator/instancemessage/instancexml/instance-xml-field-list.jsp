<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="form-field-select" method="post">
    <form id="queryForm">
        <table>
            <tr>
                <td>域id:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="searchFieldId" name="searchFieldId" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>域名称:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="searchFieldName" name="searchFieldName" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>
            </tr>
        </table>
    </form>
    <table id="dg-fields-select"></table>
    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onSelOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onSelCancel();">取消</a>
    </div>
</form>

<script>
    $("#dg-fields-select").datagrid({
        fitColumns: true,
        url:'${ctx}/generic/commonQuery/msgField?msgType=XML',
        height: 320,
        pagination: true,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
        columns: [[
            {field: 'ck', checkbox: true},
            {field: 'id', title: 'id', hidden: true},
            {field: 'fieldId', title: '域id', width: 100},
            {field: 'nameZh', title: '域名称', width: 100},
        ]]
    });
    function onSearch(){
        var fieldId = $("#searchFieldId").textbox("getValue");
        var nameZh = $("#searchFieldName").textbox("getValue");
        var p = {};
        if (fieldId != null && fieldId != "") {
            p['fieldId|like'] = fieldId;
        }
        if (nameZh != null && nameZh != "") {
            p['nameZh|like'] = nameZh;
        }
        $("#dg-fields-select").datagrid('load', p);
    }
    function onSelOk(){
        var rows = $("#dg-fields-select").datagrid('getSelections');
        var ids = new Array();
        if(rows && rows.length>0){
            for(var i= 0;i<rows.length;i++){
                ids.push(rows[i].id);
            }
        }
        var simMessageid = "${simMessageid}";
        $.post("${ctx}/sim/instance/message/addFields", {simMessageid: simMessageid, ids: ids}, function (data) {
            if (data.success) {
                onSelCancel();
            } else {
                showMsg("提示","操作失败",true);
            }
        });
    }

    function onSelCancel(){
        $('#dlg-fields').window('close');
    }
</script>
