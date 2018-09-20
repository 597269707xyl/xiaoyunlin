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
                height: $(window).height() -48,
                fitColumns: true,
                rownumbers:true,
                url: '${ctx}/generic/commonQuery/simSystemInstance',
                singleSelect:true,
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'name', title: '仿真实例名称', width: 100},
                    {field: 'simSystem', title: '所属仿真系统', width: 100,
                        formatter:function(value,row,index){
                            if (row.simSystem){
                                return row.simSystem.name;
                            }else {
                                return '';
                            }
                        }},
                    {field: 'adapter', title: '适配器', width: 100,
                        formatter:function(value,row,index){
                            if (row.adapter){
                                return row.adapter.name;
                            }else {
                                return '';
                            }
                        }},
                    {field: 'state', title: '实例运行状态', width: 100,
                        formatter: function(value,row,index){
                            if(row.adapter){
                                if (row.adapter.adapterStatus==1){
                                    return '服务已启动成功';
                                }else {
                                    return '服务已停止';
                                }
                            } else {
                                return '未指定适配器';
                            }
                        }},
                    {field: 'opt', title: '操作', width: 100,
                        formatter: function(value,row,index){
                            return '<a href="#" onclick="startServer(\''+row.id+'\')">启动服务</a>' +
                                    ' | <a href="#" onclick="stopServer(\''+row.id+'\')">停止服务</a>';
                        }},
                    {field: 'responseModel', title: '来账应答方式', width: 100,
                        formatter:function(value,row,index){
                            if (row.adapter){
                                if(row.adapter.responseModel == 0){
                                    return '手动';
                                }
                                return '自动';
                            }else {
                                return '未指定适配器';
                            }
                        }},
                    {field: 'internalTime', title: '关联交易缓冲时间', width: 100,
                        formatter:function(value,row,index){
                            if (row.adapter){
                                var internalTime = 0;
                                if(row.adapter.internalTime != null){
                                    internalTime = row.adapter.internalTime;
                                }
                                return internalTime + '(毫秒)';
                            }else {
                                return '未指定适配器';
                            }
                        }}
                ]]
            });
        });
        function onSearch() {
            var insName = $("#simInsName").textbox("getValue");
            var p = {};
            if (insName != null && insName != "") {
                p['name|like'] = insName;
            }
            $("#dg").datagrid('load', p);
        }

        function setReply(){
            var row = $("#dg").datagrid("getSelected");
            if(row){
                showWindow("设置来账应答方式", "${ctx}/sim/instance/server/setReply?id="+row.id, 320, 160, true, false, false);
            } else {
                showMsg("提示", "请选择一条记录!", true)
            }
        }

        function setInternalTime(){
            var row = $("#dg").datagrid("getSelected");
            if(row){
                showWindow("设置关联交易缓冲时间", "${ctx}/sim/instance/server/setInternalTime?id="+row.id, 320, 160, true, false, false);
            } else {
                showMsg("提示", "请选择一条记录!", true)
            }
        }

        function startServer(id){
            if (showConfirm("提示","确定要启动服务吗？",function(){
                        $.post("${ctx}/sim/instance/server/startServer",{id:id},function(data){
                            if (data.success){
                                alert('启动命令发送成功，请刷新查看实例运行状态');
                                setTimeout(function(){
                                    $("#dg").datagrid('reload');
                                },1000);

                            }else {
                                showMsg("提示",data.msg,true);
                            }
                        });
                    }));

        }

        function stopServer(id){
            if (showConfirm("提示","确定要停止服务吗？",function(){
                        $.post("${ctx}/sim/instance/server/stopServer",{id:id},function(data){
                            if (data.success){
                                alert('停止命令发送成功，请刷新查看实例运行状态');
                                setTimeout(function(){
                                    $("#dg").datagrid('reload');
                                },1000);
                            }else {
                                showMsg("提示",data.msg,true);
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
                <td>仿真实例名称:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="simInsName" name="simInsName" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                       onclick="setReply();">设置来账应答方式</a></td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                       onclick="setInternalTime();">设置关联交易缓冲时间</a></td>
            </tr>
        </table>
    </form>
</div>
<table id="dg"></table>
</body>
</html>
