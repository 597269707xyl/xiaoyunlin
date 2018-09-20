<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post">
    <div class="popup">
        <input type="hidden" id="id" name="id"/>
        <table class="single">
            <tr>
                <td>数据类型名称:</td>
                <td><input class="easyui-textbox" id="name" name="name" data-options="required:true"/></td>
            </tr>
            <tr>
                <td>数据类型代码:</td>
                <td><input class="easyui-textbox" id="code" name="code" data-options="required:true"/></td>
            </tr>
            <tr>
                <td>数据格式:</td>
                <td>
                    <input class="easyui-combobox" id="type" name="type"
                           data-options="valueField:'id',textField:'text',limitToList:'true',url:'${ctx}/generic/dict/DATATYPE_FORMAT',panelHeight:'auto',required:true"
                           onenter="onSearch"/>
                </td>
            </tr>
            <tr>
                <td>数据标准:</td>
                <td>
                    <input class="easyui-combobox" id="serial" name="serial"
                           data-options="valueField:'id',textField:'text',limitToList:'true',url:'${ctx}/generic/dict/SERIAL',panelHeight:'auto',required:true"
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
        formData.name = $("#datatypename").textbox('getValue');
        formData.code = $("#datatypecode").textbox('getValue');
        formData.type=$('#type').combobox('getValue');
        formData.serial=$('#serial').combobox('getValue');
        formData.descript = $("#descript").textbox('getValue');
        return formData;
    }
    function setData(param) {
        if (param.action == 'update') {
            $.ajax({
                url: "${ctx}/msg/datatype/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
                    $("#datatypename").textbox('setValue', vo.name);
                    $("#datatypecode").textbox('setValue', vo.code);
                }
            });
        }
    }
    function onOk() {
        var form = $('#fm');
        var flag = form.form('validate');
        if (!flag) return;
        var formData;
        formData = form.serialize();
       // formData = getData();
        $.ajax({
            url: '${ctx}/msg/datatype/add',
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
