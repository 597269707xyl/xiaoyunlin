<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="form-update" method="post">
    <div class="popup">
        <input type="hidden" id="id" name="id"/>
        <table>
            <tr>
                <td>数据类型:</td>
                <td> <input class="easyui-combobox" id="dataSetType" name="dataSetType"
                            data-options="valueField:'id',textField:'text',limitToList:'true',url:'${ctx}/generic/dict/DATATYPE_TYPE',panelHeight:'auto',required:true"
                            onenter="onSearch"/></td>
            </tr>
            <tr>
                <td>数据生成规则:</td>
                <td><input class="easyui-textbox" id="generatRule" name="generatRule" data-options="multiline:true"
                           style="height: 60px;"/></td>
            </tr>
            <tr>
                <td>说明:</td>
                <td><input class="easyui-textbox" id="descript" name="descript" data-options="multiline:true"
                           style="height: 60px;"/></td>
            </tr>
        </table>
    </div>
    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onEditOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onEditCancel();">取消</a>
    </div>
</form>

<script>
    function setData(id) {
            $.ajax({
                url: "${ctx}/msg/datatype/getEnum/" + id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
                    $('#id').val(vo.id);
                    $("#dataSetType").combobox('setValue', vo.dataSetType);
                    $("#generatRule").textbox('setValue', vo.generatRule);
                    $("#descript").textbox('setValue', vo.descript)
                }
            });
    }

    function getData() {
        var formData = {};
        formData.id = $("#id").val();
        formData.dataSetType=$('#dataSetType').combobox('getValue');
        formData.generatRule = $("#generatRule").textbox('getValue');
        formData.descript = $("#descript").textbox('getValue');
        return formData;
    }
    function onEditOk(){
        var form = $('#form-update');
        var flag = form.form('validate');
        if (!flag) return;
        var formData = form.serialize();
        formData = getData();
       // var dataTypeId="${dataTypeId}"
        var id=$("#id").val();
        $.ajax({
            url: '${ctx}/msg/datatype/updateDataTypeEnum/'+id,
            type: 'post',
            data: formData,
            cache: false,
            success: function (data) {
                if (data.success) {
                    onEditCancel();
                } else {
                    showMsg('温馨提示', data.msg, true);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.responseText);
                onEditCancel();
            }
        });
    }

    function onEditCancel(){
        $('#dlg-fields').window('close');
    }
</script>
