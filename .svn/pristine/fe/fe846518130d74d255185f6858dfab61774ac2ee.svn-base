<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="height: 350px">
<table id="dg-sim-list"></table>
</div>
<script>
    function setData(param){
        $("#dg-sim-list").datagrid({
            fit:true,
            title: '依赖仿真实例列表',
            fitColumns: true,
            url:'${ctx}/proj/proj/getAllocatedSimulators?projectId='+param.id,
            height: 350,
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {field: 'name', title: '仿真实例', width: 150},
                {field: 'systemName', title: '所属仿真系统', width: 150,
                    formatter:function(value,row,index){
                        if (row.simSystem.name){
                            return row.simSystem.name;
                        }
                        return '';
                    }}
            ]],
            toolbar: [{
                text:'添加',
                iconCls: 'icon-add',
                handler: function(){
                    $('#window-sim-list').window({
                        title: '选择仿真实例',
                        width: 480,
                        height: 420,
                        href: '${ctx}/proj/proj/getSimulators',
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
                            $("#dg-sim-list").datagrid('reload');
                            $("#dg").datagrid('reload');
                        }
                    });
                }
            },'-',{
                text:'删除',
                iconCls: 'icon-remove',
                handler: function(){
                    var rows = $("#dg-sim-list").datagrid('getSelections');
                    var ids = new Array();
                    if(rows && rows.length>0){
                        for(var i= 0;i<rows.length;i++){
                            ids.push(rows[i].id);
                        }
                    }else {
                        showMsg("提示","请选择要删除的记录",true);
                        return;
                    }
                    var request = param.id;
                    $.post("${ctx}/proj/proj/deleteAllocatedSimulators", {projectId: param.id, ids: ids}, function (data) {
                        if (data.success) {
                            $("#dg-sim-list").datagrid('reload');
                            $("#dg").datagrid('reload');
                        } else {
                            showMsg("提示",data.msg,true);
                        }
                    });
                }
            }]
        });

    }
    function onSetCancel(){
        closeWindow();
    }

</script>
