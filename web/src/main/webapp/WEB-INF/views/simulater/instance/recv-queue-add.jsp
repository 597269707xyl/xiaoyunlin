<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="recv-queue-add" class="easyui-form" method="post">
    <input type="hidden" name="instanceId" value="${instanceId}"/>
    <div class="popup">
        <table class="single">
            <tr>
                <td>MQ名称:</td>
                <td><input class="easyui-textbox" id="queueNameAdd" required/></td>
                <td>队列管理名称:</td>
                <td><input class="easyui-textbox" id="queueManagerAdd" /></td>
            </tr>
            <tr>
                <td>MQIP地址:</td>
                <td><input class="easyui-textbox" id="hostNameAdd" required/></td>
                <td>MQ端口:</td>
                <td><input class="easyui-numberbox" id="portAdd" required/></td>
            </tr>
            <tr>
                <td>通道名称:</td>
                <td><input class="easyui-textbox" id="channelAdd" /></td>
                <td>编码字符集:</td>
                <td><input class="easyui-numberbox" id="ccsidAdd" /></td>
            </tr>
            <tr>
                <td>MQ用户名:</td>
                <td><input class="easyui-textbox" id="userIdAdd" /></td>
                <td>MQ密码:</td>
                <td><input class="easyui-textbox" id="passwordAdd" /></td>
            </tr>
            <tr>
                <td>接收队列:</td>
                <td><input class="easyui-textbox" id="receiveQueueAdd" required/></td>
            </tr>
        </table>
    </div>
    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onRecvQueueAddOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onRecvQueueAddCancel();">取消</a>
    </div>
</form>
<script>
    function getRecvData() {
        var formData = {};
        formData.queueNameRecv = $('#queueNameAdd').textbox('getValue');
        formData.receiveQueueRecv = $('#receiveQueueAdd').textbox('getValue');
        formData.queueManagerRecv = $('#queueManagerAdd').textbox('getValue');
        formData.hostNameRecv = $('#hostNameAdd').textbox('getValue');
        formData.portRecv = $('#portAdd').numberbox('getValue');
        formData.channelRecv = $('#channelAdd').textbox('getValue');
        formData.ccsidRecv = $('#ccsidAdd').numberbox('getValue');
        formData.userIdRecv = $('#userIdAdd').textbox('getValue');
        formData.passwordRecv = $('#passwordAdd').textbox('getValue');
        return formData;
    }
    function onRecvQueueAddOk() {
        var form = $('#recv-queue-add');
        var flag = form.form('validate');
        if (!flag) return;
        var re =  /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        var hostName = $("#hostNameAdd").textbox('getValue');
        if(hostName != '' && !re.test(hostName)){
            showMsg("提示","请正确输入MQIP地址",true);
            return;
        }
        var instanceId = '${instanceId}';
        if(instanceId == 0){
            var data = getRecvData();
            var rows = $('#dg-recv-queue').datagrid('getRows');
            rows.push(data);
            $('#dg-recv-queue').datagrid('loadData', rows);
            onRecvQueueAddCancel();
        } else {
            var data = getRecvData();
            data.instanceId = instanceId;
            $.ajax({
                url: '${ctx}/sim/instance/addRecvQueueData',
                data: data,
                type: 'POST',
                success: function(data){
                    if(data.success){
                        $('#dg-recv-queue').datagrid({url: '${ctx}/sim/instance/recvQueueData?instanceId='+instanceId});
                        onRecvQueueAddCancel();
                    } else {
                        showMsg("提示","添加失败",true);
                    }
                }
            })
        }
    }
    function onRecvQueueAddCancel() {
        $('#window-recv-queue-add').window('close');
    }
</script>
