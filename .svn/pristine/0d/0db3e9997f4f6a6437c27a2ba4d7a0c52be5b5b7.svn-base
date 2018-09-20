<%--
  Created by IntelliJ IDEA.
  User: huangbo
  Date: 2016/5/9
  Time: 10:25
  To change this template use File | Settings | File Templates.
--%>
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
                height: $(window).height() - 48,
                fitColumns: true,
                singleSelect: false,
                url: '${ctx}/msg/charset/query',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'name', title: '字符集名称', width: 70},
                    {field: 'code', title: '字符集代号', width: 70},
                    {field: 'enum', title: '取值范围', width: 70},
                    {field: 'descript', title: '说明', width: 100}
                ]]
            });
        });
        function add() {
            showWindow("新增", "${ctx}/msg/charset/add", 280, 230, true, false, false);
        }
        function edit() {
            var rows = $("#dg").datagrid('getSelections');
            if (rows == null || rows.length != 1) {
                showMsg("提示", "请选择一条记录进行修改", true);
                return;
            }
            var row = $("#dg").datagrid('getSelected');
            if (row == null) {
                showMsg("提示", "请选择要修改的记录", true);
            } else {
                var url = "${ctx}/msg/charset/edit/" + row.id;
                showWindow("修改", url, 280, 230, true, false, false);
            }
        }
        function del() {
            var dgRow = $("#dg").datagrid('getSelected');

            if (!dgRow) {
                showMsg('提示', '请选择字符集', true);
            }
            else {
                var id = dgRow.id;
                if (showConfirm("提示", "确定要删除用户吗？", function () {
                            $.post("${ctx}/msg/charset/del", {id: id}, function (data) {
                                if (data.success) {
                                    $("#dg").datagrid('reload');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });
                        }));
            }
        }
    </script>
</head>
<body>
<div class="toolBar">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="add();">增加</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="del();">删除</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                   onclick="edit('update');">修改</a>
            </td>
        </tr>
    </table>
</div>
<table id="dg"></table>
<div id="dlg-fields"></div>
</body>
</html>

