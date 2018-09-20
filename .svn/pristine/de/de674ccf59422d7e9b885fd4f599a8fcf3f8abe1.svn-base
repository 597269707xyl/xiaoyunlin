<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="height: 300px">
    <table id="dg-recv-queue"></table>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onRecvDataOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onRecvDataCancel();">取消</a>
</div>
<script>
    var systemInstanceId=0;
    function setData(instanceId){
        var url = '${ctx}/sim/instance/recvQueueData?instanceId='+instanceId;
        if(typeof (instanceId) == 'undefined' || instanceId == ''){
            instanceId = 0;
            url = '';
        }
        systemInstanceId = instanceId;
        $("#dg-recv-queue").datagrid({
            fit:true,
            fitColumns: true,
            url: url,
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
            ]],
            toolbar: [{
                text:'添加',
                iconCls: 'icon-add',
                handler: function(){
                    $('#window-recv-queue-add').window({
                        title: '添加接收队列',
                        width: 540,
                        height: 280,
                        href: '${ctx}/sim/instance/addRecvQueue?instanceId='+instanceId,
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
                            setData(instanceId);
                        },
                        onClose: function () {
                        }
                    });
                }
            },'-',{
                text:'选择',
                iconCls: 'icon-add',
                handler: function(){
                    $('#window-recv-queue-list').window({
                        title: '选择已有队列',
                        width: 480,
                        height: 420,
                        href: '${ctx}/sim/instance/selectRecvQueue/',
                        modal: true,
                        minimizable: false,
                        maximizable: false,
                        shadow: false,
                        cache: false,
                        closed: false,
                        collapsible: false,
                        resizable: false,
                        loadingMessage: '正在加载数据，请稍等......',
                        onLoad: function () {
                            setData(instanceId)
                        },
                        onClose: function () {
                        }
                    });
                }
            },'-',{
                text:'删除',
                iconCls: 'icon-remove',
                handler: function(){
                    var rows = $("#dg-recv-queue").datagrid('getSelections');
                    var ids = new Array();
                    if(rows && rows.length>0){
                        for(var i= 0;i<rows.length;i++){
                            ids.push(rows[i].id);
                        }
                    }else {
                        showMsg("提示","请选择要删除的记录",true);
                        return;
                    }
                    if(systemInstanceId == 0){
                        var data = $('#dg-recv-queue').datagrid('getRows');
                        for (var i=0; i<data.length; i++){
                            for(var j=0; j<rows.length; j++){
                                if(data[i].queueNameRecv == rows[j].queueNameRecv){
                                    data.remove(i);
                                    i--;
                                    break;
                                }
                            }
                        }
                        $('#dg-recv-queue').datagrid('loadData', data);
                        $('#recvQueueList').textbox('setValue', JSON.stringify(data));
                    } else {
                        $.post("${ctx}/sim/instance/deleteRecvQueue", {ids: ids}, function (data) {
                            if (data.success) {
                                $("#dg-recv-queue").datagrid('reload');
                            } else {
                                showMsg("提示",data.msg,true);
                            }
                        });
                    }
                }
            }]
        });
        var jsonStr = $('#recvQueueList').textbox('getValue');
        if(jsonStr != '' && instanceId == 0){
            $('#dg-recv-queue').datagrid('loadData', JSON.parse(jsonStr));
        } else {
            $('#dg-recv-queue').datagrid({url: '${ctx}/sim/instance/recvQueueData?instanceId='+instanceId});
        }
    }
    Array.prototype.remove = function (dx) {
        if (isNaN(dx) || dx > this.length) { return false; }
        for (var i = 0, n = 0; i < this.length; i++) {
            if (this[i] != this[dx]) {
                this[n++] = this[i]
            }
        }
        this.length -= 1
    }
    function onRecvDataOk(){
        var rows = $('#dg-recv-queue').datagrid('getRows');
        var jsonStr = JSON.stringify(rows);
        $('#recvQueueList').textbox('setValue', jsonStr);
        onRecvDataCancel();
    }
    function onRecvDataCancel(){
        $('#window-recv-queue').window('close');
    }
</script>
