<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" class="easyui-form" method="post">
    <input type="hidden" id="id" name="id"/>
    <div class="popup">
        <table>
            <tr>
                <td>适配器名称:</td>
                <td><input class="easyui-textbox" id="name1" name="name" required="true"/></td>
            </tr>
            <tr id="showText2">
                <td>远程url:</td>
                <td><input class="easyui-textbox" id="remoteUrl" name="remoteUrl"/></td>
            </tr>
            <tr id="showText3">
                <td>本地url:</td>
                <td><input class="easyui-textbox" id="localUrl" name="localUrl"/></td>
            </tr>
            <tr>
                <td>描述:</td>
                <td><input class="easyui-textbox" id="description" name="description" data-options="multiline:true"
                           /></td>
            </tr>
        </table>
    </div>
    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="closeWindow();">取消</a>
    </div>
</form>
<script>
    function onOk() {
        commonSubmitForm('${ctx}/sys/adapter/add/HTTP');
    }

    function getData() {
        var formData = {};
        formData.id = $('#id').val();
        formData.name = $('#name1').val();
        formData.remoteUrl = $('#remoteUrl').val();
        formData.localUrl = $('#localUrl').val();
        formData.description = $('#description').val();
        return formData;
    }
    function setData(param) {
        if (param.action == 'update') {
            $.ajax({
                url: "${ctx}/sys/adapter/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
                }
            });
        }
    }
</script>
