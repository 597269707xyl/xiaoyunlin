<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="height: 300px">
    <table id="dg-recv-queue-select"></table>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onRecvQueueSelectOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onRecvQueueSelectCancel();">取消</a>
</div>
<script>
    var systemInstanceId = 0;
    function setData(instanceId){
        if(typeof (instanceId) == 'undefined' || instanceId == ''){
            instanceId = 0;
        }
        systemInstanceId = instanceId;
        $("#dg-recv-queue-select").datagrid({
            fit:true,
            fitColumns: true,
            url:'${ctx}/sim/instance/selectRecvQueueData?instanceId='+instanceId,
            height: 300,
//            singleSelect:true,
            pagination: false,
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {field: 'queueNameRecv', title: 'MQ名称', width: 100},
                {field: 'queueManagerRecv', title: '队列管理器名称', width: 100},
                {field: 'hostNameRecv', title: 'MQIp地址', width: 100},
                {field: 'receiveQueueRecv', title: '接收队列', width: 100}
            ]]
        });
    }
    function onRecvQueueSelectOk(){
        var rows = $("#dg-recv-queue-select").datagrid('getSelections');
        if(rows.length == 0){
            showMsg("提示","请选择要添加的记录",true);
            return;
        }
        if(systemInstanceId == 0){
            $('#dg-recv-queue').datagrid('loadData', rows);
            $('#recvQueueList').textbox('setValue', JSON.stringify(rows));
            onRecvQueueSelectCancel();
        } else {
            var url = '${ctx}/sim/instance/addRecvQueueDataList?instanceId='+systemInstanceId;
            if(rows.length == 1){
                url = '${ctx}/sim/instance/addRecvQueueData?instanceId='+systemInstanceId;
                rows = rows[0];
            }
            $.ajax({
                url: url,
                data: rows,
                type: 'POST',
                success: function(data){
                    if(data.success){
                        $('#dg-recv-queue').datagrid({url: '${ctx}/sim/instance/recvQueueData?instanceId='+systemInstanceId});
                        onRecvQueueSelectCancel();
                    } else {
                        showMsg("提示","添加失败",true);
                    }
                }
            })
        }
    }
    function onRecvQueueSelectCancel(){
        $('#window-recv-queue-list').window('close');
    }
</script>
