<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>执行批次查询</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <link href="${ctx}/asset/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<body style="width: 100%;height: 100%">
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'west',title:'批次列表',split:true" style="width:45%">
        <div class="easyui-layout" data-options="fit:true">
            <div class="toolBar" data-options="region:'north'">
                <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin:4px 0">
                    <tbody>
                    <tr>
                        <td>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="onExportExcel();">导出Excel</a>
                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="batchDel();">删除</a>
                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="stopBatch();">停止</a>
                        </td>
                    </tr>
                    <tr>
                        <td>执行时间:<input class="easyui-datetimebox" id="timeStart" name="timeStart" style="width: 130px">
                        至：<input class="easyui-datetimebox" id="timeEnd" name="timeEnd" style="width: 130px">
                            测试项:<input id="testItem" name="testItem" style="width: 200px"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        执行人:<input class="easyui-textbox" id="execUser" name="execUser" style="width: 130px"/>
                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                               onclick="onBatchSearch();">查询</a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div data-options="region:'center'">
                <div id="batch-list" class="easyui-datagrid">
                </div>
            </div>
        </div>
    </div>
    <div data-options="region:'center'">
        <div class="easyui-layout" data-options="fit:true">
            <div class="toolBar" data-options="region:'north'">
                <table>
                    <tr>
                        <td>案例编号:</td>
                        <td><input maxlength="30"
                                   allowInput="true" class="easyui-textbox" id="searchNO" style="width: 200px"
                                   onenter="onSearch();"/></td>
                        <td>用例目的:</td>
                        <td><input class="easyui-textbox"
                                   allowInput="true" id="searchPurpose" style="width: 200px"
                                   onenter="onSearch();"/></td>
                    </tr>
                    <tr>
                        <td>执行结果:</td>
                        <td><input id="searchSearch" class="easyui-combobox" style="width: 200px;"/></td>
                        <td>失败原因:</td>
                        <td><input class="easyui-combobox" style="width: 200px;"
                                   id="searchResult" name="searchResult"
                                   data-options="panelHeight:'auto',valueField:'id',textField:'text',limitToList:true,url:'${ctx}/generic/dict/UC_FAIL_CLUASE'"/>
                        </td>
                        <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                               onclick="onSearch();">查询</a>
                        </td>
                    </tr>
                </table>
            </div>
            <div data-options="region:'center'">
                <table class="easyui-datagrid" id="execList" data-options="height:'150'">
                    <thead>
                    <tr>
                        <c:if test="${toolType=='nxyw'}">
                            <th data-options="field:'id',hidden:true"></th>
                            <th data-options="field:'case_number',width:50">案例编号</th>
                            <th data-options="field:'purpose',width:90">用例目的</th>
                            <th data-options="field:'step',width:90">用例步骤</th>
                            <th data-options="field:'expected',width:90">预期结果</th>
                            <th data-options="field:'msg',width:50,formatter:showMsgData">测试数据</th>
                            <%--<th data-options="field:'resultType',width:30,formatter:resultType">执行结果</th>--%>
                            <th data-options="field:'resultDis',width:50,formatter:resultDescript">执行结果</th>
                            <th data-options="field:'descript',width:120,formatter:descriptShow">错误描述</th>
                            <th data-options="field:'no',width:80">用例标识</th>
                        </c:if>
                        <c:if test="${toolType=='atsp'}">
                            <th data-options="field:'id',hidden:true"></th>
                            <th data-options="field:'no',width:80">用例标识</th>
                            <th data-options="field:'case_number',width:50">案例编号</th>
                            <th data-options="field:'purpose',width:90">用例目的</th>
                            <th data-options="field:'step',width:90">用例步骤</th>
                            <th data-options="field:'msg',width:50,formatter:showMsgData">测试数据</th>
                            <%--<th data-options="field:'resultType',width:30,formatter:resultType">执行结果</th>--%>
                            <th data-options="field:'resultDis',width:50,formatter:resultDescript">执行结果</th>
                        </c:if>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<div id="window-exec-list"></div>
