<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<table>
    <tr>
        <td>当前<a id="nowMessageCode"></a>报文域：<input id="destFieldList" style="width: 430px;"></td>
    </tr>
    <tr>
        <td>目标报文：<input id="srcMsg" style="width: 430px;"></td>
    </tr>
    <tr>
        <td>类型：<input id="srcSendRecv" style="width: 430px;"></td>
    </tr>
    <tr>
        <td>目标报文域：<input id="srcFieldList" style="width: 430px;"></td>
    </tr>
</table>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
       onclick="onAddRuleOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
       onclick="onAddRuleCancel();">取消</a>
</div>
<script>
    var Id;
    var index;
    var usecaseDataId;
    var srcMsgData;
    var destMsgCode;
    function setData(params){
        Id = params.messageId;
        index = params.index;
        usecaseDataId = params.usecaseDataId;
        srcMsgData = params.arrData;
        var messageCode = params.messageCode;
        if(typeof (messageCode) == 'undefined' || messageCode == ''){
            messageCode = srcMsgData[index].messageCode;
        }
        $("#nowMessageCode").html(messageCode);
        destMsgCode = params.messageCode;
        $("#destFieldList").combogrid({
            pannelWidth: 400,
            idField: 'fieldId',
            textField: 'nameZh',
            url: '${ctx}/func/exec/getFields?id='+Id,
            columns:[[
                {field:'fieldId', title: '域Id', width: 200},
                {field:'nameZh', title: '域名称', width: 100},
                {field:'msgCode', title: '报文编号', width: 100}
            ]]
        });
        $("#srcMsg").combogrid({
            data: srcMsgData,
            idField: 'index',
            textField: 'messageCode',
            panelHeight:'auto',
            columns:[[
                {field: 'messageId', title: 'id', hidden: true},
                {field:'messageCode', title: '报文名称', width: 300},
                {field:'index', title: '条数', width: 100}
            ]],
            onSelect: function(){
                var g = $('#srcMsg').combogrid('grid');
                var srcData = g.datagrid('getSelected');
                $("#srcSendRecv").combobox({
                    data: [{id: 'send', text: '发送'},{id: 'recv', text: '应答'}],
                    valueField: 'id',
                    textField: 'text',
                    panelHeight:'auto',
                    onSelect: function (record) {
                        if(record.id == 'send'){
                            var messageId = srcData.messageId;
                            $("#srcFieldList").combogrid({url: '${ctx}/func/exec/getFields?id='+messageId});
                        } else {
                            $("#srcFieldList").combogrid({url: '${ctx}/func/exec/recvFieldList?id='+Id});
                        }
                    },
                    onLoadSuccess:function(){
                        var data= $(this).combobox("getData");
                        if (data.length > 0) {
                            $('#srcSendRecv').combobox('select', data[0].id);
                        }
                    }
                });
            },
            onLoadSuccess:function(data){
                $('#srcMsg').combogrid('grid').datagrid('selectRecord',data.rows[0].messageId);
            }
        });
    }

    $("#srcFieldList").combogrid({
        pannelWidth: 400,
        idField: 'fieldId',
        textField: 'nameZh',
        columns:[[
            {field:'fieldId', title: '域Id', width: 200},
            {field:'nameZh', title: '域名称', width: 100},
            {field:'msgCode', title: '报文编号', width: 100}
        ]]
    });
    function onAddRuleOk() {
        var destFieldId = $("#destFieldList").combogrid('getValue');

        var srcSendRecv = $("#srcSendRecv").combobox('getValue');
        var srcFieldId = $("#srcFieldList").combogrid('getValue');
        if(destFieldId == ''){
            showMsg('提示', '当前报文域不能为空!', true);
            return;
        }
        if(srcSendRecv == ''){
            showMsg('提示', '请选择类型!', true);
            return;
        }
        var g = $('#srcMsg').combogrid('grid');
        var srcMsg = g.datagrid('getSelected');
        var srcMsgId = srcMsg.messageId;
        var srcMsgIndex = srcMsg.index;
        var srcMsgCode = srcMsg.messageCode;
        var data = {usecaseDataId: usecaseDataId, destFieldId: destFieldId, srcSendRecv: srcSendRecv, srcFieldId: srcFieldId, srcMsgId: srcMsgId, srcMsgIndex: srcMsgIndex};
        if(typeof (usecaseDataId) == 'undefined' || usecaseDataId == 0){
            var destFieldZh = $("#destFieldList").combogrid('getText');
            var from = $("#srcSendRecv").combobox('getText');
            var srcFieldZh = $("#srcFieldList").combogrid('getText');
            data.destFieldZh = destFieldZh;
            data.srcFieldZh = srcFieldZh;
            data.from = from;
            data.destMsgCode = destMsgCode;
            data.srcMsgCode = srcMsgCode;
            var rows = $('#dg-rule-list').datagrid('getRows');
            rows.push(data);
            $('#dg-rule-list').datagrid('loadData', rows);
            onAddRuleCancel();
        } else {
            data.evaluateType = srcMsgIndex;
            $.messager.progress();
            $.ajax({
                url: '${ctx}/func/exec/rules/addRule',
                type: "POST",
                data: data,
                success: function(data){
                    $.messager.progress('close');
                    if(data.success){
                        $('#dg-rule-list').datagrid('reload');
                        onAddRuleCancel();
                    } else {
                        showMsg('提示', '添加取值规则失败!', true);
                    }
                }
            });
        }
    }
    function onAddRuleCancel() {
        $('#window-rule-add').window('close');
    }
</script>