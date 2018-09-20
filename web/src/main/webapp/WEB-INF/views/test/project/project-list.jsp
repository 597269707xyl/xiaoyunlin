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
</head>
<body>
<div class="toolBar">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                   onclick="addOrUpdate('add');">增加</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="deleteProjects();">删除</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                   onclick="addOrUpdate('update');">修改</a>
                <%--<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"--%>
                <%--onclick="assignSimIns();">指定仿真实例</a>--%>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                   onclick="assignMember();">指定成员</a>
                <%--<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"--%>
                <%--onclick="showProjectResource();">项目文档管理</a>--%>
            </td>
        </tr>
    </table>
    <form id="queryForm">
        <table>
            <tr>
                <td>项目名称:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="name" name="name" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>被测系统:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="testSystem" name="testSystem" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>
            </tr>
        </table>
    </form>
</div>
<div style="height: auto">
    <table id="dg"></table>
</div>
<div id="dlg-fields"></div>
<div id="window-sim-list"></div>
<script>
    $(function () {
        $('#dg').datagrid({
            height: $(window).height() - 80,
            fitColumns: true,
            singleSelect: false,
            url: '${ctx}/proj/proj/query',
            pagination: true,
            pageSize: 20,
            pageList: [10, 20, 30, 50],
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {field: 'abb', title: '项目标识', width: 70},
                {field: 'name', title: '项目名称', width: 70},
                {field: 'testsystem', title: '被测系统', width: 70},
                {field: 'typeDis', title: '项目类型', width: 70},
                {field: 'starttime', title: '创建日期', width: 70},
                {field: 'endtime', title: '预期截止日期', width: 70},
                {field: 'userName', title: '测试人员', width: 100},
                {field: 'insName', title: '依赖实例', width: 100},
                {field: 'descript', title: '说明', width: 100}
            ]]
        });
    });
    function onSearch() {
        var name = $("#name").textbox("getValue");
        var testSystem = $("#testSystem").textbox("getValue");
        var p = {};
        if (name != null && name != "") {
            p['name|like'] = name;
        }
        if (testSystem != null && testSystem != "") {
            p['testsystem|like'] = testSystem;
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
        commonShowWindow(param, title, "${ctx}/proj/proj/add", 360, 280, true, false, false);
    }

    function assignMember() {
        var dg = $("#dg");
        var rows = dg.datagrid('getSelections');
        if (rows == null || rows.length != 1) {
            showMsg("提示", "请选择一条记录进行操作", true);
            return;
        }
        var dgRow = dg.datagrid('getSelected');
        if (!dgRow) {
            showMsg('提示', '请选择测试项目', true);
        }
        else {
            var url = "${ctx}/proj/proj/assign?id=" + dgRow.id;
            showWindow("修改", url, 480, 330, true, false, false);
        }
    }
    function deleteProjects() {
        var nodes = $("#dg").datagrid('getSelections'), ids = new Array();
        if (nodes && nodes.length > 0) {
            for (var i = 0; i < nodes.length; i++) {
                ids.push(nodes[i].id);
            }
        }
        if (nodes && nodes.length > 0) {
            if (showConfirm("提示", "确定要删除项目吗？", function () {
                        $.post("${ctx}/proj/proj/deleteProjects", {ids: ids}, function (data) {
                            if (data.success) {
                                $("#dg").datagrid('reload');
                            } else {
                                showMsg("提示", "操作出错", true);
                            }
                        });

                    }));
        } else {
            showMsg("提示", "请选择要删除的项目", true);
        }
    }
    function assignSimIns() {
        var dg = $("#dg");
        var rows = dg.datagrid('getSelections');
        if (rows == null || rows.length != 1) {
            showMsg("提示", "请选择一条记录进行操作", true);
            return;
        }
        var row = dg.datagrid('getSelected');
        if (!row) {
            showMsg('提示', '请选择测试项目', true);
        } else {
            var param = {};
            param.action = 'update';
            param.id = row.id;
            commonShowWindow(param, "指定仿真实例", "${ctx}/proj/proj/allocateSimulators", 480, 420, true, false, false);
        }
    }

    function showProjectResource() {
        var dg = $("#dg");
        var rows = dg.datagrid('getSelections');
        if (rows == null || rows.length != 1) {
            showMsg("提示", "请选择一条记录进行操作", true);
            return;
        }
        var row = dg.datagrid('getSelected');
        if (!row) {
            showMsg('提示', '请选择测试项目', true);
        } else {
            var param = {};
            param.action = 'update';
            param.id = row.id;
            commonShowWindow(param, "项目文件列表", "${ctx}/proj/proj/fileList", 480, 420, true, false, false);
        }
    }

</script>
</body>
</html>

