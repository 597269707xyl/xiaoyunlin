<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>缺陷管理</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <link href="${ctx}/asset/css/style.css" rel="stylesheet" type="text/css"/>
    <script src="${ctx}/asset/js/easyui/plugins/jquery.datagrid.js"></script>
    <script>
        $(function () {
            $('#dg').datagrid({
                height: $(window).height() -110,
                <%--url: '${ctx}/generic/commonQuery/simSystemInstance',--%>
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'itemName', title: '功能模块', width: 150},
                    {field: 'projectName', title: '项目名称', width: 150},
                    {field: 'mark', title: '缺陷标识', width: 150},
                    {field: 'case', title: '案例编号', width: 150},
                    {field: 'state', title: '缺陷状态', width: 100},
                    {field: 'grad', title: '缺陷等级', width: 100},
                    {field: 'user', title: '测试人员', width: 100},
                    {field: 'startTime', title: '创建时间', width: 150},
                    {field: 'endTime', title: '修复时间', width: 150},
                    {field: 'descript', title: '缺陷描述', width: 300}
//                    {field: 'state', title: '类型', width: 50,
//                        formatter: function(value,row,index){
//                            return '<a href="#" onclick="startServer(\''+row.id+'\')">测试数据</a>';
//                        }},
//                    {field: 'opt', title: '测试数据', width: 150,
//                        formatter: function(value,row,index){
//                            return '<a href="#" onclick="startServer(\''+row.id+'\')">测试数据</a>';
//                        }}
                ]]
            });
            var data = {'total':'1','rows':[{'itemName':'宽城村镇银行','projectName':'业务联调测试', 'mark':'NPS-XLL-001','case':'NCS2-LT-XXL-050','state':'已修复','grad':'高', 'user':'李艳军','startTime':'2018-03-01','endTime':'2018-04-04','descript':'账户检测有缺陷'}
            ,{'itemName':'宽城村镇银行','projectName':'业务联调测试', 'mark':'NPS-XLL-002','case':'NCS2-LT-XXL-051','state':'未修复','grad':'中', 'user':'李艳军','startTime':'2018-03-01','endTime':'','descript':'账号检测有缺陷'}]};
            $('#dg').datagrid('loadData', data);
        });
        function onSearch() {

            var insName = $("#simInsName").textbox("getValue");
            var p = {};
            if (insName != null && insName != "") {
                p['name|like'] = insName;
            }
            $("#dg").datagrid('load', p);
        }

        function addOrUpdate(title, type){
            commonShowWindow(null, title, "${ctx}/defect/add", 330, 350, true, false, false);
        }
    </script>
</head>
<body>
<div class="toolBar">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                   onclick="addOrUpdate('新增缺陷', 'add');">增加</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                   onclick="addOrUpdate('修改缺陷', 'edit');">修改</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
                   onclick="del();">删除</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                   onclick="setReponseRule();">设置缺陷状态</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                   onclick="cloneBaseData();">导出</a>
            </td>
        </tr>
    </table>
    <form id="queryForm">
        <table>
            <tr>
                <td>测试项目:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="simInsName" name="simInsName" style="width: 150px"
                           onenter="onSearch"/>
                </td>
                <td>缺陷标识:</td>
                <td>
                    <input class="easyui-textbox" type="text" name="simInsName" style="width: 150px"
                           onenter="onSearch"/>
                </td>
                <td>缺陷状态:</td>
                <td>
                    <select name="type" class="easyui-combobox" style="width:150px;"
                            data-options="panelHeight:'auto',limitToList:'true'">
                        <option></option>
                        <option value="1">已修复</option>
                        <option value="0">未修复</option>
                    </select>
                </td>
                <td>缺陷等级:</td>
                <td>
                    <select id="type" name="type" class="easyui-combobox" style="width:150px;"
                            data-options="panelHeight:'auto',limitToList:'true'">
                        <option></option>
                        <option value="G">高</option>
                        <option value="Z">中</option>
                        <option value="D">底</option>
                    </select>
                </td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>
            </tr>
            <tr>
                <td>创建时间:</td>
                <td><input class="easyui-datetimebox" id="sendBegin" name="sendBegin" style="width: 150px"></td>
                <td>至：</td>
                <td><input class="easyui-datetimebox" id="sendEnd" name="sendEnd" style="width: 150px"></td>
                <td>修复时间:</td>
                <td><input class="easyui-datetimebox" id="" name="sendBegin" style="width: 150px"></td>
                <td>至：</td>
                <td><input class="easyui-datetimebox" id="" name="sendEnd" style="width: 150px"></td>
            </tr>
        </table>
    </form>
</div>
<div style="height: auto">
    <table id="dg"></table>
</div>
</body>
</html>