<script>
    function resultType(value, row, index) {
        if (row.result == 'expected') {
            return "成功";
        } else {
            return "失败";
        }
    }
    function resultDescript(value, row, index) {
        if (row.result == 'expected') {
            return "成功";
        } else {
            return row.resultDis;
        }
    }
    function descriptShow(value, row, index){
        if (row.result == 'expected') {
            return "";
        } else {
            if(value != '' && value != null){
                if(value.indexOf("应答报文超时") != -1) return "应答报文超时";
                return value.substring(value.lastIndexOf("预期返回结果"));
            }
            return '';
        }
    }
    $('#searchSearch').combobox({
        valueField: 'id',
        textField: 'text',
        limitToList: true,
        panelHeight: 'auto',
        data: [{id: "succ", text: "成功"}, {id: "fail", text: "失败"}],
        onSelect: function (record) {
            if (record.id == 'succ') {
                $('#searchResult').combobox('setValue', '');
            }
        }
    });
    $(function () {
        $("#testItem").combotree({
            url:'${ctx}/func/exec/tree?mark=send&caseType=t&id=-1',
            valueField: 'id',
            textField: 'text',
            onSelect: function (node) {
                var type = node.type;

                $(this).tree('options').url = '${ctx}/func/exec/tree?mark=send&caseType=t&type='+type;
                if (node.state == 'closed') {
                    $(this).tree('expand', node.target);
                } else {
                    $(this).tree('collapse', node.target);
                }

            },
            onExpand:function(node){
            },
            onBeforeExpand:function(node){
                var type = node.type;
                $(this).tree('options').url = '${ctx}/func/exec/tree?mark=send&caseType=t&type='+type;
            },
            onLoadError: function (Error) {

            },
            onLoadSuccess: function (success) {
            }
        });
        $('#batch-list').datagrid({
            fit: true,
            nowrap:false,
            fitColumns: true,
            singleSelect: true,
            pagination: true,
            pageSize: 20,
            pageList: [10, 20, 30, 50],
            url: "${ctx}/func/execquery/batchListData",
            columns: [[
                {field: 'id', title: 'id', hidden: true},
                {field: 'begin_time', title: '执行时间', width: 80},
                {field: 'status', title: '执行状态', width: 40},
                {field: 'uc_count', title: '总数', width: 30},
                {field: 'uc_succ_count', title: '成功数', width: 30},
                {field: 'uc_error_count', title: '失败数', width: 30},
                {field: 'rate', title: '成功率', width: 30},
                {field: 'itemName', title: '测试项', width: 120},
                {field: 'user_name', title: '执行人', width: 30}
            ]],
            onClickRow: function (index, row) {
                $('#execList').datagrid({url: '${ctx}/func/execquery/execList?batchId=' + row.id});
            },
            onLoadSuccess: function (data) {
                //$('#batch-list').datagrid('selectRow', 0);
            },
            onSelect: function (index, row) {
                if (row && row.id) {
                    $('#execList').datagrid({url: '${ctx}/func/execquery/execList?batchId=' + row.id});
                } else {
                    $('#execList').datagrid('loadData', new Array());
                }
            }
        });
    });
    $("#execList").datagrid({
        fit: true,
        nowrap:false,
        fitColumns: true,
        singleSelect: true,
        pagination: true,
        pageSize: 20,
        pageList: [10, 20, 30, 50]
    });
    //用例查询
    function onSearch() {
        var item = $('#batch-list').datagrid('getSelected');
        if (item) {
            var searchNO = $("#searchNO").textbox("getValue");
            var searchPurpose = $("#searchPurpose").textbox("getValue");
            var searchResult = $("#searchResult").combobox("getValue");
            var searchSearch = $("#searchSearch").combobox("getValue");
            var p = {batchId: item.id};
            if (searchNO != null && searchNO != "") {
                p['caseNumber|like'] = searchNO;
            }
            if (searchPurpose != null && searchPurpose != "") {
                p['purpose|like'] = searchPurpose;
            }
            if (searchResult != null && searchResult != '') {
                p['result'] = searchResult;
            }
            if (searchSearch != null && searchSearch != '') {
                p['search'] = searchSearch;
            }
            $("#execList").datagrid('load', p);
        }
    }
    function onBatchSearch() {
        var timeStart = $("#timeStart").textbox("getValue");
        var timeEnd = $("#timeEnd").textbox("getValue");
        var testItem = $("#testItem").combotree('tree').tree('getSelected');
        var execUser = $("#execUser").textbox("getValue");
        var p = {};
        if (timeStart != '') {
            p['startTime'] = timeStart;
        }
        if (timeEnd != '') {
            p['endTime'] = timeEnd;
        }
        if (testItem) {
            p['testItem'] = testItem.id;
        }
        if (execUser != '') {
            p['execUser'] = execUser;
        }
        $('#batch-list').datagrid('load', p);
    }
    //测试数据
    function showMsgData(value, row, index) {
        return "<a href='#' onclick='showMsgDataDtail(" + row.id + ")'>测试数据</a>";
    }
    //测试执行数据详情
    function showMsgDataDtail(execId) {
        $('#window-exec-list').window({
            title: '测试数据',
            width: 1000,
            height: 400,
            href: '${ctx}/func/usecase/getUsecaseExecList',
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
                setData(execId);
            }
        });
    }
    //导出excel表格
    function onExportExcel() {
        var rows = $("#batch-list").datagrid('getSelections');
        if (rows.length < 1) {
            showMsg("提示", "请选择一条记录", true);
            return;
        }
        var ids = new Array();
        for (var i = 0; i < rows.length; i++) {
            ids.push(rows[i].id);
        }
        var idStrs = ids.join(",");
        location.href = "${ctx}/func/execquery/exportExcel?ids=" + idStrs;
    }

    function batchDel() {
        var rows = $("#batch-list").datagrid('getSelections');
        if (rows.length < 1) {
            showMsg("提示", "请选择一条记录", true);
            return;
        }
        if (showConfirm("提示", "确定要删除该记录吗？", function () {
                    $.post("${ctx}/func/execquery/del", {id: rows[0].id}, function (data) {
                        if (data.success) {
                            $("#batch-list").datagrid('reload');
                        } else {
                            showMsg("提示", "操作出错", true);
                        }
                    });
                }));
    }
    function stopBatch(){
        var rows = $("#batch-list").datagrid('getSelections');
        if (rows.length < 1) {
            showMsg("提示", "请选择一条记录", true);
            return;
        }
        $.post("${ctx}/func/usecase/stopBatch", {batchId: rows[0].id}, function (data) {
            if (data.success) {
                showMsg("提示", "停止成功!", true);
                $("#batch-list").datagrid('reload');
            } else {
                showMsg("提示", "操作出错", true);
            }
        });
    }
</script>
</body>
</html>
