<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Title</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <link href="${ctx}/asset/css/style.css" rel="stylesheet" type="text/css"/>
    <script>
        $(function () {
            $('#dg').datagrid({
                fit: true,
                fitColumns: true,
                singleSelect: true,
                rownumbers:true,
                url: '${ctx}/generic/commonQuery/msgDataType',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    //{field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'name', title: '数据类型名称', width: 100},
                    {field: 'code', title: '数据类型代号', width: 100},
                    {field: 'typeDis', title: '数据格式', width: 100},
                    {field: 'serialDis', title: '数据标准', width: 100},
                    {field: 'descript', title: '描述', width: 100}
                ]],
                onClickRow: function (index, row) {
                    reloadData('dataTypeEnum');
                }
            });
            $('#dg-fields').datagrid({
                fit: true,
                fitColumns: true,
                singleSelect: false,
                url: '${ctx}/msg/datatype/getDataEnum',
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'dataSetTypeDis', title: '数据类型', width: 100},
                    {field: 'generatRule', title: '数据生成规则', width: 200},
                    {field: 'descript', title: '说明', width: 200}
                ]]
            });
        });
        function onSearch() {
            var name = $("#name").textbox("getValue");
            var serial = $("#serial").combobox("getValue");
            var p = {};
            if (name != null && name != "") {
                p['name|like'] = name;
            }
            if (serial != null && serial != "") {
                p['serial'] = serial;
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
            $('#myWindow').window({
                title: title,
                width: 360,
                height: 270,
                href: '${ctx}/msg/datatype/add',
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
                    reloadData('dataType');
                }
            });
        }
        function reloadData(type) {
            if (type == 'dataType') {
                $("#dg").datagrid('reload');
                $("#dg-fields").datagrid('reload', {id: null});
            } else if (type == 'dataTypeEnum') {
                var dg = $("#dg");
                var row = dg.datagrid('getSelected');
                if (row) {
                    $("#dg-fields").datagrid('reload', {id: row.id});
                }

            }
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
                            $.post("${ctx}/msg/datatype/del", {ids: ids}, function (data) {
                                if (data.success) {
                                    reloadData('dataType');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });
                        }));
            } else {
                showMsg("提示", "请选择要删除的记录", true);
            }
        }
        function addDataTypeEnum() {
            var title = "新增";
            var dg = $("#dg");
            var dgComp = $("#dg-fields");
            var dgRow = dg.datagrid('getSelected');
            if (!dgRow) {
                showMsg('提示', '请选择数据类型', true);
                return;
            }
            $('#dlg-fields').window({
                title: title,
                width: 360,
                height: 300,
                href: '${ctx}/msg/datatype/addDataTypeEnum?id=' + dgRow.id,
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

                },
                onClose: function () {
                    reloadData('dataTypeEnum');
                }
            });
        }
        function delDataTypeEnum() {
            var nodes = $("#dg-fields").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            var dataTypeId = $("#dg").datagrid('getSelected').id;
            if (nodes && nodes.length > 0) {
                if (showConfirm("提示", "确定要删除吗？", function () {
                            $.post("${ctx}/msg/datatype/delDataTypeEnum", {
                                dataTypeId: dataTypeId,
                                ids: ids
                            }, function (data) {
                                if (data.success) {
                                    reloadData('dataTypeEnum');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });
                        }));
            } else {
                showMsg("提示", "请选择要删除的记录", true);
            }
        }
        function UpdateDataTypeEnum() {
            var rows = $("#dg-fields").datagrid('getSelections');
            if (rows == null || rows.length != 1) {
                showMsg("提示", "请选择一条记录进行修改", true);
                return;
            }
            var param = {};
            var row = $("#dg-fields").datagrid('getSelected');
            if (row == null) {
                showMsg("提示", "请选择要修改的记录", true);
                return;
            } else {
                $('#dlg-fields').window({
                    title: '修改',
                    width: 350,
                    height: 300,
                    href: '${ctx}/msg/datatype/updateDataTypeEnum',
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
                        setData(row.id);
                    },
                    onClose: function () {
                        reloadData('dataTypeEnum');
                    }
                });
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
                            <td>数据名称:</td>
                            <td>
                                <input class="easyui-textbox" type="text" id="name" name="name" style="width: 100px"
                                       onenter="onSearch"/>
                            </td>
                            <td>数据标准:</td>
                            <td>
                                <input class="easyui-combobox" id="serial" name="serial"
                                       data-options="valueField:'id',textField:'text',limitToList:'true',url:'${ctx}/generic/dict/SERIAL',panelHeight:'auto'"
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
    <div class="bottomContent" data-options="region:'center',title:'数据类型取值列表'">
        <div class="easyui-layout" data-options="fit:true">
            <div class="toolBar" data-options="region:'north'">
                <table style="width:100%;">
                    <tr>
                        <td style="width:100%;">
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                               onclick="addDataTypeEnum();">增加</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
                               onclick="delDataTypeEnum();">删除</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                               onclick="UpdateDataTypeEnum();">修改</a>
                        </td>
                    </tr>
                </table>
            </div>
            <div data-options="region:'center'">
                <table id="dg-fields"></table>
            </div>
        </div>
    </div>
</div>
<div id="dlg-fields"></div>
</body>
</html>
