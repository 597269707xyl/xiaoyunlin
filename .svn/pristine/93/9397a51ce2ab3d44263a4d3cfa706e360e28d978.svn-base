<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>来账统计</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <link href="${ctx}/asset/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<body style="width: 100%;height: 100%">
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'west',split:true" style="width:15%">
        <div class="easyui-layout" data-options="fit:true">
            <div class="toolBar" data-options="region:'north'">
                <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin:4px 0">
                    <tbody>
                    <tr>
                        <td>成员行号:</td>
                        <td><input maxlength="30"
                                        allowInput="true" class="easyui-textbox" id="bankNo" style="width: 100px"
                                        onenter="onBankSearch();"/>
                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                               onclick="onBankSearch();">查询</a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div data-options="region:'center'">
                <div id="bank-list" class="easyui-datagrid">
                </div>
            </div>
        </div>
    </div>
    <div data-options="region:'center',split:true">
        <div class="easyui-layout" data-options="fit:true">
            <div data-options="region:'west',split:true" style="width: 20%">
                <div class="easyui-layout" data-options="fit:true">
                    <div class="toolBar" data-options="region:'north'">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin:4px 0">
                            <tbody>
                            <tr>
                                <td>案例编号:</td>
                                <td><input maxlength="30"
                                                allowInput="true" class="easyui-textbox" id="caseNo" style="width: 130px"
                                                onenter="onCaseSearch();"/>
                                </td>
                            </tr>
                            <tr>
                                <td>开始时间:</td>
                                <td><input class="easyui-datetimebox" id="timeStart" name="timeStart" style="width: 130px"></td>
                            </tr>
                            <tr>
                                <td>截止时间：:</td>
                                <td><input class="easyui-datetimebox" id="timeEnd" name="timeEnd" style="width: 130px"></td>
                                <td>
                                    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                                       onclick="onCaseSearch();">查询</a>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div data-options="region:'center'">
                        <div id="case-list" class="easyui-datagrid">
                        </div>
                    </div>
                </div>
            </div>
            <div data-options="region:'center'">
                <div class="easyui-layout" data-options="fit:true">
                    <div class="toolBar" data-options="region:'north'">
                        <table>
                            <tr>
                                <td>流水号: </td>
                                <td><input maxlength="30"
                                           allowInput="true" class="easyui-textbox" id="msgSeqNo" style="width: 200px"
                                           onenter="onSearch();"/></td>
                                <td>报文编号: </td>
                                <td><input class="easyui-textbox"
                                           allowInput="true" id="msgCode" style="width: 200px"
                                           onenter="onSearch();"/></td>
                                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                                       onclick="onSearch();">查询</a>
                            </tr>
                        </table>
                    </div>
                    <div data-options="region:'center'">
                        <table class="easyui-datagrid" id="recvList" data-options="height:'150'">
                            <thead>
                            <tr>
                                <th data-options="field:'id',hidden:true"></th>
                                <th data-options="field:'bankNo',width:60">成员行号</th>
                                <th data-options="field:'caseNo',width:60">案例编号</th>
                                <th data-options="field:'msgSeqNo',width:60">流水号</th>
                                <th data-options="field:'msgCode',width:60">报文编号</th>
                                <th data-options="field:'msgName',width:60">报文名称</th>
                                <th data-options="field:'msg',width:40,formatter:showMsgData">测试数据</th>
                                <th data-options="field:'createTime',width:60">测试时间</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="window-mark-list"></div>
