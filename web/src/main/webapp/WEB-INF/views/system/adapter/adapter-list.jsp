<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Title</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
</head>
<body>
<div class="toolBar">
    <div id="mm" style="width:100px;">
        <div data-options="iconCls:'icon-add'" onclick="addOrUpdate('add','MQ');">MQ适配器</div>
        <div data-options="iconCls:'icon-add'" onclick="addOrUpdate('add','TCP');">tcp适配器</div>
        <div data-options="iconCls:'icon-add'" onclick="addOrUpdate('add','HTTP');">http适配器</div>
    </div>
    <table>
        <tr>
            <td></td>
            <td>
                <a href="#" class="easyui-splitbutton" data-options="menu:'#mm',iconCls:'icon-add'">增加</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="del();">删除</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="addOrUpdate('update','mq');">修改</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="copyAdapter();">复制适配器</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="setSysinsConf();">适配器参数</a>
            </td>
            <td>
                <div class="datagrid-btn-separator"></div>
            </td>
            <td>适配器名称:</td>
            <td>
                <input class="easyui-textbox" type="text" id="name" name="name" style="width: 100px"
                       onenter="onSearch"/>
            </td>
            <td>适配器类型:</td>
            <td>
                <input class="easyui-combobox" type="text" id="adapterType" name="adapterType" style="width: 100px"
                       data-options="valueField: 'label',textField: 'value',panelHeight:'auto',icons:[{iconCls:'icon-clear',handler: function(e){$(e.data.target).combobox('clear');}}],
                           data: [{label: 'MQ',value: 'MQ适配器'},{label: 'TCP',value: 'TCP适配器'},{label: 'HTTP',value: 'HTTP适配器'}]"
                       onenter="onSearch"/>
            </td>
            <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                   onclick="onSearch();">查询</a></td>
        </tr>
    </table>
</div>
<div style="height: auto">
    <table id="dg"></table>
</div>
<div id="window-paramset"></div>
<div id="window-set-param"></div>
<div id="win"></div>
<script>
    $(function () {
        $('#dg').datagrid({
            height: $(window).height() - 80,
            fitColumns: true,
            url: '${ctx}/sys/adapter/query',
            pagination: true,
            pageSize: 20,
            pageList: [10, 20, 30, 50],
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {field: 'name', title: '适配器名称', width: 70},
                {field: 'adapterType', title: '适配器类型', width: 70},
                {field: 'description', title: '描述', width: 70}
            ]]
        });
    });

    function onSearch() {
        var name = $("#name").textbox("getValue");
        var adapterType = $("#adapterType").combobox("getValue");
        var p = {};
        if ((name != null && name != "" ) || ( adapterType != null && adapterType != "")) {
            p['name|like'] = name;
            p['adapterType|like'] = adapterType;
        }

        $("#dg").datagrid('load', p);
    }

    function add() {
        showWindow("新增", "${ctx}/sys/adapter/add", 320, 300, true, false, false);
    }

    function del() {
        var nodes = $("#dg").datagrid('getSelections'), ids = new Array();
        if (nodes && nodes.length > 0) {
            for (var i = 0; i < nodes.length; i++) {
                ids.push(nodes[i].id);
            }
        }
        if (nodes && nodes.length > 0) {
            if (showConfirm("提示", "确定要删除适配器吗？", function () {
                        $.post("${ctx}/sys/adapter/del", {ids: ids}, function (data) {
                            if (data.success) {
                                $("#dg").datagrid('reload');
                            } else {
                                showMsg("提示", "操作出错", true);
                            }
                        });

                    }));
        } else {
            showMsg("提示", "请选择要删除的适配器", true);
        }
    }

    function setSysinsConf() {
        var rows = $("#dg").datagrid('getSelections');
        if (rows == null || rows.length != 1) {
            showMsg("提示", "请选择一个适配器", true);
            return;
        }
        var row = $("#dg").datagrid('getSelected');
        if (row == null) {
            showMsg("提示", "请选择要设置参数的适配器", true);
            return;
        }
        var param = {};
        param.id = row.id;
        $('#myWindow').window({
            title: "设置适配器参数",
            width: 530,
            height: 430,
            href: '${ctx}/sim/instance/paramList',
            modal: true,
            minimizable: false,
            maximizable: false,
            shadow: false,
            cache: false,
            closed: false,
            collapsible: false,
            resizable: true,
            loadingMessage: '正在加载数据，请稍等......',
            onLoad: function () {
                setData(param)
            },
            onClose: function () {
            }
        });
    }

    function addOrUpdate(type, systemId) {
        var param = {action: type, systemid: systemId};
        var title;
        if (type == 'update') {
            var rows = $("#dg").datagrid('getSelections');
            if (rows.length == 0) {
                showMsg("提示", "请选择要修改的适配器", true);
                return;
            } else if (rows.length > 1) {
                showMsg("提示", "请选择单条适配器进行修改", true);
                return;
            } else {
                var row = $("#dg").datagrid('getSelected');
                if (row) {
                    param.id = row.id;
                    param.systemid = row.adapterType;
                }
                title = "修改适配器（类型：" + param.systemid + "）";
            }


        } else {
            title = "新增适配器（类型：" + systemId + "）";
        }
        if (param.systemid == 'HTTP') {
            commonShowWindow(param, title, "${ctx}/sys/adapter/add/" + param.systemid, 320, 260, true, false, false);
        } else if (param.systemid == 'TCP') {
            commonShowWindow(param, title, "${ctx}/sys/adapter/add/" + param.systemid, 640, 260, true, false, false);
        } else {
            commonShowWindow(param, title, "${ctx}/sys/adapter/add/" + param.systemid, 620, 500, true, false, false);
        }
    }

    function copyAdapter(){
        var nodes = $("#dg").datagrid('getSelections'), ids = new Array();
        if (nodes && nodes.length > 0) {
            for (var i = 0; i < nodes.length; i++) {
                ids.push(nodes[i].id);
            }
        }
        if (nodes && nodes.length > 0) {
            if (showConfirm("提示", "确定要复制所有适配器吗？", function () {
                        $.post("${ctx}/sys/adapter/copy", {ids: ids}, function (data) {
                            if (data.success) {
                                $("#dg").datagrid('reload');
                            } else {
                                showMsg("提示", "操作出错", true);
                            }
                        });

                    }));
        } else {
            showMsg("提示", "请选择要复制的适配器", true);
        }
    }
</script>
</body>
</html>
