<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>来账报文列表</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <script>
        $(function () {
            $('#dg').datagrid({
                height: $(window).height() - 80,
                fitColumns: true,
                url: '${ctx}/generic/commonQuery/caseRecv',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50, 100],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'bankNo', title: '成员行号', width: 70},
                    {field: 'caseNo', title: '案例编号', width: 70},
                    {field: 'msgSeqNo', title: '流水号', width: 70},
                    {field: 'msgId', title: '报文标识号', width: 70},
                    {field: 'msgCode', title: '报文编号', width: 70},
                    {field: 'msgName', title: '报文名称', width: 70},
                    {field: 'createTime', title: '来账时间', width: 70},
                    {field: 'message', title: '测试数据', width: 70,
                        formatter:function(value, row, index){
                            return '<a href="javascript:markdata(' + row.id + ','+0+')">测试数据</a>';
                    }},
                    {field: 'status', title: '手动应答', width: 70,
                        formatter:function(value, row, index){
                            if(row.status==3){
                                return '<a href="javascript:markdata(' + row.id+ ','+1+')">手动应答</a>';
                            }
                            if(row.status==1){
                                return '已自动应答';
                            }
                            if(row.status==2){
                                return '已手动应答';
                            }
                            if(row.status==0){
                                return '未找到应答报文模板';
                            }
                        }}
                ]]
            });
        });
        function markdata(id,type){
            $('#window-mark-list').window({
                title: '测试数据',
                width: 1000,
                height:600,
                href: '${ctx}/func/mark/markDataPage',
                modal: false,
                minimizable: false,
                maximizable: false,
                shadow: false,
                cache: false,
                closed: false,
                collapsible: false,
                resizable: true,
                loadingMessage: '正在加载数据，请稍等......',
                onLoad: function () {
                    setData(id,type);
                }
            });
        }
        function onSearch() {
            var msgCode = $("#msgCode").textbox("getValue");
            var bankNo = $("#bankNo").textbox("getValue");
            var caseNo = $("#caseNo").textbox("getValue");
            var msgSeqNo = $("#msgSeqNo").textbox("getValue");
            var msgId = $("#msgId").textbox("getValue");
            var p = {};
            if (msgCode != null && msgCode != "") {
                p['msgCode|like'] = msgCode;
            }
            if(bankNo != null && bankNo != ""){
                p['bankNo|like'] = bankNo;
            }
            if(caseNo != null && caseNo != ""){
                p['caseNo|like'] = caseNo;
            }
            if(msgSeqNo != null && msgSeqNo != ""){
                p['msgSeqNo|like'] = msgSeqNo;
            }
            if(msgId != null && msgId != ""){
                p['msgId|like'] = msgId;
            }
            $("#dg").datagrid('load', p);
        }
        function del(){
            var rows = $("#dg").datagrid('getSelections');
            if(rows.length<1){
                showMsg("提示", "请选择一条记录", true);
                return;
            }
            var ids = new Array();
            if (rows && rows.length > 0) {
                for (var i = 0; i < rows.length; i++) {
                    ids.push(rows[i].id);
                }
            }
            if (showConfirm("提示", "确定要删除该记录吗？", function () {
                        $.post("${ctx}/func/mark/dels", {ids: ids}, function (data) {
                            if (data.success) {
                                $("#dg").datagrid('reload');
                            } else {
                                showMsg("提示", data.msg, true);
                            }
                        });

                    }));
        }
    </script>
</head>
<body>
<div class="toolBar">
    <form id="queryForm">
        <table>
            <tr>
                <td></td>
                <td>成员行号:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="bankNo" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>案例编号:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="caseNo" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>流水号:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="msgSeqNo" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>报文标识号:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="msgId" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>报文编号:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="msgCode" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
                       onclick="del();">删除</a></td>
            </tr>
        </table>
    </form>
</div>
<div style="height: auto">
    <table id="dg"></table>
</div>
<div id="window-mark-list"></div>
</body>
</html>
