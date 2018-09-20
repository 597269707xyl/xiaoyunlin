<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/4/27
  Time: 14:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <link href="${ctx}/asset/css/style.css" rel="stylesheet" type="text/css"/>
    <script>
        $(function () {
            $('#dg').datagrid({
                height: $(window).height() - 48,
                fitColumns: true,
                url: '${ctx}/generic/commonQuery/log',
                rownumbers:true,
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    //{field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'username', title: '操作人员', width: 100},
                    {field: 'logtime', title: '操作时间', width: 80},
                    {field: 'clientip', title: '登录IP', width: 80},
                    {field: 'content', title: '内容', width: 80}
                ]]
            });
        });
        function onSearch() {
            var operator = $("#operator").textbox("getValue");
            var timeStart = $("#timeStart").datetimebox("getText");
            var timeEnd = $("#timeEnd").datetimebox("getText");
            var type = $("#type").combobox("getValue");
            var parms = {};
            if (operator != null && operator != "") {
                parms['username|like'] = operator;
            }
            if (timeStart != null && timeStart != "") {
                parms['logtime|>='] = timeStart;
            }
            if (timeEnd != null && timeEnd != "") {
                parms['logtime|<='] = timeEnd;
            }
            if (type != null && type != "") {
                parms['type|like'] = type;
            }
            $("#dg").datagrid('load', parms);
        }
    </script>
</head>
<body>
<div class="toolBar">
    <table>
        <tr>
            <td>操作人员:</td>
            <td>
                <input class="easyui-textbox" type="text" id="operator" name="operator" style="width: 100px"
                       onenter="onSearch"/>
            </td>
            <td>操作时间:</td>
            <td><input class="easyui-datetimebox" id="timeStart" name="timeStart" style="width: 150px"></td>
            <td>至：</td>
            <td><input class="easyui-datetimebox" id="timeEnd" name="timeEnd" style="width: 150px"></td>
            <td>类型:</td>
            <td>
                <select class="easyui-combobox" type="text" id="type" name="type" style="width: 150px"
                        data-options="limitToList:true" panelHeight="auto" onenter="onSearch"/>
                <option value="S">系统管理</option>
                <option value="M">报文数据管理</option>
                <option value="C">仿真配置管理</option>
                <option value="P">测试项目管理</option>
                <option value="A">自动化测试执行</option>
                <option value="T">仿真测试执行</option>
            </td>
            <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                   onclick="onSearch();">查询</a></td>
        </tr>
    </table>
</div>
<table id="dg"></table>
</body>
</html>
