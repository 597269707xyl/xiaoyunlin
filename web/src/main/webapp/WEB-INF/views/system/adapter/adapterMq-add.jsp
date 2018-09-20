<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" class="easyui-form" method="post">
    <input type="hidden" id="id" name="id"/>
    <div style="height:90%">
        <div class="popup" style="height: 20%">
            <table>
                <tr>
                    <td>适配器名称:</td>
                    <td><input class="easyui-textbox popup_input" id="name1" name="name" required="true"
                               /></td>
                    <td>成员行号:</td>
                    <td><input class="easyui-textbox popup_input" id="bankNo" name="bankNo"/></td>
                </tr>
                <tr>
                    <td>描述:</td>
                    <td><input class="easyui-textbox" id="description" name="description"
                               data-options="multiline:true"
                               /></td>
                </tr>
            </table>
        </div>
        <div style="height: 40%">
            <table id="datagrid_send"></table>
        </div>
        <div style="height: 40%">
            <table id="datagrid_recv"></table>
        </div>
    </div>
    <div style="text-align:center;padding:5px;height: 5%">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="closeWindow();">取消</a>
    </div>
</form>
<script>
    $("#datagrid_send").datagrid({
        fit: true,
        fitColumns: true,
        singleSelect: true,
        pagination: false,
        columns: [[
            {field: 'ck', checkbox: true},
            {field: 'id', title: 'id', hidden: true},
            {field: 'ip', title: 'ip', width: 100},
            {field: 'port', title: '端口', width: 70},
            {field: 'queueMgr', title: '队列管理名称', width: 130},
            {field: 'channel', title: '通道名称', width: 100},
            {field: 'ccsid', title: '编码字符集', width: 120},
            {field: 'queue', title: '队列', width: 100},
            {field: 'user', title: '用户名', width: 100}
        ]],
        toolbar: [{
            text: '添加发送队列',
            iconCls: 'icon-add',
            handler: function () {
                $('#win').window({
                    title: "添加发送队列",
                    width: 400,
                    height: 350,
                    href: '${ctx}/sys/adapter/addQueueList',
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
                        setData("send");
                    },
                    onClose: function () {
                    }
                });

            }
        }, '-', {
            text: '删除发送队列',
            iconCls: 'icon-remove',
            handler: function () {
                var rows = $("#datagrid_send").datagrid('getSelections');
                if (rows && rows.length > 0) {
                    for (var i = 0; i < rows.length; i++) {
                        var index = $("#datagrid_send").datagrid('getRowIndex', rows[0]);
                        $('#datagrid_send').datagrid('deleteRow', index);
                    }
                } else {
                    showMsg("提示", "请选择要删除的记录", true);
                    return;
                }
            }
        }]
    });

    $("#datagrid_recv").datagrid({
        fit: true,
        fitColumns: true,
        singleSelect: true,
        pagination: false,
        columns: [[
            {field: 'ck', checkbox: true},
            {field: 'id', title: 'id', hidden: true},
            {field: 'ip', title: 'ip', width: 100},
            {field: 'port', title: '端口', width: 70},
            {field: 'queueMgr', title: '队列管理名称', width: 130},
            {field: 'channel', title: '通道名称', width: 100},
            {field: 'ccsid', title: '编码字符集', width: 120},
            {field: 'queue', title: '队列', width: 100},
            {field: 'user', title: '用户名', width: 100}
        ]],
        toolbar: [{
            text: '添加接收队列',
            iconCls: 'icon-add',
            handler: function () {
                $('#win').window({
                    title: "添加接收队列",
                    width: 400,
                    height: 350,
                    href: '${ctx}/sys/adapter/addQueueList',
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
                        setData("recv");
                    },
                    onClose: function () {
                    }
                });
            }
        }, '-', {
            text: '删除接收队列',
            iconCls: 'icon-remove',
            handler: function () {
                var rows = $("#datagrid_recv").datagrid('getSelections');
                if (rows && rows.length > 0) {
                    for (var i = 0; i < rows.length; i++) {
                        var index = $("#datagrid_recv").datagrid('getRowIndex', rows[0]);
                        $('#datagrid_recv').datagrid('deleteRow', index);
                    }
                } else if (vo.responseModel == false){
                    showMsg("提示", "请选择要删除的记录", true);
                    return;
                }
            }
        }]
    });
    function setData(param) {
        if (param.action == 'update') {
            $.ajax({
                url: "${ctx}/sys/adapter/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
                }
            });
            $("#datagrid_send").datagrid({
                url: '${ctx}/sys/adapter/getSendQueue/' + param.id
            });
            $("#datagrid_recv").datagrid({
                url: '${ctx}/sys/adapter/getRecvQueue?id=' + param.id
            })
        }
    }

    function getData() {
        var formData = {};
        formData.id = $('#id').val();
        formData.name = $('#name1').val();
        formData.bankNo = $('#bankNo').val();
        formData.description = $('#description').val();
        formData.sendQueue = JSON.stringify($('#datagrid_send').datagrid("getRows"));
        formData.recvQueue = JSON.stringify($('#datagrid_recv').datagrid("getRows"));
        return formData;
    }
    function onOk() {
        commonSubmitForm('${ctx}/sys/adapter/add/MQ');
    }
</script>
