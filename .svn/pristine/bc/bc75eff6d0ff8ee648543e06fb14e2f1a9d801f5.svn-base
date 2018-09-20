<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <link href="${ctx}/asset/css/style.css" rel="stylesheet" type="text/css"/>
    <script>
        $(function () {
            $('#dg').datagrid({
                //fit: true,
                height: $(window).height() - 48,
                fitColumns: true,
                url: '${ctx}/generic/commonQuery/role',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'name', title: '名称', width: 100},
                    {field: 'role', title: '标识', width: 100},
                    {field: 'notes', title: '描述', width: 100}
                ]]
            });
        });
        function onSearch() {
            var name = $("#name").textbox("getValue");
            var role = $("#role").textbox("getValue");
            var p = {};
            if (name != null && name != "") {
                p['name|like'] = name;
            }
            if (role != null && role != "") {
                p['role|like'] = role;
            }
            $("#dg").datagrid('load', p);
        }

        function addOrUpdate(type) {
            var title = "新增";
            var param = {action: type};
            if (type == 'update') {
                var rows = $("#dg").datagrid('getSelections');
                if (rows == null || rows.length != 1) {
                    showMsg("提示", "请选择一条记录进行修改", true);
                    return;
                }
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
            commonShowWindow(param, title, "${ctx}/sys/role/add", 300, 245, true, false, false);
        }

        function del() {
            var nodes = $("#dg").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            if (nodes && nodes.length > 0) {
                if (showConfirm("提示", "确定要删除用户吗？", function () {
                            $.post("${ctx}/sys/role/del", {ids: ids}, function (data) {
                                if (data.success) {
                                    $("#dg").datagrid('reload');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });

                        }));
            } else {
                showMsg("提示", "请选择要删除的用户", true);
            }
        }
        function allocFunc() {
            var rows = $("#dg").datagrid('getSelections');
            if (rows == null || rows.length != 1) {
                showMsg("提示", "请选择一条记录进行操作", true);
                return;
            }
            var row = $("#dg").datagrid('getSelected');
            if (row == null) {
                showMsg("提示", "请选择记录", true);
                return;
            }
            var param = {action: 'update'};
            param.id = row.id;
            commonShowWindow(param, "为角色指定功能", "${ctx}/sys/role/funAlloc", 300, 450, true, false, false);
        }
    </script>
</head>
<body>
<div class="toolBar">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                   onclick="addOrUpdate('add');">增加</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="del();">删除</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                   onclick="addOrUpdate('update');">修改</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="allocFunc();">指定功能</a>
            </td>
        </tr>
    </table>

</div>
<table id="dg"></table>
</body>
</html>
