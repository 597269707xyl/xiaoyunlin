<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>缺陷统计</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
</head>
<body>
<div class="easyui-tabs" style="width: 100%;height:auto;">
    <div title="项目缺陷统计">
        <div class="toolBar">
            <form id="execForm" style="margin-bottom: 0px;">
                <table>
                    <tr>
                        <td>测试项:</td>
                        <td>
                            <input id="testItem" name="testItem" onclick="simSearch();"/>
                        </td>
                        <td>创建时间:</td>
                        <td><input class="easyui-datetimebox" id="sendBegin" name="sendBegin" style="width: 150px"></td>
                        <td>至：</td>
                        <td><input class="easyui-datetimebox" id="sendEnd" name="sendEnd" style="width: 150px"></td>
                        <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                               onclick="simSearch();">统计</a></td>
                    </tr>
                </table>
            </form>
        </div>
        <div style="height: auto">
            <table  class="easyui-datagrid" id="dg">
                <thead>
                <tr>
                    <th data-options="field:'id',checkbox:true"></th>
                    <th data-options="field:'itemName',width:100">测试项</th>
                    <th data-options="field:'projectName',width:100">项目名称</th>
                    <th data-options="field:'count',width:100">缺陷数</th>
                    <th data-options="field:'rate',width:100">占比（%）</th>
                    <th data-options="field:'rate1',width:100">修复率（%）</th>
                    <th data-options="field:'time',width:100">创建时间</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
    <div title="缺陷严重程度统计">
        <div class="toolBar">
            <form id="userForm" style="margin-bottom: 0px;">
                <table>
                    <tr>
                        <td>测试项:</td>
                        <td>
                            <input id="testItem1" name="testItem" style="width: 150px" onclick="userSearch();"/>
                        </td>
                        <td>创建时间:</td>
                        <td><input class="easyui-datetimebox" id="userBegin" name="userBegin" style="width: 150px"></td>
                        <td>至：</td>
                        <td><input class="easyui-datetimebox" id="userEnd" name="userEnd" style="width: 150px"></td>
                        <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                               onclick="userSearch();">统计</a></td>
                    </tr>
                </table>
            </form>
        </div>
        <div style="height: auto">
            <table class="easyui-datagrid" id="rdg">
                <thead>
                <tr>
                    <th data-options="field:'id',checkbox:true"></th>
                    <th data-options="field:'itemName',width:100">测试项</th>
                    <th data-options="field:'projectName',width:100">项目名称</th>
                    <th data-options="field:'h',width:100">高</th>
                    <th data-options="field:'z',width:100">中</th>
                    <th data-options="field:'d',width:100">低</th>
                    <th data-options="field:'count',width:100">合计</th>
                    <th data-options="field:'time',width:100">创建时间</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script>
    $(function () {
        $("#testItem").combotree({
            url:'${ctx}/func/exec/tree?mark=send&caseType=t&id=-1&type=',
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
        $("#testItem1").combotree({
            url:'${ctx}/func/exec/tree?mark=send&caseType=t&id=-1&type=',
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
        $('#dg').datagrid({
            //fit: true,
            height: $(window).height() - 80,
            fitColumns: true,
            singleSelect: true,
            pagination: true,
            pageSize: 20,
            pageList: [10, 20, 30, 50],
            <%--url: "${ctx}/test/count/query"--%>
        });
        var data1 = {'total':'1','rows':[{'itemName':'宽城村镇银行','projectName':'业务联调测试', 'count':'10','rate':'10%','rate1':'2%','time':'2018-03-01'}]};
        $('#dg').datagrid('loadData', data1);
        $('#rdg').datagrid({
            //fit: true,
            height: $(window).height() - 80,
            fitColumns: true,
            singleSelect: true,
            pagination: true,
            pageSize: 20,
            pageList: [10, 20, 30, 50],
            <%--url: "${ctx}/test/count/userquery"--%>
        });
        var data = {'total':'1','rows':[{'itemName':'宽城村镇银行','projectName':'业务联调测试', 'g':'2','z':'3','d':'5','count':'10','time':'2018-03-01'}]};
        $('#rdg').datagrid('loadData', data);
    });
    function simSearch() {
        var ps = {};
        var simInstance = $('#simInstance').textbox('getValue');
        var sendBegin = $('#sendBegin').datetimebox('getValue');
        var sendEnd = $('#sendEnd').datetimebox('getValue');
        ps.simInstance=simInstance;
        ps.sendBegin=sendBegin;
        ps.sendEnd=sendEnd;
        $("#dg").datagrid('load', ps);
    }
    function userSearch() {
        var pu={};
        var uname = $('#uname').textbox('getValue');
        var userBegin = $('#userBegin').datetimebox('getValue');
        var userEnd = $('#userEnd').datetimebox('getValue');
        pu.uname=uname;
        pu.userBegin=userBegin;
        pu.userEnd=userEnd;
        $("#rdg").datagrid('load',pu);
    }
</script>
</body>
</html>

