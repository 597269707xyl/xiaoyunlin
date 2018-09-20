<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post">
    <input type="hidden" id="id" name="id"/>
    <input type="hidden" id="parent" name="parent"/>
    <input type="hidden" id="level" name="level"/>
    <div class="popup">
        <table class="single">
            <tr>
                <td class="popup_label">名称:</td>
                <td><input class="easyui-textbox" id="name" name="name" data-options="required:true"/></td>

            </tr>
            <tr>
                <td class="popup_label">有无子功能:</td>
                <td>
                    <input type="radio" name="leaf" id="leaf1" value="false">有</input>
                    <input type="radio" name="leaf" id="leaf2" value="true">无</input>
                    <%--<select id="leaf" name="leaf">
                        <option value="false">有</option>
                        <option value="true">无</option>
                    </select>--%>
                </td>
            </tr>
            <tr>
                <td class="popup_label">功能类型:</td>
                <td>
                    <select id="type" name="type" class="easyui-combobox "
                            data-options="panelHeight:'auto',required:true,limitToList:true">
                        <option value="MenuPage">菜单项</option>
                        <option value="ActionBtn">功能按钮</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="popup_label">功能URL:</td>
                <td><input class="easyui-textbox" id="url" name="url" /></td>
            </tr>
            <tr>
                <td class="popup_label">功能函数:</td>
                <td><input class="easyui-textbox" id="action" name="action" /></td>
            </tr>
            <tr>
                <td class="popup_label">图标类:</td>
                <td><input class="easyui-textbox" id="iconCls" name="iconCls" /></td>
            </tr>
            <tr>
                <td class="popup_label">顺序号:</td>
                <td><input class="easyui-textbox" id="seqNo" name="seqNo" /></td>
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
    function setData(param) {
        if (param.action == 'update') {
            $.ajax({
                url: "${ctx}/sys/func/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
                    if (vo.isLeaf) {
                        $("#leaf2").attr("checked", "checked");
                    } else {
                        $("#leaf1").attr("checked", "checked");
                    }
                }
            });
        } else if (param.action == 'add') {
            if (param.funcType == 'sub') {
                $("#parent").val(param.parent);
            }
            $("#level").val(param.level);
        }
    }
    function getData() {
        var formData = {};
        formData.id = $("#id").val();
        formData.parent = $("#parent").val();
        formData.level = $("#level").val();
        formData.name = $("#name").textbox('getValue');
        formData.leaf = $('input[type="radio"][name="leaf"]:checked').val();
        formData.type = $("#type").combobox('getValue');
        formData.url = $("#url").textbox('getValue');
        formData.action = $("#action").textbox('getValue');
        formData.iconCls = $("#iconCls").textbox('getValue');
        formData.seqNo = $("#seqNo").textbox('getValue');
        return formData;
    }
    function onOk() {
        var form = $('#fm');
        var flag = form.form('validate');
        if (!flag) return;
        var formData;
        formData = getData();
        $.ajax({
            url: '${ctx}/sys/func/add',
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
