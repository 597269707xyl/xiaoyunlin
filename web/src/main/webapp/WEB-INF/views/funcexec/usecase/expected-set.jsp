<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form>
<table align="center">
    <tr>
        <td id="fieldName"></td>
    </tr>
    <tr>
        <td>
            <input id="expectedValue" type="text" style="width: 350px;" class="easyui-textbox" name="expectedValue" />
        </td>
    </tr>
</table>

</form>

<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onParamOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onParamCancel();">取消</a>
</div>

<script>
    var usecaseDataId;
    var fieldId;
    var fieldIdName;
    var type;
    function setParamData(param){
        usecaseDataId = param.usecaseDataId;
        fieldId = param.fieldId;
        fieldIdName = param.fieldIdName;
        type = param.type;
        $("#fieldName").html(param.fieldName+":");
    }

    function onParamOk(){
        var expectedValue = $('#expectedValue').val();
        if(expectedValue == ''){
            showMsg("提示","请输入预期值",true);
            return;
        }
        if(typeof (usecaseDataId) == 'undefined' || usecaseDataId == '' || usecaseDataId == 0){
            var data = {};
            data.fieldId = fieldId;
            data.nameZh = fieldIdName;
            data.expectedValue = expectedValue;
            var rows = $('#dg-expected-list').datagrid('getRows');
            rows.push(data);
            $('#dg-expected-list').datagrid('loadData', rows);
            onParamCancel();
        } else {
            $.post("${ctx}/func/exec/saveExpected", {type: type, usecaseDataId: usecaseDataId, fieldId: fieldId,fieldIdName:fieldIdName,expectedValue:expectedValue}, function (data) {
                if (data.success) {
                    onParamCancel();
                    $("#dg-expected-list").datagrid('reload');
                } else {
                    showMsg("提示","操作失败",true);
                }
            });
        }
    }
    function onParamCancel(){
        $('#window-expected-set').window('close');
    }


</script>
