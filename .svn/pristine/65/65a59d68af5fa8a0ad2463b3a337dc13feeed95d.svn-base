<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post">
    <input type="hidden" id="id" name="id"/>
    <div class="popup">
    <table class="single">
        <tr>
            <td class="popup_label">格式名称:</td>
            <td><input class="easyui-textbox" id="name" name="name" data-options="required:true"/></td>
        </tr>
        <tr>
            <td class="popup_label">报文类型:</td>
            <td>
                <input class="easyui-combobox" id="type" name="type"
                       data-options="valueField:'id',textField:'text',limitToList:true,url:'${ctx}/generic/dict/MSGTYPE',panelHeight:'auto',required:true"
                       onenter="onSearch"/>
            </td>
        </tr>
        <tr>
            <td class="popup_label">描述:</td>
            <td><input class="easyui-textbox" id="descript" name="descript" style="height:60px;"/></td>
        </tr>
    </table></div>

    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onCancel();">取消</a>
    </div>
</form>

<script>

    function getData(){
        return $("#fm").serialize();
    }
    function setData(param){
        if (param.action == 'update'){
            $.ajax({
                url: "${ctx}/msg/format/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load',vo);
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
            url: '${ctx}/msg/format/add',
            type: 'post',
            data: formData,
            cache: false,
            success: function (data) {
                if (data.success) {
                    closeWindow();
                } else {
                    showMsg('温馨提示', data.msg,true);
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
