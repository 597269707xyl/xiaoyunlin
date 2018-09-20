<%--
  Created by IntelliJ IDEA.
  User: huangbo
  Date: 2018/8/1
  Time: 15:25
  To change this template use File | Settings | File Templates.
--%>
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

</head>
<body>
<div class="toolBar">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                   onclick="addOrUpdate('新增缺陷', 'add');">增加</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                   onclick="addOrUpdate('修改缺陷', 'update');">修改</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
                   onclick="del();">删除</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                   onclick="setFixed();">批量修复</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                   onclick="exportExcel();">导出</a>
            </td>
        </tr>
    </table>
    <form id="queryForm">
        <table>
            <tr>
                <td>创建时间:</td>
                <td><input class="easyui-datetimebox" id="createFrom" name="createFrom" data-options="icons: [{
                    iconCls: 'icon-clear', handler: function (e) {
                        $(e.data.target).datetimebox('clear');
                     }
                }]"></td>
                <td>至：</td>
                <td><input class="easyui-datetimebox" id="createTo" name="createTo" data-options="icons: [{
                    iconCls: 'icon-clear', handler: function (e) {
                        $(e.data.target).datetimebox('clear');
                     }
                }]"></td>
                <td>修复时间:</td>
                <td><input class="easyui-datetimebox" id="fixFrom" name="fixFrom" data-options="icons: [{
                    iconCls: 'icon-clear', handler: function (e) {
                        $(e.data.target).datetimebox('clear');
                     }
                }]"></td>
                <td>至：</td>
                <td><input class="easyui-datetimebox" id="fixTo" name="fixTo" data-options="icons: [{
                    iconCls: 'icon-clear', handler: function (e) {
                        $(e.data.target).datetimebox('clear');
                     }
                }]"></td>

            </tr>
            <tr>
                <td>缺陷标识:</td>
                <td>
                    <input class="easyui-textbox" id="abbreviation" name="abbreviation"
                           data-options="icons:[{iconCls:'icon-clear',handler: function(e){$(e.data.target).textbox('clear');}}]">
                </td>
                <td>用例标识:</td>
                <td>
                    <input class="easyui-textbox" id="no" name="no"
                           data-options="icons:[{iconCls:'icon-clear',handler: function(e){$(e.data.target).textbox('clear');}}]">
                </td>

                <td>测试项:</td>
                <td>
                    <input id="testItem" name="testItem"/>
                </td>
                <td>来往账类型:</td>
                <td>
                    <input id="mark" name="mark" class="easyui-combobox"
                           data-options="valueField: 'value',textField: 'text',panelHeight:'auto',
                            icons:[{iconCls:'icon-clear',handler: function(e){$(e.data.target).combobox('clear');}}],
                            data: [{value: 'send',text: '往账'},{value: 'recv',text: '来账'}]">
                    </input>
                </td>
                <td>缺陷等级:</td>
                <td>
                    <input id="grade" name="grade" class="easyui-combobox"
                           data-options="valueField: 'value',textField: 'text',panelHeight:'auto',
                            icons:[{iconCls:'icon-clear',handler: function(e){$(e.data.target).combobox('clear');}}],
                            data: [{value: 'H',text: '高'},{value: 'M',text: '中'},{value: 'L',text: '低'}]">
                    </input>
                </td>
                <td>修复状态:</td>
                <td>
                    <input id="fixStatus" name="fixStatus" class="easyui-combobox"
                           data-options="valueField: 'value',textField: 'text',panelHeight:'auto',
                            icons:[{iconCls:'icon-clear',handler: function(e){$(e.data.target).combobox('clear');}}],
                            data: [{value: '1',text: '已修复'},{value: '0',text: '未修复'}]">
                    </input>
                </td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>


            </tr>

        </table>
    </form>
</div>
<div style="height: auto">
    <table id="dg"></table>
