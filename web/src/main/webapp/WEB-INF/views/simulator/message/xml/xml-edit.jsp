<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/asset/js/jquery.form.min.js"></script>
<form id="fm-update" method="post" action="${ctx}/sim/message/uploads" enctype="multipart/form-data">
    <input type="hidden" id="id" name="id"/>
    <input type="hidden" id="type" name="type"/>
    <div class="popup">
        <table class="single">
            <tr>
                <td>仿真系统:</td>
                <td>
                    <input class="easyui-combobox" id="simname" name="simname" style="width: 200px"
                           data-options="valueField:'id',textField:'text',limitToList:'true',url:'${ctx}/sim/message/getAllsim?type=XML',panelHeight:'auto'"
                           onenter="onSearch" required="true"/>
                </td>
            </tr>
            <tr>
                <td>报文名称:</td>
                <td><input class="easyui-textbox" id="msgname" name="msgname" style="width: 200px" required="true"/>
                </td>
            </tr>
            <tr>
                <td>报文编码:</td>
                <td><input class="easyui-textbox" id="mesgType" name="mesgType" style="width: 100px" required="true"/>
                </td>
            </tr>
            <tr>
                <td><label>加签验签:</label></td>
                <td><select id="signFlag" name="signFlag" class="easyui-combobox" style="width:180px;"
                            data-options="panelHeight:'auto',limitToList:'true'">
                    <option value="true">是</option>
                    <option value="false">否</option>
                </select></td>
            </tr>
            <tr>
                <td><label>报文标准:</label></td>
                <td><input class="easyui-combobox" id="standard" name="standard" style="width: 200px"
                           valueField="id" textField="text" limitToList="true" url="${ctx}/generic/dict/STANDARD" panelHeight="auto"
                           required="true"/>
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
        formData.type = '${type}';
        formData.name = $("#msgname").textbox('getValue');
        formData.mesgType = $("#mesgType").textbox('getValue');
        formData.signFlag = $("#signFlag").combobox("getValue");
        formData.standard = $("#standard").combobox("getValue");
        return formData;
    }
    function setXmlData(param) {
            $.ajax({
                url: "${ctx}/sim/message/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm-update').form('load', vo);
                    $("#id").val(vo.id);
                    $("#simname").combobox('setValue', vo.simSystem.id);
                    $("#msgname").textbox('setValue', vo.name);
                    $("#signFlag").combobox('setValue', '' + vo.signFlag);
                    $("#standard").combobox('setValue', '' + vo.standard == null ? "" : vo.standard);
                    $("#mesgType").textbox('setValue', vo.mesgType);
                    $("#type").val(vo.type);
                }
            });
    }
    function onOk() {
        var form = $('#fm-update');
        var flag = form.form('validate');
        if (!flag) return;
        var formData = form.serialize();
        formData = getData();
        var simid = $("#simname").combobox('getValue');
        $.messager.progress();
        $.ajax({
            url: '${ctx}/sim/message/updateXmlMessage/' + simid,
            type: 'post',
            data: formData,
            cache: false,
            success: function (data) {
                $.messager.progress('close');
                if (data.success) {
                    closeWindow();
                } else {
                    showMsg('温馨提示', data.msg, true);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $.messager.progress('close');
                alert(XMLHttpRequest.responseText);
                closeWindow();
            }
        });
    }
    function onCancel() {
        closeWindow();
    }
</script>
