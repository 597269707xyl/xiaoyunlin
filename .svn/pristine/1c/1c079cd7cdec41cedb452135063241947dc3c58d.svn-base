<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="msglistForm">
<table>
    <tr>
        <td></td>
        <td>仿真实例名称:</td>
        <td>
            <input class="easyui-combobox" id="searchsimname" name="searchsimname"
                   style="width: 100px"
                   data-options="valueField:'id',textField:'text',limitToList:'true',url:'${ctx}/sim/instance/getAllSimSystemOrder?type=XML',panelHeight:'auto'"
                   onenter="onSearchMsg" required/>
        </td>
        <td>报文名称:</td>
        <td>
            <input class="easyui-textbox" type="text" id="searchname" name="searchname"
                   style="width: 100px"
                   onenter="onSearchMsg"/>
        </td>
    </tr>
    <tr>
        <td></td>
        <td>报文编码:</td>
        <td>
            <input class="easyui-textbox" type="text" id="searchmesgType" name="searchmesgType"
                   style="width: 100px"
                   onenter="onSearchMsg"/>
        </td>
        <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
               onclick="onSearchMsg();">查询</a></td>
    </tr>
</table>
</form>
<div style="height: 350px">
    <table id="dg-msg-list"></table>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
       onclick="onUsecaseDataMsgAddOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
       onclick="onUsecaseDataMsgAddCancel();">取消</a>
</div>
<script>
    $('#dg-msg-list').datagrid({
        fit: true,
        fitColumns: true,
        singleSelect: true,
        rownumbers: true,
        pagination: true,
        pageSize: 10,
        pageList: [10, 20, 30, 50],
        columns: [[
            {field: 'ck', checkbox: true},
            {field: 'id', title: 'id', hidden: true},
            {field: 'name', title: '报文名称', width: 100},
            {field: 'msgType', title: '报文编号', width: 100},
            {field: 'respMsgName', title: '应答报文名称', width: 100},
            {field: 'respMesgType', title: '应答报文编号', width: 100},
            {
                field: 'sysinstancename', title: '仿真实例名称', width: 100,
                formatter: function (value, row, index) {
                    return row.systemInstance.name;
                }
            },
            {field: 'standardDis', title: '报文标准', width: 40}
        ]]
    });
    function onSearchMsg() {
        var form = $('#msglistForm');
        var flag = form.form('validate');
        if (!flag) return;
        var name = $("#searchname").textbox("getValue");
        var mesgType = $("#searchmesgType").textbox("getValue");
        var systemInstance = $("#searchsimname").combobox("getValue");
        var p = {};
        if (name != null && name != "") {
            p['name|like'] = name;
        }
        if (mesgType != null && mesgType != "") {
            p['msgType|like'] = mesgType;
        }
        if (systemInstance != null && systemInstance != "") {
            p['systemInstance.id'] = systemInstance;
        }
        $("#dg-msg-list").datagrid({url: '${ctx}/sim/instance/message/query?type=XML', queryParams:p});
    }
    function onUsecaseDataMsgAddOk() {
        var row = $('#dg-msg-list').datagrid('getSelected');
        if(row){
            var data = {};
            data.msgName = row.name;
            data.msgCode = row.msgType;
            data.standard = row.standard;
            $('#window-mark-add-panel').window({
                title: '添加数据',
                width: 400,
                height:250,
                href: '${ctx}/func/epcc/addpage',
                modal: false,
                minimizable: false,
                maximizable: false,
                shadow: false,
                cache: false,
                closed: false,
                collapsible: false,
                resizable: true,
                loadingMessage: '正在加载数据，请稍等......',
                onLoad: function () {
                    setData(data);
                }
            });
            onUsecaseDataMsgAddCancel();
        } else {
            showMsg("提示", "请选择一条报文!", true);
        }
    }
    function onUsecaseDataMsgAddCancel() {
        closeWindow();
    }
</script>