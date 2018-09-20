<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<table align="center">
    <tr>
        <td>
            缓冲时间:
            <input id="internalTime" class="easyui-textbox" style="width: 120px;">单位:秒
        </td>
    </tr>
</table>

</form>

<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onInternalOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onInternalCancel();">取消</a>
</div>
<script>

    function onInternalOk(){
        var internalTime = $("#internalTime").textbox("getValue");
        if(internalTime == ''){
            internalTime = 0;
        }
        var id = '${id}';
        $.messager.progress();
        $.ajax({
            url:"${ctx}/sim/instance/server/setInternalTime",
            type: 'post',
            data:{id:id, internalTime:internalTime},
            success: function (data) {
                $.messager.progress('close');
                if(data.success){
                    showMsg("提示", data.msg, true);
                    $("#dg").datagrid('reload');
                    onInternalCancel();
                } else {
                    showMsg("提示", data.msg, true)
                }
            }
        });
    }
    function onInternalCancel(){
        $('#myWindow').window('close');
    }
</script>