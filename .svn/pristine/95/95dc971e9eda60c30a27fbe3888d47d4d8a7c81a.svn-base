<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <script src="${ctx}/asset/js/easyui/plugins/jquery.datagrid.js"></script>

</head>
<body>
<div class="toolBar">
    <form id="queryForm">
        <table>
            <tr>
                <td></td>
                <td>
                    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
                       onclick="del();">删除</a>
                </td>
                <td>测试项:</td>
                <td>
                    <input id="testItem" name="testItem"/>
                </td>
                <td>案例编号:</td>
                <td>
                    <input class="easyui-textbox" id="no" name="no" data-options="" style="">
                </td>
                <td>用例目的:</td>
                <td>
                    <input class="easyui-textbox" id="purpose" name="purpose" data-options="" style="">
                </td>
                <td>执行结果:</td>
                <td>
                    <input id="result" name="result" style="width: 100px;"/>
                </td>
                <td>
                    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a>
                </td>
                <td>
                    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="addImpDefect();">新增缺陷</a>
                </td>
            </tr>
        </table>
    </form>
</div>
<div style="height: auto">
    <table id="dg"></table>
</div>
<div id="window-exec-list"></div>
<script>
    $(function () {
        $("#testItem").combotree({
            url: '${ctx}/func/exec/tree?mark=send&caseType=t&id=-1&type=',
            valueField: 'id',
            textField: 'text',
            onSelect: function (node) {
                var type = node.type;

                $(this).tree('options').url = '${ctx}/func/exec/tree?mark=send&caseType=t&type=' + type;
                if (node.state == 'closed') {
                    $(this).tree('expand', node.target);
                } else {
                    $(this).tree('collapse', node.target);
                }

            },
            onExpand: function (node) {
            },
            onBeforeExpand: function (node) {
                var type = node.type;
                $(this).tree('options').url = '${ctx}/func/exec/tree?mark=send&caseType=t&type=' + type;
            },
            onLoadError: function (Error) {

            },
            onLoadSuccess: function (success) {
            }
        });
        $("#result").combobox({
            url: '${ctx}/generic/dict/UC_RESULT',
            panelHeight: 'auto',
            valueField: 'id',
            textField: 'text'
        });
        $('#dg').datagrid({
//                fit: true,
            fitColumns: true,
            height: $(window).height() - 50,
            singleSelect: false,
            pagination: true,
            pageSize: 20,
            pageList: [10, 20, 30, 50],
            url: '${ctx}/func/execquery/query',
            columns: [[
                {field: 'id', title: 'id', hidden: true},
                {field: 'no', title: '用例标识', width: 70},
                {field: 'case_number', title: '案例编号', width: 70},
                {field: 'purpose', title: '用例目的', width: 70},
                {field: 'step', title: '用例步骤', width: 70},
                {field: 'number', title: '执行次数', width: 70},
                {field: 'resultDis', title: '用例执行结果', width: 70}
            ]],
            view: detailview,
            detailFormatter: function (index, row) {
                return '<div style="padding:2px;position:relative;"><table class="ddv"></table></div>';
            },
            onExpandRow: function (index, row) {
                var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                ddv.datagrid({
                    url: '${ctx}/func/usecase/getUsecaseExec?id=' + row.id,
                    fitColumns: true,
                    singleSelect: true,
                    rownumbers: true,
                    loadMsg: '',
                    height: 'auto',
                    columns: [[
                        {field: 'id', title: 'id', hidden: true},
                        {field: 'round', title: '执行轮次', width: 60},
                        {field: 'execTime', title: '执行时间', width: 100},
                        {field: 'resultDis', title: '本次执行结果', width: 100},
                        {
                            field: 'msg', title: '测试数据', width: 100,
                            formatter: function (value, row, index) {
                                return "<a href='#' onclick='detail(" + row.id + ")'>测试数据</a>";
                            }
                        },
                        {
                            field: 'mm', title: '下载', width: 100,
                            formatter: function (value, row, index) {
                                return "<a href='#' onclick='uploadData(" + row.id + ")'>导出数据</a>";
                            }
                        }
                    ]],
                    onResize: function () {
                        $('#usecaseList').datagrid('fixDetailRowHeight', index);
                    },
                    onLoadSuccess: function () {
                        setTimeout(function () {
                            $('#usecaseList').datagrid('fixDetailRowHeight', index);
                        }, 0);
                    }
                });
                $('#usecaseList').datagrid('fixDetailRowHeight', index);
            }
        });
    });

    function uploadData(id) {
        location.href = "${ctx}/func/execquery/uploadData?id=" + id;
    }

    function detail(execId) {
        $('#window-exec-list').window({
            title: '测试数据',
            width: 800,
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
    function onSearch() {
        var item = $('#testItem').combotree('tree').tree('getSelected');
        if (item) {
            var result = $("#result").combobox('getValue');
            var searchNO = $("#no").textbox("getValue");
            var searchPurpose = $("#purpose").textbox("getValue");
            var p = {};
            p['nxyFuncItem.id'] = item.id;
            p['type'] = item.type;
//                if (searchNO != null && searchNO != "") {
//                    p['no|like'] = searchNO;
//                }
            if (searchNO != null && searchNO != "") {
                p['caseNumber|like'] = searchNO;
            }
            if (searchPurpose != null && searchPurpose != "") {
                p['purpose|like'] = searchPurpose;
            }
            if (result != null && result != "") {
                p['result'] = result;
            }
            $("#dg").datagrid('load', p);
        } else {
            showMsg('提示', '请选择要查询的测试项', true);
        }
    }
    function del() {
        var rows = $("#dg").datagrid('getSelections');
        if (rows.length < 1) {
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
                    $.post("${ctx}/func/execquery/dels", {ids: ids}, function (data) {
                        if (data.success) {
                            $("#dg").datagrid('reload');
                        } else {
                            showMsg("提示", data.msg, true);
                        }
                    });

                }));
    }

    function addDefect() {
        commonShowWindow({action: 'update', type: 1}, '新增缺陷', "${ctx}/defect/addUd", 330, 250, true, false, false);
    }

    function addImpDefect() {
        var param = {};
        var title;
        var rows = $("#dg").datagrid('getSelections');
        if (rows.length == 0) {
            showMsg("提示", "请选择案例", true);
            return;
        } else if (rows.length > 1) {
            showMsg("提示", "请选择单条案例进行添加缺陷操作", true);
            return;
        } else {
            var row = $("#dg").datagrid('getSelected');
            if (row) {
                param.usecaseId = row.id;
                param.action='update';
            }
            title = "添加自定义缺陷";
            commonShowWindow(param, title, "${ctx}/defect/addImp/", 330, 250, true, false, false);
        }
    }
</script>
</body>
</html>
