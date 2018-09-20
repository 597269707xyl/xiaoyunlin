<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post">
    <div class="popup">
        <input type="hidden" id="id" name="id"/>
        <table class="single">
            <tr>
                <td>分类:</td>
                <td><input class="easyui-textbox" id="category" name="category"
                           data-options="readonly:'true'"
                /></td>

            </tr>
            <tr>
                <td>键:</td>
                <td>
                    <input class="easyui-textbox" id="key" name="key" data-options="required:true"/>
                </td>
            </tr>
            <tr>
                <td>值:</td>
                <td>
                    <input class="easyui-textbox" id="value" name="value" data-options="required:true"/>
                </td>
            </tr>
            <tr>
                <td>顺序号:</td>
                <td><input class="easyui-textbox" id="seqNo" name="seqNo"/></td>
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
        if (param.action == "update") {
            $.ajax({
                url: "${ctx}/sys/code/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
                }
            })
        }
        if (param.action == "add") {
            var codeType = param.codeType;
            if (codeType == 'top') {
                $("#category").textbox('setValue', 'ROOT');
            } else if (codeType == 'sub') {
                $("#category").textbox('setValue', param.category);
            }
        }

    }
    function getData() {
        return $("#fm").serialize();
    }
    function onOk() {
        var form = $('#fm');
        var flag = form.form('validate');
        if (!flag) return;
        var formData;
        formData = getData();
        $.ajax({
            url: '${ctx}/sys/code/add',
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
