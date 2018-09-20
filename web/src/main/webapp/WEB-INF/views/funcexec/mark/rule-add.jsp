<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<table>
    <tr>
        <td>当前应答报文<a id="nowMessageCode"></a>报文域：<input id="destFieldList" style="width: 430px;"></td>
    </tr>
    <tr>
        <td>请求报文域：<input id="srcFieldList" style="width: 430px;"></td>
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
        $("#srcFieldList").combogrid({
            pannelWidth: 400,
            idField: 'fieldId',
            textField: 'nameZh',
            url: '${ctx}/func/mark/recvFieldList?id='+Id,
            columns:[[
                {field:'fieldId', title: '域Id', width: 200},
                {field:'nameZh', title: '域名称', width: 100},
                {field:'msgCode', title: '报文编号', width: 100}
            ]]
        });
    }

    function onAddRuleOk() {
        var destFieldId = $("#destFieldList").combogrid('getValue');

        var srcFieldId = $("#srcFieldList").combogrid('getValue');
        if(destFieldId == ''){
            showMsg('提示', '当前报文域不能为空!', true);
            return;
        }
        var data = {usecaseDataId: usecaseDataId, destFieldId: destFieldId, srcFieldId: srcFieldId};
        if(typeof (usecaseDataId) == 'undefined' || usecaseDataId == 0){
            var destFieldZh = $("#destFieldList").combogrid('getText');
            var srcFieldZh = $("#srcFieldList").combogrid('getText');
            data.destFieldZh = destFieldZh;
            data.srcFieldZh = srcFieldZh;
            var rows = $('#dg-rule-list').datagrid('getRows');
            rows.push(data);
            $('#dg-rule-list').datagrid('loadData', rows);
            onAddRuleCancel();
        } else {
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