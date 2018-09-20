<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm-comp-edit" method="post">
    <input type="hidden" id="id" name="id"/>
    <table>
        <tr>
            <td>序号:</td>
            <td><input id="seq_no" name="seq_no" value="0" style="width: 300px" type="text" class="easyui-numberbox" data-options="min:0" required/></td>
        </tr>
        <tr>
            <td>域id:</td>
            <td><input class="easyui-textbox" id="fieldId" name="fieldId" data-options="readonly:'true'" style="width: 300px" editable="false"/></td>
        </tr>
        <tr>
            <td>域名称:</td>
            <td>
                <input class="easyui-textbox" id="nameZh" name="nameZh" data-options="readonly:'true'" style="width: 300px" editable="false"/>
            </td>
        </tr>
        <tr>
            <td>是否必填:</td>
            <td>
            <select id="mflag" name="mflag" class="easyui-combobox" style="width:300px;"
                    data-options="panelHeight:'auto',limitToList:'true'">
                <option value="true">是</option>
                <option value="false">否</option>
            </select>
            </td>
        </tr>
        <%--<tr>--%>
            <%--<td>是否自动生成:</td>--%>
            <%--<td>--%>
                <%--<select id="fflag" name="fflag" class="easyui-combobox" style="width:300px;"--%>
                        <%--data-options="panelHeight:'auto',limitToList:'true'">--%>
                    <%--<option value="false">是</option>--%>
                    <%--<option value="true">否</option>--%>
                <%--</select>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <tr>
            <td>是否加签域:</td>
            <td>
                <select id="sflag" name="sflag" class="easyui-combobox" style="width:300px;"
                        data-options="panelHeight:'auto',limitToList:'true'">
                    <option value="true">是</option>
                    <option value="false">否</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>默认值:</td>
            <td>
                <input class="easyui-textbox" id="dvalue" name="dvalue" style="width: 300px"/>
            </td>
        </tr>
        <tr>
            <td>取值方式:</td>
            <td>
                <input class="easyui-combobox" id="type" name="valueType" style="width: 300px"
                       data-options="valueField:'id',
                       textField:'text',
                       url:'${ctx}/generic/dict/FIELDSETVALUEMETHOD',
                       panelHeight:'auto',
                       limitToList:'true',
                       onSelect: function(rec){
                           if(rec.id == 'methodvalue'){
                               setRow('row2','show');
                               setRow('row1','hide');
                           }else{
                               setRow('row1','show');
                               setRow('row2','hide');
                           }
                       },
                       onChange:function(newValue,oldValue){
                           $('#invokemethod').combobox('setValue','');
                           $('#value').textbox('setValue','');
                       }
                       "
                       onenter="onSearch"/>
            </td>
        </tr>
        <tr id="row1" style="display:none">
            <td>取值:</td>
            <td>
                <input class="easyui-textbox" id="value" name="value" style="width: 300px"/>
            </td>
        </tr>
        <tr id="row2" style="display:none">
            <td>方法列表:</td>
            <td>
                <input class="easyui-combobox" id="invokemethod" name="invokemethod" style="width: 300px"
                       data-options="valueField:'id',
                       textField:'text',
                       url:'${ctx}/generic/dict/REP_INVOKED_METHOD',
                       panelHeight:200,
                       limitToList:'true',
                       onSelect: function(rec){
                           $('#value').textbox('setValue',rec.id);
                       }
                       "
                       onenter="onSearch"/>
            </td>
        </tr>
    </table>

    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onCancel();">取消</a>
    </div>
</form>

<script>

    function getData(){
        return $("#fm-comp-edit").serialize();
    }
    function setData(id){
        $.ajax({
                url: "${ctx}/sim/message/field/get/" + id,
                cache: false,
                success: function (vo) {
                    $('#fm-comp-edit').form('load',vo);
                    $('#id').val(vo.id);
                    $('#fieldId').textbox('setValue',vo.msgField.fieldId);
                    $('#nameZh').textbox('setValue',vo.msgField.nameZh);
                    if (vo.mflag == null){
                        $('#mflag').combobox('setValue','false');
                    }else {
                        $('#mflag').combobox('setValue',''+vo.mflag);
                    }
                    if (vo.fflag == null){
                        $('#fflag').combobox('setValue','false');
                    }else {
                        $('#fflag').combobox('setValue',''+vo.fflag);
                    }
                    if (vo.sflag == null){
                        $('#sflag').combobox('setValue','false');
                    }else {
                        $('#sflag').combobox('setValue',''+vo.sflag);
                    }

                    $('#valueType').combobox('setValue',vo.valueType);
                    $('#dvalue').textbox('setValue',vo.dvalue);

                    if (vo.valueType == 'methodvalue'){
                        setRow('row2','show');
                        setRow('row1','hide');
                        $('#invokemethod').combobox('setValue',vo.value);
                    }else {
                        setRow('row1','show');
                        setRow('row2','hide');
                        $('#value').textbox('setValue',vo.value);
                    }
                }
            });
    }

    function onOk() {
        var form = $('#fm-comp-edit');
        var flag = form.form('validate');
        if (!flag) return;
        var formData;
        formData = getData();
        var id = $("#id").val();
        $.messager.progress();
        $.ajax({
            url: '${ctx}/sim/message/field/edit?id='+id,
            type: 'post',
            data: formData,
            cache: false,
            success: function (data) {
                $.messager.progress('close');
                if (data.success) {
                    onCancel();
                } else {
                    showMsg('温馨提示', data.msg,true);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $.messager.progress('close');
                alert(XMLHttpRequest.responseText);
                onCancel();
            }
        });
    }

    function onCancel() {
        $('#dlg-fields').window('close');
    }
    function setRow(rowID,value) {
        var row = document.getElementById(rowID);
        if (row != null) {
            if (value=='show'){
                row.style.display = (document.all ? "block" : "table-row");
            }else {
                row.style.display = "none";
            }
        }
    }
</script>
