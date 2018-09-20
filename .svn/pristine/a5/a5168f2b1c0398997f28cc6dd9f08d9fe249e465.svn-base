<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>项目进度统计</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <script src="${ctx}/asset/js/echarts.min.js"></script>
</head>
<body>
<div class="easyui-layout content" data-options="fit:true">
    <div class="toolBar">
        <form id="execForm" style="height: 3%">
            <table>
                <tr>
                    <td></td>
                    <td>测试项:</td>
                    <td>
                        <input id="testItem" name="testItem" onclick="simSearch();" data-options="icons: [{
                    iconCls: 'icon-clear', handler: function (e) {
                        $(e.data.target).combotree('clear');
                        selectType = null;
                    }
                }],"/>
                    </td>
                    <td>类型:</td>
                    <td>
                        <select id="mark" name="mark" class="easyui-combobox" style="width:180px;"
                                data-options="panelHeight:'auto'">
                            <option value="send">往账</option>
                            <option value="recv">来账</option>
                        </select>
                    </td>
                    <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                           onclick="showPie();">统计</a></td>
                </tr>
            </table>
        </form>
    </div>

    <div id="p1" class="easyui-panel" style="width: 100%;height: 97%">


        <div id="main" style="height: 100%">

            <%-- <table class="easyui-datagrid" id="dg">
             </table>--%>

        </div>
    </div>
</div>
<script>
    var selectType;
    $(function () {
        $("#testItem").combotree({
            url: '${ctx}/func/exec/tree?mark=send&caseType=t&id=-1&type=',
            valueField: 'id',
            textField: 'text',
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
        /*$('#dg').datagrid({
            //fit: true,
            height: $(window).height() - 80,
            fitColumns: true,
            singleSelect: true,
            pagination: false,
            pageSize: 20,
            pageList: [10, 20, 30, 50],
            //url: '${ctx}/proj/proj/getStatistics',
            columns: [[
                {field: 'id', title: 'id', hidden: true},
                {field: 'projectName', title: '项目名称', width: 70},
                {field: 'itemName', title: '测试项名称', width: 170},
                {field: 'total', title: '总案例数', width: 70},
                {field: 'suc', title: '通过案例数', width: 70},
                {field: 'fail', title: '未通过案例数', width: 70},
                {field: 'other', title: '未执行案例数', width: 70}
            ]]
        });*/
        /*$('#p1').panel({
         height: $(window).height() - 48
         })*/
    });
    function simSearch() {
        var testItem = $('#testItem').textbox('getValue');
        if (testItem != null && testItem != "") {
            var mark = $('#mark').combobox("getValue");
            var p = {id: testItem, id: testItem, mark: mark, type: selectType};
            $("#dg").datagrid('load', p);
        } else {
            showMsg("提示", "请选择测试项", true);
            return;
        }
    }

    function showPie() {
        var testItem = $('#testItem').textbox('getValue');
        if (testItem != null && testItem != "") {
            var mark = $('#mark').combobox("getValue");
            var p = {id: testItem, mark: mark, type: selectType};

            $.get('${ctx}/proj/proj/getStatistics', {
                id: testItem,
                mark: mark,
                type: selectType
            }, function (data) {
                var box = $("#main");
                var pieData = [];
                if (data.total != 0) {
                    pieData = [
                        {
                            value: data.other,
                            name: '未执行用例'
                        },
                        {
                            value: data.suc,
                            name: '通过用例'
                        },
                        {
                            value: data.fail,
                            name: '未通过用例'
                        }
                    ];
                } else {
                    pieData = [
                        {
                            value: data.total,
                            name: '总用例数'
                        }
                    ];
                }
                var pieChart = echarts.init(document.getElementById('p1'));
                var pieOption = {
                    title: {
                        text: data.projectName,
                        left: 'center'
                    },
                    tooltip: {formatter: "{b} : {c} ({d}%)"},
                    legend: {
                        bottom: 10,
                        left: 'center',
                        data: ['未通过用例', '通过用例', '未执行用例']
                    },
                    series: [{
                        name: data.projectName, type: 'pie', radius: '55%', center: ['50%', '50%']
                    }]
                };
                if (mark == 'send') {
                    var subtext = "用例总数(" + "往账" + "):" + data.total;
                } else {
                    var subtext = "用例总数:(" + "来账" + "):" + data.total;
                }

                if (data.itemName != null) {
                    subtext = data.itemName + "   " + subtext;
                }
                pieOption.title.subtext = subtext;
                pieOption.series[0].data = pieData;
                pieChart.setOption(pieOption);

            });
        } else {
            showMsg("提示", "请选择测试项", true);
            return;
        }
    }
</script>
</body>
</html>
