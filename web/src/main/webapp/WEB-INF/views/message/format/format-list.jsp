<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Title</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <script>
        $(function () {
            $('#dg').datagrid({
                fit: true,
                fitColumns: true,
                singleSelect: true,
                rownumbers:true,
                url: '${ctx}/generic/commonQuery/msgFormat',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    //{field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'name', title: '名称', width: 100},
                    {field: 'typeDis', title: '报文类型', width: 100},
                    {field: 'descript', title: '描述', width: 100}
                ]],
                onClickRow: function (index, row) {
                    reloadData('comps');
                }
            });
            $('#dg-comps').datagrid({
                fit: true,
                fitColumns: true,
                singleSelect: false,
                url: '${ctx}/msg/format/getComps',
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'typeCompDis', title: '类型', width: 100},
                    {field: 'fields', title: '协议格式', width: 100},
                    {field: 'seqNo', title: '序号', width: 100}
                ]]
            });
        });
        function reloadData(type) {
            if (type == 'format') {
                $("#dg").datagrid('reload');
                $("#dg-comps").datagrid('reload', {id: null});
            } else if (type == 'comps') {
                var dg = $("#dg");
                var row = dg.datagrid('getSelected');
                if (row) {
                    $("#dg-comps").datagrid('reload', {id: row.id});
                }

            }
        }
        function onSearch() {
            var name = $("#formatname").textbox("getValue");
            var msgType = $("#msgtype").combobox("getValue");
            var p = {};
            if (name != null && name != "") {
                p['name|like'] = name;
            }
            if (msgType != null && msgType != "") {
                p['type'] = msgType;
            }
            $("#dg").datagrid('load', p);
        }

        function addOrUpdate(type) {
            var title = "新增";
            var param = {action: type};
            if (type == 'update') {
                var row = $("#dg").datagrid('getSelected');
                if (row == null) {
                    showMsg("提示", "请选择要修改的记录", true);
                    return;
                } else {
                    param.id = row.id;
                    title = "编辑";
                }
            } else {
                title = "新增";
            }
            //commonShowWindow(param,title,"${ctx}/msg/format/add",400,300,true,false,false);
            $('#myWindow').window({
                title: title,
                width: 280,
                height: 230,
                href: '${ctx}/msg/format/add',
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
                    reloadData('format');
                }
            });
        }

        function del() {
            var nodes = $("#dg").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            if (nodes && nodes.length > 0) {
                if (showConfirm("提示", "确定要删除记录吗？", function () {
                            $.post("${ctx}/msg/format/del", {ids: ids}, function (data) {
                                if (data.success) {
                                    reloadData('format');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });

                        }));
            } else {
                showMsg("提示", "请选择要删除的记录", true);
            }
        }

        function addOrUpdateFormatComp(type) {
            var dg = $("#dg");
            var dgComp = $("#dg-comps");
            var dgRow = dg.datagrid('getSelected');
            var dgCompRow = dgComp.datagrid('getSelected');
            var param = {};
            if (type == 'add') {
                if (!dgRow) {
                    showMsg('提示', '请选择要修改的报文头', true);
                    return;
                }
                param.action = 'add';
            } else {
                var rows = dgComp.datagrid('getSelections');
                if (rows == null || rows.length != 1) {
                    showMsg("提示", "请选择一条记录进行修改", true);
                    return;
                }
                if (!dgCompRow) {
                    showMsg('提示', '请选择要修改的记录', true);
                    return;
                }
                param.action = 'update';
                param.id = dgCompRow.id;
            }
            $('#dlg-comps').window({
                title: '增加报文组成',
                width: 280,
                height: 230,
                href: '${ctx}/msg/format/addComp?id=' + dgRow.id,
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
                    if (param.action == 'update') {
                        setData(param);
                    }
                },
                onClose: function () {
                    reloadData('comps');
                }
            });
        }
        function delFormatComp() {
            var nodes = $("#dg-comps").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            var formatId = $("#dg").datagrid('getSelected').id;
            if (nodes && nodes.length > 0) {
                if (showConfirm("提示", "确定要删除吗？", function () {
                            $.post("${ctx}/msg/format/delComps", {formatId: formatId, ids: ids}, function (data) {
                                if (data.success) {
                                    reloadData('comps');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });
                        }));
            } else {
                showMsg("提示", "请选择要删除的记录", true);
            }
        }

    </script>
</head>
<body>
<div class="easyui-layout content" data-options="fit:true">
    <div class="topContent" data-options="region:'north',split:true" style="height:60%;">
        <div class="easyui-layout" data-options="fit:true">
            <div class="toolBar" data-options="region:'north'">
                <table style="width:100%;">
                    <tr>
                        <td style="width:100%;">
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                               onclick="addOrUpdate('add');">增加</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
                               onclick="del();">删除</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                               onclick="addOrUpdate('update');">修改</a>
                        </td>
                    </tr>
                </table>
                <form id="queryForm">
                    <table>
                        <tr>
                            <td>名称:</td>
                            <td>
                                <input class="easyui-textbox" type="text" id="formatname" name="formatname"
                                       style="width: 100px"
                                       onenter="onSearch"/>
                            </td>
                            <td>报文类型:</td>
                            <td>
                                <input class="easyui-combobox" id="msgtype" name="msgtype" style="width: 100px"
                                       valueField="id" textField="text" limitToList="true" panelHeight="auto" url="${ctx}/generic/dict/MSGTYPE"
                                       onenter="onSearch"/>
                            </td>
                            <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                                   onclick="onSearch();">查询</a></td>
                        </tr>
                    </table>
                </form>
            </div>
            <div data-options="region:'center'">
                <table id="dg"></table>
            </div>
        </div>
    </div>
    <div class="bottomContent" data-options="region:'center',title:'报文组成列表'">
        <div class="easyui-layout" data-options="fit:true">
            <div class="toolBar" data-options="region:'north'">
                <table style="width:100%;">
                    <tr>
                        <td style="width:100%;">
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                               onclick="addOrUpdateFormatComp('add');">增加</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
                               onclick="delFormatComp();">删除</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                               onclick="addOrUpdateFormatComp('update');">修改</a>
                        </td>
                    </tr>
                </table>
            </div>
            <div data-options="region:'center'">
                <table id="dg-comps"></table>
            </div>
        </div>
    </div>
</div>
<div id="dlg-comps"></div>
</body>
</html>
