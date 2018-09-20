<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2018/7/11
  Time: 9:38
  To change this template use File | Settings | File Templates.
--%>
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
                height: $(window).height() - 80,
                fitColumns: true,
                //url: '${ctx}/sys/institution/query',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'fileName', title: '文档名', width: 70},
                    {field: 'itemName', title: '测试项', width: 70},
                    {field: 'projectName', title: '测试项目', width: 70},
                    {field: 'descript', title: '描述', width: 70},
                    {field: 'time', title: '创建时间', width: 70}
                ]]
            });
            var data = {'total':'1','rows':[{'itemName':'宽城村镇银行','projectName':'业务联调测试', 'fileName':'20180706测试报告','descript':'测试报告','time':'2018-07-06'}
                ,{'itemName':'宽城村镇银行','projectName':'业务联调测试', 'fileName':'NPS-XLL-002','fileName':'20180707测试报告','descript':'测试报告','time':'2018-07-07'}]};
            $('#dg').datagrid('loadData', data);
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
            showWindow("上传", "${ctx}/test/file/add", 320, 260, true, false, false);
        }
        function edit() {


        }
        function del() {

            showMsg("提示", "删除", true);

        }
    </script>
</head>
<body>
<div class="toolBar">
    <table>
        <tr>
            <td>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="add();">上传</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="del();">删除</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="edit();">描述修改</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="downloadFile();">下载</a>
            </td>
            <td>
                <div class="datagrid-btn-separator"></div>
            </td>
            <td>名称:</td>
            <td>
                <input class="easyui-textbox" type="text" id="name" name="name" style="width: 100px"
                       onenter="onSearch"/>
            </td>
            <td>项目:</td>
            <td>
                <input class="easyui-combobox" type="text" id="adapterType" name="adapterType"
                       data-options="" url="${ctx}/proj/proj/findAll?type=config" valueField="id" textField="name"
                       onenter="onSearch"/>
            </td>
            <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                   onclick="onSearch();">查询</a></td>
        </tr>
    </table>
</div>
<div style="height: auto">
    <table id="dg"></table>
</div>
</body>
</html>
