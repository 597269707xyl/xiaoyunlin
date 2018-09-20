<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post">
    <div class="popup">
        <input type="hidden" id="id" name="id"/>
        <input type="hidden" id="setType" name="setType"/>
        <table class="single">
            <tr>
                <td>${label}:</td>
                <td><input class="easyui-textbox" id="headname" name="headname" data-options="required:true"/></td>
            </tr>
            <tr>
                <td>报文类型:</td>
                <td>
                    <input class="easyui-combobox" id="type" name="type"
                           data-options="valueField:'id',textField:'text',limitToList:'true',url:'${ctx}/generic/dict/MSGTYPE',panelHeight:'auto',required:true"
                           onenter="onSearch"/>
                </td>
            </tr>
            <tr>
                <td>描述:</td>
                <td><input class="easyui-textbox" id="descript" name="descript" data-options="multiline:true"
                           style="height: 60px;"/></td>
            </tr>
        </table>
    </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onCancel();">取消</a>
    </div>
</form>

<script>

    function getData() {
        var formData = {};
        formData.id = $("#id").val();
        formData.setType = '${setType}';
        formData.name = $("#headname").textbox('getValue');
        formData.msgType = $("#type").combobox('getValue');
        formData.descript = $("#descript").textbox('getValue');
        return formData;
    }
    function setData(param) {
        if (param.action == 'update') {
            $.ajax({
                url: "${ctx}/msg/fieldset/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
                    $("#headname").textbox('setValue', vo.name);
                    $("#type").combobox('setValue', vo.msgType);
                    $("#setType").val(vo.setType);
                }
            });
        }
    }
    function onOk() {
        var form = $('#fm');
        var flag = form.form('validate');
        if (!flag) return;
        var formData;
        formData = getData();
        $.ajax({
            url: '${ctx}/msg/fieldset/add',
            type: 'post',
            data: formData,
            cache: false,
            success: function (data) {
                if (data.success) {
                    closeWindow();
                } else {
                    showMsg('温馨提示', data.msg, true);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.responseText);
                closeWindow();
            }
        });
    }
    function onCancel() {
        closeWindow();
    }
</script>
