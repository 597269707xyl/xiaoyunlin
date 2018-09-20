<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="replyParamForm">
<table align="center">
    <tr>
        <td>
            <input id="param" class="easyui-combobox" name="param"
                   data-options="
           valueField:'id',
           textField:'text',
           value:'response',
           required: true,
           limitToList:true,
           data:[{
			id: 'response',
			text: '应答'
            },{
                id: 'transefer',
                text: '转发'
            }]
           ">
        </td>
    </tr>
</table>

</form>

<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onParamOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onParamCancel();">取消</a>
</div>

<script>
    var requestId;
    var responseIds;
    function setParamData(param){

        requestId = param.requestId;
        responseIds = param.responsIds;

    }

    function onParamOk(){
        var flag = $('#replyParamForm').form('validate');
        if (!flag) return;
        var p = $('#param').combobox('getValue');
        $.post("${ctx}/sim/instance/message/addResponses", {requestId: requestId, responseIds: responseIds,param:p}, function (data) {
            if (data.success) {
                onParamCancel();
            } else {
                showMsg("提示","操作失败",true);
            }
        });
    }
    function onParamCancel(){
        $('#window-reply-param').window('close');
    }


</script>
