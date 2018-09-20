<%--
  Created by IntelliJ IDEA.
  User: huangbo
  Date: 2018/8/1
  Time: 15:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>缺陷统计</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <script src="${ctx}/asset/js/echarts.min.js"></script>
</head>
<body>
<div class="easyui-layout content" data-options="fit:true">
    <div class="topContent" data-options="region:'north'" style="height:62%;">
        <div class="toolBar">
            <form id="execForm" style="margin-bottom: 0px;">
                <table>
                    <tr>
                        <td>测试项:</td>
                        <td>
                            <input id="testItem" name="testItem" onclick="statistics();"/>
                        </td>
                        <td>创建时间:</td>
                        <td><input class="easyui-datetimebox" id="createFrom" name="createFrom" style="width: 150px"
                                   data-options="icons: [{
                    iconCls: 'icon-clear', handler: function (e) {
                        $(e.data.target).datetimebox('clear');
                     }
                }]">
                        </td>
                        <td>至：</td>
                        <td><input class="easyui-datetimebox" id="createTo" name="createTo" style="width: 150px"
                                   data-options="icons: [{
                    iconCls: 'icon-clear', handler: function (e) {
                        $(e.data.target).datetimebox('clear');
                     }
                }]"></td>
                        <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                               onclick="statistics();">统计</a></td>
                    </tr>
                </table>
            </form>
        </div>
        <div id="p1" class="easyui-panel" style="height: 94%">
            <%--<table id="dg"></table>--%>
        </div>
    </div>
    <div style="height:38%;" data-options="region:'center',title:'缺陷列表'">
        <table id="defect_dg" style="height:100%;"></table>
    </div>
</div>

<script>
    var selectType;
    var selectType1;
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
        /*        $('#dg').datagrid({
         fitColumns: true,
         singleSelect: true,
         collapsible: true,
         border: false,
         //url: '
        ${ctx}/defect/getStatistics',
         columns: [[
         {field: 'id', title: 'id', hidden: true},
         {field: 'projectName', title: '项目名称', width: 70},
         {field: 'itemName', title: '测试项', width: 170},
         {field: 'total', title: '缺陷数', width: 70},
         {
         field: 'partPercent', title: '总占比(%)', width: 70,
         formatter: function (val, rowData, rowIndex) {
         if (val != null)
         return val.toFixed(2);
         }
         },
         {
         field: 'fixPercent', title: '修复率(%)', width: 70,
         formatter: function (val, rowData, rowIndex) {
         if (val != null)
         return val.toFixed(2);
         }
         },
         {field: 'hLevel', title: '高(缺陷等级)', width: 70},
         {field: 'mLevel', title: '中(缺陷等级)', width: 70},
         {field: 'lLevel', title: '低(缺陷等级)', width: 70}
         ]]
         });*/
        $('#defect_dg').datagrid({
            //height: $(window).height() * 0.37,
            fitColumns: true,
            singleSelect: true,
            pagination: true,
            pageSize: 10,
            pageList: [10, 20, 30, 50],
            url: '${ctx}/defect/getDefectMatched',
            columns: [[
                {field: 'id', title: 'id', hidden: true},
                {field: 'abbreviation', title: '缺陷标识', width: 70},
                {
                    field: 'usecaseName', title: '用例标识', width: 70,
                    formatter: function (val, row) {
                        if (row.nxyFuncUsecase) return row.nxyFuncUsecase.no;
                    }
                },
                {
                    field: 'type', title: '导入类型', width: 30,
                    formatter: function (val, row) {
                        if (row.type == 'IMP') {
                            return '系统导入';
                        } else if (row.type == 'UD') {
                            return '自定义';
                        }
                    }
                },
                {
                    field: 'grade', title: '缺陷等级', width: 30,
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
                {
                    field: 'fixStatus', title: '修复状态', width: 30,
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
                    field: 'mark', title: '来往账状态', width: 30,
                    formatter: function (val, row) {
                        if (row.mark == 'send') {
                            return '往账';
                        } else if (row.mark == 'recv') {
                            return '来账';
                        }
                    }
                },
                {field: 'createTime', title: '创建时间', width: 70},
                {field: 'createUser', title: '创建人', width: 70},
                {field: 'fixTime', title: '修复时间', width: 70},
                {field: 'fixUser', title: '修复人', width: 70}
            ]]
        });
    });
    function statistics() {
        var testItem = $('#testItem').textbox('getValue');
        var createFrom = $('#createFrom').datebox("getValue");
        var createTo = $('#createTo').datebox("getValue");
        var p = {type: selectType};
        var t = {};
        if (createFrom != null && createFrom != "") {
            p['createFrom'] = createFrom;
            t['createTime|>='] = createFrom;
        }
        if (createTo != null && createTo != "") {
            p['createTo'] = createTo;
            t['createTime|<='] = createTo;
        }
        if (testItem != null && testItem != "") {
            p['id'] = testItem;
            if (selectType == 'project') {
                t['testProject.id'] = testItem;
            } else if (selectType == 'item') {
                t['nxyFuncItem.id'] = testItem;
            }

            $.get('${ctx}/defect/getStatistics',
                    p,
                    function (data) {
                        var box = $("#main");

                        var pieData1 = [
                            {
                                value: data.other,
                                name: '其他'
                            },
                            {
                                value: data.sum,
                                name: '符合条件'
                            }
                        ];
                        var pieData2 = [
                            {
                                value: data.lLevel,
                                name: '低'
                            },
                            {
                                value: data.mLevel,
                                name: '中'
                            },
                            {
                                value: data.hLevel,
                                name: '高'
                            }
                        ];
                        var pieData3 = [
                            {
                                value: data.unFixed,
                                name: '未修复'
                            },{
                                value: data.fixed,
                                name: '已修复'
                            }

                        ];

                        var pieChart = echarts.init(document.getElementById('p1'));
                        var pieOption = {
                            title: [{
                                text: '总占比',
                                subtext: '总用例数:' + data.total + '   符合条件用例数:' + data.sum,
                                x: '20%',
                                textAlign: 'center'
                            }, {
                                text: '缺陷等级比',
                                x: '50%',
                                subtext: '高:' + data.hLevel + '   中:' + data.mLevel + '   低:' + data.lLevel,
                                textAlign: 'center'
                            }, {
                                text: '修复比',
                                x: '80%',
                                subtext: '已修复:' + data.fixed + '   未修复:' + data.unFixed,
                                textAlign: 'center'
                            }],
                            tooltip: {formatter: "{b} : {c} ({d}%)"},
                            series: [{
                                type: 'pie', radius: '55%', center: ['20%', '50%']

                            }, {
                                type: 'pie', radius: '55%', center: ['50%', '50%']
                            },
                                {
                                    type: 'pie', radius: '55%', center: ['80%', '50%']
                                }]
                        };
                        if (data.sum != 0) {
                            pieOption.series[1].data = pieData2;
                            pieOption.series[2].data = pieData3;
                        }else {
                            pieOption.series[1].data = [];
                            pieOption.series[2].data = [];
                        }
                        pieOption.series[0].data = pieData1;
                        pieChart.setOption(pieOption);

                    });

        } else {
            showMsg("提示", "请选择测试项", true);
            return;
        }

        //$("#dg").datagrid('load', p);
        $("#defect_dg").datagrid('load', t);
    }
</script>
</body>
</html>


