<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post">
    <input type="hidden" id="id" name="id"/>
    <div class="popup">
    <table>
        <tr>
            <td>域id:</td>
            <td><input class="easyui-textbox" id="fieldId" name="fieldId" data-options="readonly:'true'"/></td>
        </tr>
        <tr>
            <td>域名称:</td>
            <td>
                <input class="easyui-textbox" id="nameZh" name="nameZh" data-options="readonly:'true'"/>
            </td>
        </tr>
        <tr>
            <td>是否必填:</td>
            <td>
            <select id="moFlag" name="moFlag" class="easyui-combobox"  style="width:170px;"
                    data-options="panelHeight:'auto'">
                <option value="true">是</option>
                <option value="false">否</option>
            </select>
            </td>
        </tr>
        <tr>
            <td>是否自动生成:</td>
            <td>
                <select id="fixFlag" name="fixFlag" class="easyui-combobox" style="width:170px;"
                        data-options="panelHeight:'auto'">
                    <option value="false">是</option>
                    <option value="true">否</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>默认值:</td>
            <td>
                <input class="easyui-textbox" id="defaultValue" name="defaultValue"/>
            </td>
        </tr>
    </table></div>

    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onCancel();">取消</a>
    </div>
</form>

<script>

    function getData(){
        var data = {};
        data['id'] = $('#id').val();
        data['moFlag'] = $('#moFlag').combobox('getValue');
        data['fixFlag'] = $('#fixFlag').combobox('getValue');
        data['defaultValue'] = $('#defaultValue').textbox('getValue');
        return data;
    }
    function setData(id){
        $.ajax({
                url: "${ctx}/msg/fieldset/field/get/" + id,
                cache: false,
                success: function (vo) {
                    //$('#fm').form('load',vo);
                    $('#id').val(vo.id);
                    $('#fieldId').textbox('setValue',vo.field.fieldId);
                    $('#nameZh').textbox('setValue',vo.field.nameZh);
                    $('#moFlag').combobox('setValue',''+vo.moFlag);
                    $('#fixFlag').combobox('setValue',''+vo.fixFlag);
                    $('#defaultValue').textbox('setValue',vo.defaultValue);
                }
            });
    }
    function onOk() {

        var form = $('#fm');
        var flag = form.form('validate');
        if (!flag) return;
        var formData;
        formData = getData();
        var id = $("#id").val();
        $.ajax({
            url: '${ctx}/msg/fieldset/field/edit?id='+id,
            type: 'post',
            data: formData,
            cache: false,
            success: function (data) {
                if (data.success) {
                    onCancel();
                } else {
                    showMsg('温馨提示', data.msg,true);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.responseText);
                onCancel();
            }
        });
    }
    function onCancel() {
        $('#dlg-fields').window('close');
    }
</script>
