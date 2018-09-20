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
            var setType = '${setType}';
            var colName = setType == 'HEAD' ? '报文头' : '通用业务要素集';
            $('#dg').datagrid({
                fit: true,
                fitColumns: true,
                singleSelect: true,
                rownumbers:true,
                url: '${ctx}/generic/commonQuery/msgFieldSet?setType=' + '${setType}',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    //{field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'name', title: colName, width: 100},
                    {field: 'msgTypeDis', title: '报文类型', width: 100},
                    {field: 'descript', title: '描述', width: 100}
                ]],
                onClickRow: function (index, row) {
                    reloadData('fields');
                }
            });
            $('#dg-fields').datagrid({
                fit: true,
                fitColumns: true,
                singleSelect: false,
                url: '${ctx}/msg/fieldset/getFields',
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {
                        field: 'fieldId', title: '域id', width: 100,
                        formatter: function (value, row, index) {
                            if (row.field) {
                                return row.field.fieldId;
                            } else {
                                return value;
                            }
                        }
                    },
                    {
                        field: 'nameZh', title: '域名称', width: 100,
                        formatter: function (value, row, index) {
                            if (row.field) {
                                return row.field.nameZh;
                            } else {
                                return value;
                            }
                        }
                    },
                    {
                        field: 'moFlag', title: '必填', width: 100,
                        formatter: function (value, row, index) {
                            if (row.moFlag) {
                                return '是';
                            } else {
                                return '否';
                            }
                        }
                    },
                    {
                        field: 'fixFlag', title: '自动生成', width: 100,
                        formatter: function (value, row, index) {
                            if (row.fixFlag) {
                                return '否';
                            } else {
                                return '是';
                            }
                        }
                    },
                    {field: 'defaultValue', title: '默认值', width: 100}
                ]]
            });
        });
        function onSearch() {
            var name = $("#name").textbox("getValue");
            var msgType = $("#msgType").combobox("getValue");
            var p = {};
            if (name != null && name != "") {
                p['name|like'] = name;
            }
            if (msgType != null && msgType != "") {
                p['msgType'] = msgType;
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
                height: 210,
                href: '${ctx}/msg/fieldset/add?type=' + '${setType}',
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
                    reloadData('fieldset');
                }
            });
        }
        function reloadData(type) {
            if (type == 'fieldset') {
                $("#dg").datagrid('reload');
                $("#dg-fields").datagrid('reload', {id: null});
            } else if (type == 'fields') {
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
                            $.post("${ctx}/msg/fieldset/del", {ids: ids}, function (data) {
                                if (data.success) {
                                    reloadData('fieldset');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });
                        }));
            } else {
                showMsg("提示", "请选择要删除的记录", true);
            }
        }
        function addFieldSetComp() {
            var dg = $("#dg");
            var dgComp = $("#dg-fields");
            var dgRow = dg.datagrid('getSelected');
            if (!dgRow) {
                showMsg('提示', '请选择要增加域值的记录', true);
                return;
            }
                $('#dlg-fields').window({
                    title: '选择业务要素',
                    width: 480,
                    height: 430,
                    href: '${ctx}/msg/fieldset/fieldList?id=' + dgRow.id+'&setType=' + '${setType}'+'&msgType='+dgRow.msgType,
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
                        reloadData('fields');
                    }
                });

        }
        function delFieldSetComp() {
            var nodes = $("#dg-fields").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            var fieldSetId = $("#dg").datagrid('getSelected').id;
            if (nodes && nodes.length > 0) {
                if (showConfirm("提示", "确定要删除吗？", function () {
                            $.post("${ctx}/msg/fieldset/delFields", {
                                fieldSetId: fieldSetId,
                                ids: ids
                            }, function (data) {
                                if (data.success) {
                                    reloadData('fields');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });
                        }));
            } else {
                showMsg("提示", "请选择要删除的记录", true);
            }
        }
        function updateFieldSetComp() {
            var rows = $("#dg-fields").datagrid('getSelections');
            if (rows == null || rows.length != 1) {
                showMsg("提示", "请选择一条记录进行修改", true);
                return;
            }
            var row = $("#dg-fields").datagrid('getSelected');
            if (row == null) {
                showMsg("提示", "请选择要修改的记录", true);
                return;
            } else {
                $('#dlg-fields').window({
                    title: '修改',
                    width: 280,
                    height: 230,
                    href: '${ctx}/msg/fieldset/field/edit',
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
                        reloadData('fields');
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
                            <td>名称:</td>
                            <td>
                                <input class="easyui-textbox" type="text" id="name" name="name" style="width: 100px"
                                       onenter="onSearch"/>
                            </td>
                            <td>报文类型:</td>
                            <td>
                                <input class="easyui-combobox" id="msgType" name="msgType" style="width: 100px"
                                       data-options="valueField:'id',textField:'text',limitToList:'true',url:'${ctx}/generic/dict/MSGTYPE',panelHeight:'auto'"
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
    <div class="bottomContent" data-options="region:'center',title:'业务要素列表'">
        <div class="easyui-layout" data-options="fit:true">
            <div class="toolBar"  data-options="region:'north'">
                <table style="width:100%;">
                    <tr>
                        <td style="width:100%;">
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                               onclick="addFieldSetComp();">增加</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
                               onclick="delFieldSetComp();">删除</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                               onclick="updateFieldSetComp();">修改</a>
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
