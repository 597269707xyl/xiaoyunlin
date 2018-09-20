<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post">
    <input type="hidden" id="id" name="id"/>
    <div class="popup">
        <table>
            <tr>
                <td  class="popup_label">组成类型:</td>
                <td>
                    <input class="easyui-combobox" id="typeComp" name="typeComp"
                           data-options="valueField:'id',textField:'text',limitToList:'true',url:'${ctx}/generic/dict/MSG_FORMAT_COMP',panelHeight:'auto',required:true"
                           onenter="onSearch"/>
                </td>
            </tr>
            <tr>
                <td  class="popup_label">序号:</td>
                <td><input class="easyui-textbox" id="seqNo" name="seqNo" data-options="required:true"/></td>
            </tr>
            <tr>
                <td  class="popup_label">协议格式:</td>
                <td>
                    <input class="easyui-combobox" id="protocol" name="protocol"
                           data-options="valueField:'id',textField:'text',limitToList:'true',multiple:true,
                     url:'${ctx}/generic/dict/FIELD_PROPERTY',panelHeight:'auto',required:true"
                           onenter="onSearch"/>
                </td>
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
        formData.typeComp = $("#typeComp").combobox('getValue');
        formData.seqNo = $("#seqNo").textbox('getValue');
        return formData;
    }
    function setData(param) {
        if (param.action == 'update') {
            $.ajax({
                url: "${ctx}/msg/format/getComp/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
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
        var protocol = $("#protocol").combobox('getValues');
        $.ajax({
            url: '${ctx}/msg/format/addComp?formatId=' + '${formatId}&protocol=' + protocol,
            type: 'post',
            data: formData,
            cache: false,
            success: function (data) {
                if (data.success) {
                    onCancel();
                } else {
                    showMsg('温馨提示', data.msg, true);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.responseText);
                onCancel();
            }
        });
    }
    function onCancel() {
        $('#dlg-comps').window('close');
    }
</script>