</div>
<script>
    var selectType;
    $(function () {
        $("#testItem").combotree({
            url: '${ctx}/func/exec/tree?mark=send&caseType=t&id=-1&type=',
            valueField: 'id',
            textField: 'text',
            icons: [{
                iconCls: 'icon-clear', handler: function (e) {
                    $(e.data.target).combotree('clear');
                    selectType = null;
                }
            }],
            onSelect: function (node) {
                var type = node.type;
                selectType = type;
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
        $('#dg').datagrid({
            height: $(window).height() - 110,
            url: '${ctx}/defect/getDefect',
            pagination: true,
            pageSize: 20,
            pageList: [10, 20, 30, 50],
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {
                    field: 'abbreviation', title: '缺陷标识', width: 100
                },
                {
                    field: 'projectName', title: '项目名称', width: 150,
                    formatter: function (val, row, index) {
                        if (row.testProject) return row.testProject.name;
                    }
                },
                {
                    field: 'itemName', title: '测试项名称', width: 250,
                    formatter: function (val, row) {
                        if (row.nxyFuncItem) return row.nxyFuncItem.name;
                    }
                },
                {
                    field: 'case', title: '用例标识', width: 150,
                    formatter: function (val, row) {
                        if (row.nxyFuncUsecase) return row.nxyFuncUsecase.no;
                    }
                },
                {
                    field: 'mark', title: '来往账类型', width: 100,
                    formatter: function (val, row) {
                        if (row.mark == 'send') {
                            return '往账';
                        } else if (row.mark == 'recv') {
                            return '来账';
                        }
                    }
                },

                {
                    field: 'fixStatus', title: '修复状态', width: 100,
                    formatter: function (val, row) {
                        if (row.fixStatus == null) {
                            return;
                        } else if (row.fixStatus) {
                            return '已修复';
                        } else {
                            return '未修复'
                        }
                    }
                },
                {
                    field: 'grade', title: '缺陷等级', width: 100,
                    formatter: function (val, row) {
                        if (row.grade == 'H') {
                            return '高';
                        } else if (row.grade == 'M') {
                            return '中';
                        } else if (row.grade == 'L') {
                            return '低';
                        }
                    }

                },
                {field: 'createUser', title: '创建人', width: 100},
                {field: 'createTime', title: '创建时间', width: 150},
                {field: 'fixTime', title: '修复时间', width: 150},
                {field: 'descript', title: '缺陷描述', width: 300}
            ]]
        });
    });

    $('#grade').combobox('setValue', 'H');

    function addOrUpdate(title, action) {
        var param = {action: action};
        if (action == 'update') {
            var rows = $("#dg").datagrid('getSelections');
            if (rows.length == 0) {
                showMsg("提示", "请选择要修改的缺陷", true);
                return;
            } else if (rows.length > 1) {
                showMsg("提示", "请选择单条缺陷进行修改", true);
                return;
            } else {
                var row = $("#dg").datagrid('getSelected');
                if (row) {
                    param.id = row.id;
                    param.type = row.type;
                }
            }
        }
        commonShowWindow(param, title, "${ctx}/defect/addUd", 330, 320, true, false, false);
    }

    function onSearch() {
        var testItem = $('#testItem').textbox('getValue');
        var createFrom = $('#createFrom').textbox('getValue');
        var createTo = $('#createTo').textbox('getValue');
        var fixFrom = $('#fixFrom').textbox('getValue');
        var fixTo = $('#fixTo').textbox('getValue');
        var fixStatus = $('#fixStatus').textbox('getValue');
        var grade = $('#grade').textbox('getValue');
        var mark = $('#mark').textbox('getValue');
        var no = $('#no').textbox('getValue');
        var abbreviation = $('#abbreviation').textbox('getValue');
        var createTime = createFrom + "," + createTo;
        var p = {};

        if (no != null && no != "") {
            p['nxyFuncUsecase.no|like'] = no;
        }
        if (abbreviation != null && abbreviation != "") {
            p['abbreviation|like'] = abbreviation;
        }
        if (mark != null && mark != "") {
            p['mark'] = mark;
        }
        if (createFrom != null && createFrom != "") {
            p['createTime|>='] = createFrom;
        }
        if (createTo != null && createTo != "") {
            p['createTime|<='] = createTo;
        }
        if (fixFrom != null && fixFrom != "") {
            p['fixTime|>='] = fixFrom;
        }
        if (fixTo != null && fixTo != "") {
            p['fixTime|='] <= fixTo;
        }
        if (grade != null && grade != "") {
            p['grade'] = grade;
        }
        if (fixStatus != null && fixStatus != "") {
            if (fixStatus == '1') {
                p['fixStatus'] = true;
            } else if (fixStatus == '0') {
                p['fixStatus'] = false;
            }
        }
        if (selectType == 'project') {
            p['testProject.id'] = testItem;
        } else if (selectType == 'item') {
            p['nxyFuncItem.id'] = testItem;
        }
        $("#dg").datagrid('load', p);
    }

    function del() {
        var nodes = $("#dg").datagrid('getSelections'), ids = new Array();
        if (nodes && nodes.length > 0) {
            for (var i = 0; i < nodes.length; i++) {
                ids.push(nodes[i].id);
            }
        }
        if (nodes && nodes.length > 0) {
            if (showConfirm("提示", "确定要删除所选缺陷吗？", function () {
                        $.post("${ctx}/defect/del", {ids: ids}, function (data) {
                            if (data.success) {
                                $("#dg").datagrid('reload');
                            } else {
                                showMsg("提示", "操作出错", true);
                            }
                        });

                    }));
        } else {
            showMsg("提示", "请选择要删除的缺陷", true);
        }
    }

    function exportExcel() {
        var testItem = $('#testItem').textbox('getValue');
        var createFrom = $('#createFrom').textbox('getValue');
        var createTo = $('#createTo').textbox('getValue');
        var fixFrom = $('#fixFrom').textbox('getValue');
        var fixTo = $('#fixTo').textbox('getValue');
        var fixStatus = $('#fixStatus').textbox('getValue');
        var grade = $('#grade').textbox('getValue');
        var mark = $('#mark').textbox('getValue');
        var createTime = createFrom + "," + createTo;
        var p = {};

        if (mark != null && mark != "") {
            p['mark'] = mark;
        }
        if (createFrom != null && createFrom != "") {
            p['createTime|>='] = createFrom;
        }
        if (createTo != null && createTo != "") {
            p['createTime|<='] = createTo;
        }
        if (fixFrom != null && fixFrom != "") {
            p['fixTime|>='] = fixFrom;
        }
        if (fixTo != null && fixTo != "") {
            p['fixTime|='] <= fixTo;
        }
        if (grade != null && grade != "") {
            p['grade'] = grade;
        }
        if (fixStatus != null && fixStatus != "") {
            if (fixStatus == '1') {
                p['fixStatus'] = true;
            } else if (fixStatus == '0') {
                p['fixStatus'] = false;
            }
        }
        if (selectType == 'project') {
            p['testProject.id'] = testItem;
        } else if (selectType == 'item') {
            p['nxyFuncItem.id'] = testItem;
        }
        location.href = "${ctx}/defect/exportExcel?" + $.param(p);
    }

    function setFixed() {
        var nodes = $("#dg").datagrid('getSelections'), ids = new Array();
        if (nodes && nodes.length > 0) {
            for (var i = 0; i < nodes.length; i++) {
                ids.push(nodes[i].id);
            }
        }
        if (nodes && nodes.length > 0) {
            if (showConfirm("提示", "批量设置为已修复状态吗？", function () {
                        $.post("${ctx}/defect/setFixed", {ids: ids}, function (data) {
                            if (data.success) {
                                $("#dg").datagrid('reload');
                            } else {
                                showMsg("提示", "操作出错", true);
                            }
                        });

                    }));
        } else {
            showMsg("提示", "请选择要设置为已修复的缺陷", true);
        }
    }
</script>
</body>
</html>

