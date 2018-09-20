<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Title</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <script src="${ctx}/asset/js/jquery.form.min.js"></script>
    <link href="${ctx}/asset/css/style.css" rel="stylesheet" type="text/css"/>
    <script>
        $(function () {
            $('#dg').datagrid({
                fit: true,
                fitColumns: true,
                url: '${ctx}/generic/commonQuery/schema',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'fileName', title: '文件名', width: 100},
                    {field: 'descript', title: '描述', width: 100},
                    /*
                     {field: 'id', title:'操作',renderer:'actionRenderer'},
                     */
                    {
                        field: 'opt', title: '操作', width: 50,
                        formatter: function (value, row, index) {
                            return '<a href="#" onclick="downloadFile(' + row.id + ')">下载</a>';
                        }
                    }
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
            showWindow("新增", "${ctx}/msg/schema/add", 280, 230, true, false, false);
        }
        ;

        function downloadFile(id) {
            var row = $("#dg").datagrid('getSelected');
            $.post("${ctx}/msg/schema/download", {id: id}, function (data) {
                if (data.success) {
                    $("#dg").datagrid('reload');
                } else {
                    showMsg("提示", "操作失败", true);
                }
            });
        }
        ;


        function del() {
            var row = $("#dg").datagrid('getSelected');
            var nodes = $("#dg").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            if (nodes && nodes.length > 0) {
                if (showConfirm("提示", "确定要删除文件吗？", function () {
                            $.post("${ctx}/msg/schema/del", {id: row.id}, function (data) {
                                if (data.success) {
                                    $("#dg").datagrid('reload');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });

                        }));
            } else {
                showMsg("提示", "请选择要删除的文件", true);
            }
        }
        ;
        function actionRenderer(e) {
            var id = e.value;
            var html = "<a href='${ctx}/task/state/res/download?resId=" + id + "'>下载</a> " +
                    " <a href='javascript:void(0)' onclick='doDeleteFile(" + id + ");'>删除</a>";
            return html;
        }

    </script>
</head>
<body>
<div class="content">
    <div class="toolBar">
        <table style="width:100%;">
            <tr>
                <td style="width:100%;">
                    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="add();">增加</a>
                    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
                       onclick="del();">删除</a>
                    <%--
                                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="downloadFile();">下载</a>
                    --%>
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
                    <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                           onclick="onSearch();">查询</a></td>
                </tr>
            </table>
        </form>
    </div>
    <table id="dg"></table>
</div>
</body>
</html>
