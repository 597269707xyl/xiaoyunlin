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
                height: $(window).height() - 80,
                fitColumns: true,
                singleSelect: false,
                url: '${ctx}/generic/commonQuery/backup',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'creator', title: '备份人', width: 100},
                    {field: 'fileName', title: '文件名称', width: 100},
                    {field: 'createTime', title: '备份时间', width: 100}
                ]]
            });
        });
        function backup() {
            $.messager.progress({
                title : '数据备份',
                msg : '数据库备份中，请不要关闭页面。'
            });
            $.post("${ctx}/sys/backup/backupDb", function (data) {
                $.messager.progress('close');
                if (data.success) {
                    $("#dg").datagrid('reload');
                    showMsg("提示", data.msg, true);
                } else {
                    showMsg("提示", data.msg, true);
                }
            });
        };
        function restore() {
            var dgRows = $("#dg").datagrid('getSelections');
            if (dgRows == null || dgRows.length != 1) {
                showMsg('提示', '请选择一个备份文件', true);
                return;
            }
            var dgRow = dgRows[0];
            var id = dgRow.id;
            if (showConfirm("提示", "是否用所选备份文件恢复数据库？", function () {
                        $.messager.progress({
                            title : '数据恢复',
                            msg : '数据库恢复中，请不要关闭页面。'
                        });
                        $.post("${ctx}/sys/backup/restoreDb", {id: id}, function (data) {
                            $.messager.progress('close');
                            if (data.success) {
                                $("#dg").datagrid('reload');
                            } else {
                                showMsg("提示", data.msg, true);
                            }
                        });
                    }));
        }
        function del(){
            var rows = $("#dg").datagrid('getSelections');
            if (rows == null || rows.length < 1) {
                showMsg('提示', '请选择要删除的备份文件', true);
                return;
            }
            var ids = new Array();
            for (var i = 0; i < rows.length; i++) {
                ids.push(rows[i].id);
            }
            if (showConfirm("提示", "确定要删除用户吗？", function () {
                        $.post("${ctx}/sys/backup/del", {ids: ids}, function (data) {
                            if (data.success) {
                                $("#dg").datagrid('reload');
                            } else {
                                showMsg("提示", "操作出错", true);
                            }
                        });
                    }));
        }
        function onSearch() {
            var timeStart = $("#timeStart").datetimebox("getText");
            var timeEnd = $("#timeEnd").datetimebox("getText");
            var parms = {};
            if (timeStart != null && timeStart != "") {
                parms['createTime|>='] = timeStart;
            }
            if (timeEnd != null && timeEnd != "") {
                parms['createTime|<='] = timeEnd;
            }
            $("#dg").datagrid('load', parms);
        }
    </script>
</head>
<body>
<div class="toolBar">
    <form id="queryForm">
        <table style="width:100%;">
            <tr>
                <td style="width:100%;">
                    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'"
                       onclick="backup();">备份案例库</a>
                    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-back'" onclick="restore();">恢复案例库</a>
                    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="del();">删除备份</a>
                </td>
            </tr>
        </table>
        <table>
            <tr>
                <td>备份时间:</td>
                <td><input class="easyui-datetimebox" id="timeStart" name="timeStart" style="width: 150px"></td>
                <td>至：</td>
                <td><input class="easyui-datetimebox" id="timeEnd" name="timeEnd" style="width: 150px"></td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>
            </tr>
        </table>
    </form>
</div>
<div style="height: auto">
    <table id="dg"></table>
</div>
</body>
</html>

