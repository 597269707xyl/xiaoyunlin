<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="form_queue" class="easyui-form">
    <input type="hidden" id="id" name="id"/>
    <div class="popup">
        <table>
            <tr>
                <td>ip:</td>
                <td><input class="easyui-textbox" id="ip" name="ip" required="true"/></td>
            </tr>
            <tr>
                <td>端口:</td>
                <td><input class="easyui-textbox" id="port" name="port"/></td>
            </tr>
            <tr>
                <td>队列管理名称:</td>
                <td><input class="easyui-textbox" id="queueMgr" name="queueMgr"/></td>
            </tr>
            <tr>
                <td>通道名称:</td>
                <td><input class="easyui-textbox" id="channel" name="channel"
                /></td>
            </tr>
            <tr>
                <td>编码字符集:</td>
                <td><input class="easyui-textbox" id="ccsid" name="ccsid"/></td>
            </tr>
            <tr>
                <td>队列:</td>
                <td><input class="easyui-textbox" id="queue" name="queue"/></td>
            </tr>
            <tr>
                <td>用户名:</td>
                <td><input class="easyui-textbox" id="user" name="user"/></td>
            </tr>
            <tr>
                <td>密码:</td>
                <td><input class="easyui-textbox" id="password" name="password"/></td>
            </tr>
        </table>
    </div>
    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOjbk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="cancel();">取消</a>
    </div>
</form>
<script>
    var queueType;
    function setData(type) {
        queueType = type;
    }
    function onOjbk() {
        var ip = $('#ip').val();
        var re =  /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        if(ip != '' && !re.test(ip)){
            showMsg("提示","请正确输入服务端IP地址",true);
            return;
        }
        var port = $('#port').val();
        var queueMgr = $('#queueMgr').val();
        var channel = $('#channel').val();
        var ccsid = $('#ccsid').val();
        var queue = $('#queue').val();
        var user = $('#user').val();
        var password = $('#password').val();
        $("#datagrid_"+queueType).datagrid('appendRow', {
            ip: ip,
            port: port,
            queue: queue,
            queueMgr: queueMgr,
            channel: channel,
            ccsid: ccsid,
            user: user,
            password:password
        });
        $('#win').window('close');
    }
    function cancel() {
        $('#win').window('close');
    }
</script>
