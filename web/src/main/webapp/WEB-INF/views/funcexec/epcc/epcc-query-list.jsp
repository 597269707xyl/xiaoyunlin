<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>来账报文标识列表</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <script>
        $(function () {
            $('#dg').datagrid({
                height: $(window).height() - 80,
                fitColumns: true,
                url: '${ctx}/func/epcc/queryList',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'case_no', title: '案例编号', width: 70},
                    {field: 'epcc_ids', title: '流水号', width: 90},
                    {field: 'bank_no', title: '发起行号', width: 70},
                    {field: 'message_code', title: '发起报文编号', width: 70},
                    {field: 'message_name', title: '发起报文名称', width: 70},
                    {field: 'msg_code', title: '接收报文编号', width: 70},
                    {field: 'msg_name', title: '接收报文名称', width: 70},
                    {field: 'time_stamp', title: '测试时间', width: 70}
                ]]
            });
        });
        function onSearch() {
            var p = {};
            var caseNo = $("#caseNo").textbox("getValue");
            var epccIds = $("#epccIds").textbox("getValue");
            var timeStart = $("#timeStart").textbox("getValue");
            var timeEnd = $("#timeEnd").textbox("getValue");
            if(timeStart != ''){
                p['beginTime'] = timeStart;
            }
            if(timeEnd != ''){
                p['endTime'] = timeEnd;
            }
            if (caseNo != null && caseNo != "") {
                p['caseNo'] = caseNo;
            }
            if(epccIds != null && epccIds != ""){
                p['epccIds'] = epccIds;
            }
            $("#dg").datagrid('load', p);
        }
        function exportExcel(){
            var caseNo = $("#caseNo").textbox("getValue");
            var epccIds = $("#epccIds").textbox("getValue");
            var timeStart = $("#timeStart").textbox("getValue");
            var timeEnd = $("#timeEnd").textbox("getValue");
            var p = "caseNo=" + caseNo + "&epccIds=" + epccIds + "&beginTime=" + timeStart + "&endTime=" + timeEnd;
            location.href="${ctx}/func/epcc/exportExcel?" + p;
        }
    </script>
</head>
<body>
<div class="toolBar">
    <form id="queryForm">
        <table>
            <tr>
                <td></td>
                <td>案例编号:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="caseNo" name="caseNo" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>流水号:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="epccIds" name="epccIds" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>开始时间:</td>
                <td><input class="easyui-datetimebox" id="timeStart" name="timeStart" style="width: 130px"></td>
                <td>截止时间：:</td>
                <td><input class="easyui-datetimebox" id="timeEnd" name="timeEnd" style="width: 130px"></td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>
                <td>
                    <a href="#" class="easyui-linkbutton" data-options=""
                       onclick="exportExcel();">导出</a>
                </td>
            </tr>
        </table>
    </form>
</div>
<div style="height: auto">
    <table id="dg"></table>
</div>
</body>
</html>
