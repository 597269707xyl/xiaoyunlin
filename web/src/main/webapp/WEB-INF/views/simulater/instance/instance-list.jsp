<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <link href="${ctx}/asset/css/style.css" rel="stylesheet" type="text/css"/>
    <script>
        $(function () {
            $('#dg').datagrid({
                height: $(window).height() -80,
                fitColumns: true,
                url: '${ctx}/generic/commonQuery/simSystemInstance',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'name', title: '仿真实例', width: 100},
                    {field: 'simSystem', title: '依赖仿真系统', width: 100,
                        formatter:function(value,row,index){
                            if (row.simSystem){
                                return row.simSystem.name;
                            }else {
                                return '';
                            }
                        }},
                    {field: 'testEnvironmentDis', title: '所属环境', width: 50},
                    {field: 'adapter', title: '适配器', width: 100,
                        formatter:function(value,row,index){
                            if (row.adapter){
                                return row.adapter.name;
                            }else {
                                return '';
                            }
                        }},
                    {field: 'createTime', title: '创建日期', width: 100},
                    {field: 'descript', title: '描述', width: 100}
                ]]
            });
            $("#testEnvironment1").combobox({
                url:'${ctx}/generic/dict/TEST_ENVIRONMENT_LIST',
                valueField:'id',
                textField:'text',
                panelHeight:'auto',
                limitToList:true
            });
        });
        function onSearch() {

            var insName = $("#simInsName").textbox("getValue");
            var testEnvironment = $('#testEnvironment1').combobox('getValue');
            var p = {};
            if (insName != null && insName != "") {
                p['name|like'] = insName;
            }
            if(testEnvironment != null && testEnvironment != ""){
                p['testEnvironment'] = testEnvironment;
            }
            $("#dg").datagrid('load', p);
        }

        function addOrUpdate(type){
            var title = "新增";
            var param = {action: type};
            if (type == 'update'){
                var rows = $("#dg").datagrid('getSelections');
                if (rows == null || rows.length != 1) {
                    showMsg("提示", "请选择一条记录进行修改", true);
                    return;
                }
                var row = $("#dg").datagrid('getSelected');
                if (row == null){
                    showMsg("提示","请选择要修改的实例",true);
                    return;
                }else {
                    param.id = row.id;
                    title = "编辑";
                }
            }else {
                title = "新增";
            }
            commonShowWindow(param,title,"${ctx}/sim/instance/add",580,260,true,false,false);
        }

        function del(){
            var nodes = $("#dg").datagrid('getSelections'),ids = new Array();
            if(nodes && nodes.length>0){
                for(var i= 0;i<nodes.length;i++){
                    ids.push(nodes[i].id);
                }
            }
            if(nodes && nodes.length>0){
                if (showConfirm("提示","确定要删除实例吗？",function(){
                            $.post("${ctx}/sim/instance/del",{ids:ids},function(data){
                                if(data.success){
                                    $("#dg").datagrid('reload');
                                }else{
                                    showMsg("提示",data.msg,true);
                                }
                            });

                        }));
            }else{
                showMsg("提示","请选择要删除的实例",true);
            }
        }
        function copy(){
            var rows = $("#dg").datagrid('getSelections');
            if (rows == null || rows.length != 1) {
                showMsg("提示", "请选择一个实例进行复制", true);
                return;
            }
            var row = $("#dg").datagrid('getSelected');
            if (row == null){
                showMsg("提示","请选择要复制的实例",true);
                return;
            }
            var param = {};
            param.id = row.id;
            param.action = 'update';
            commonShowWindow(param,'实例复制',"${ctx}/sim/instance/copy",620,200,true,false,false);
        }

        function setSysinsConf(){
            var rows = $("#dg").datagrid('getSelections');
            if (rows == null || rows.length != 1) {
                showMsg("提示", "请选择一个实例", true);
                return;
            }
            var row = $("#dg").datagrid('getSelected');
            if (row == null){
                showMsg("提示","请选择要设置参数的实例",true);
                return;
            }
            var param = {};
            param.id = row.id;
            $('#myWindow').window({
                title: "设置实例参数",
                width: 530,
                height: 430,
                href: '${ctx}/sim/instance/paramList',
                modal: true,
                minimizable: false,
                maximizable: false,
                shadow: false,
                cache: false,
                closed: false,
                collapsible: false,
                resizable: true,
                loadingMessage: '正在加载数据，请稍等......',
                onLoad: function () {
                    setData(param)
                },
                onClose: function () {
                }
            });
        }

    </script>
</head>
<body>
<div class="toolBar">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addOrUpdate('add');">增加</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="del();">删除</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="addOrUpdate('update');">修改</a>
                <%--<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="copy();">复制实例</a>--%>
            </td>
        </tr>
    </table>
    <form id="queryForm">
        <table>
            <tr>
                <%--<td>仿真系统:</td>--%>
                <%--<td>--%>
                    <%--<input class="easyui-combobox" id="simSys" name="simSys" style="width: 100px"--%>
                           <%--valueField="id" textField="text" url="${ctx}/sim/message/getAllsim"  onenter="onSearch"/>--%>
                <%--</td>--%>
                <td>仿真实例名称:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="simInsName" name="simInsName" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                    <td>所属环境:</td>
                    <td><input id="testEnvironment1" name="testEnvironment" style="width: 120px" onenter="onSearch"/></td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>
            </tr>
        </table>
    </form>
</div>
<table id="dg"></table>
<div id="window-paramset"></div>
<div id="window-set-param"></div>
<div id="window-recv-queue"></div>
<div id="window-recv-queue-add"></div>
<div id="window-recv-queue-list"></div>
</body>
</html>