<script>
    $(function () {
        $('#bank-list').datagrid({
            fit: true,
            fitColumns: true,
            singleSelect: true,
            pagination: true,
            pageSize: 20,
            pageList: [10, 20, 30, 50],
            url: "${ctx}/func/mark/bankList",
            columns: [[
                {field: 'id', title: 'id', hidden: true},
                {field: 'bank_no', title: '成员行号', width: 80},
                {field: 'count', title: '案例总数', width: 30}
            ]],
            onClickRow: function (index, row) {
                $('#case-list').datagrid({url: '${ctx}/func/mark/caseList?bankNo=' + row.bank_no});
            },
            onLoadSuccess: function(data){
                $('#bank-list').datagrid('selectRow', 0);
            },
            onSelect: function(index, row){
                if(row && row.bank_no){
                    $('#case-list').datagrid({url: '${ctx}/func/mark/caseList?bankNo=' + row.bank_no});
                } else {
                    $('#case-list').datagrid('loadData', new Array());
                }
            }
        });
    });
    $('#case-list').datagrid({
        fit: true,
        fitColumns: true,
        singleSelect: true,
        pagination: true,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
        columns: [[
            {field: 'id', title: 'id', hidden: true},
            {field: 'case_no', title: '案例编号', width: 80},
            {field: 'count', title: '执行总数', width: 30}
        ]],
        onClickRow: function (index, row) {
            var p = {bankNo:row.bank_no,caseNo:row.case_no};
            var timeStart = $("#timeStart").textbox("getValue");
            var timeEnd = $("#timeEnd").textbox("getValue");
            if(timeStart != ''){
                p['beginTime'] = timeStart;
            }
            if(timeEnd != ''){
                p['endTime'] = timeEnd;
            }
            $('#recvList').datagrid({url: '${ctx}/func/mark/caseRecvList', queryParams:p});
        },
        onLoadSuccess: function(data){
            $('#case-list').datagrid('selectRow', 0);
        },
        onSelect: function(index, row){
            if(row && row.bank_no && row.case_no){
                var p = {bankNo:row.bank_no,caseNo:row.case_no};
                var timeStart = $("#timeStart").textbox("getValue");
                var timeEnd = $("#timeEnd").textbox("getValue");
                if(timeStart != ''){
                    p['beginTime'] = timeStart;
                }
                if(timeEnd != ''){
                    p['endTime'] = timeEnd;
                }
                $('#recvList').datagrid({url: '${ctx}/func/mark/caseRecvList', queryParams:p});
            } else {
                $('#recvList').datagrid('loadData', new Array());
            }
        }
    });
    $("#recvList").datagrid({
        fit: true,
        fitColumns: true,
        singleSelect: true,
        pagination: true,
        pageSize: 20,
        pageList: [10, 20, 30, 50]
    });
    //成员行查询
    function onBankSearch(){
        var bankNo = $("#bankNo").textbox("getValue");
        var p = {};
        if (bankNo != null && bankNo != "") {
            p['bankNo'] = bankNo;
        }
        $("#bank-list").datagrid('load', p);
    }
    //案例查询
    function onCaseSearch(){
        var bank = $('#bank-list').datagrid('getSelected');
        if(bank){
            var caseNo = $("#caseNo").textbox("getValue");
            var timeStart = $("#timeStart").textbox("getValue");
            var timeEnd = $("#timeEnd").textbox("getValue");
            var p = {bankNo: bank.bank_no};
            if (caseNo != null && caseNo != "") {
                p['caseNo'] = caseNo;
            }
            if(timeStart != ''){
                p['beginTime'] = timeStart;
            }
            if(timeEnd != ''){
                p['endTime'] = timeEnd;
            }
            $('#case-list').datagrid('load', p);
        }
    }
    //执行查询
    function onSearch(){
        var caseList = $('#case-list').datagrid('getSelected');
        if(caseList){
            var msgSeqNo = $("#msgSeqNo").textbox("getValue");
            var msgCode = $("#msgCode").textbox("getValue");
            var timeStart = $("#timeStart").textbox("getValue");
            var timeEnd = $("#timeEnd").textbox("getValue");
            var p = {bankNo: caseList.bank_no, caseNo: caseList.case_no};
            if (msgSeqNo != null && msgSeqNo != "") {
                p['msgSeqNo'] = msgSeqNo;
            }
            if (msgCode != null && msgCode != "") {
                p['msgCode'] = msgCode;
            }
            if(timeStart != ''){
                p['beginTime'] = timeStart;
            }
            if(timeEnd != ''){
                p['endTime'] = timeEnd;
            }
            $("#recvList").datagrid('load', p);
        }
    }
    //测试数据
    function showMsgData(value, row, index){
        return "<a href='#' onclick='showMsgDataDtail(" + row.id + ")'>测试数据</a>";
    }
    //测试执行数据详情
    function showMsgDataDtail(id){
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
                setData(id);
            }
        });
    }
</script>
</body>
</html>
