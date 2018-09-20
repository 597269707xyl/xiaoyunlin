<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<table align="center">
    <tr>
        <td></td>
        <td>来账应答方式:</td>
        <td>
            <input class="easyui-combobox" type="text" id="setReply" style="width: 150px" value="1"
                   data-options="valueField: 'id',textField: 'text',panelHeight:'auto',
                           data: [{id: '1',text: '自动'},{id: '0',text: '手动'}]"/>
        </td>
    </tr>
</table>

</form>

<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onSetReplyOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onSetReplyCancel();">取消</a>
</div>
<script>
    function onSetReplyOk(){
        var setReply = $("#setReply").combobox("getValue");
        if(setReply == ''){
            setReply = true;
        }
        var id = '${id}';
        $.messager.progress();
        $.ajax({
            url:"${ctx}/sim/instance/server/setReply",
            type: 'post',
            data:{id:id,responseModel:setReply},
            success: function (data) {
                $.messager.progress('close');
                if(data.success){
                    onSetReplyCancel();
                    $("#dg").datagrid('reload');
                    showMsg("提示", data.msg, true);
                } else {
                    showMsg("提示", data.msg, true)
                }
            }
        });
    }
    function onSetReplyCancel(){
        $('#myWindow').window('close');
    }
</script>