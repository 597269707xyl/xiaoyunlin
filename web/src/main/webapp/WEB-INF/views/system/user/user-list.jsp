<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <script>
        $(function () {
            $('#dg').datagrid({
                //fit: true,
                height: $(window).height() - 80,
                fitColumns: true,
                url: '${ctx}/sys/user/query',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'name', title: '姓名', width: 70},
                    {field: 'sysName', title: '账号', width: 70},
                    {field: 'roles', title: '角色', width: 70}
                    /*{field: 'dept', title: '所属部门', width: 100},
                    {field: 'title', title: '职位', width: 80},
                    {field: 'degreeDis', title: '学历', width: 50},
                    {field: 'school', title: '毕业院校', width: 100},
                    {field: 'bod', title: '出生日期', width: 70},
                    {field: 'sexDis', title: '性别', width: 30},
                    {
                        field: 'sysUserEmail', title: '邮箱', width: 100,
                        formatter: function (value, row) {
                            return row.sysUser.email;
                        }
                    },
                    {field: 'phoneNum', title: '手机号', width: 80},
                    {field: 'telNum', title: '座机号', width: 80}*/
                ]]
            });
        });
        function onSearch() {
            var name = $("#name").textbox("getValue");
            var p = {};
            if (name != null && name != "") {
                p['name|like'] = name;
            }
            $("#dg").datagrid('load', p);
        }

        function add() {
            showWindow("新增", "${ctx}/sys/user/add", 320, 260, true, false, false);
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
                var url = "${ctx}/sys/user/edit/" + row.id;
                showWindow("修改", url, 320, 260, true, false, false);
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
                if (showConfirm("提示", "确定要删除用户吗？", function () {
                            $.post("${ctx}/sys/user/del", {ids: ids}, function (data) {
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
        function resetPwd(){
            var nodes = $("#dg").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            if (nodes && nodes.length > 0) {
                if (showConfirm("提示", "确定要重置用户密码吗？", function () {
                            $.post("${ctx}/sys/user/resetPwd", {ids: ids}, function (data) {
                                if (data.success) {
                                    $("#dg").datagrid('reload');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });

                        }));
            } else {
                showMsg("提示", "请选择用户", true);
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
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="edit();">修改</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="resetPwd();">重置密码</a>
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
                <%--<td>性别:</td>
                <td>
                    <input class="easyui-combobox" id="sex" name="sex" style="width: 80px" panelHeight="50px"
                           valueField="id" textField="text" limitToList="true" url="${ctx}/generic/dict/SEX" onenter="onSearch"/>
                </td>--%>
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
