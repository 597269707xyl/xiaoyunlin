<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/asset/js/jquery.form.min.js"></script>
<form id="fm" method="post" action="#" enctype="multipart/form-data">
    <div class="popup">
        <table class="single">
            <tr>
                <td>测试项:</td>
                <td>
                    <input class="easyui-combobox" id="itemId" name="itemId" style="width: 200px"
                           valueField="id" textField="name" limitToList="true" url="${ctx}/func/config/getAll" panelHeight="150"
                           required="true"/>
                </td>
            </tr>
            <tr>
                <td>配置文件:</td>
                <td><input class="f1 easyui-filebox" id="file" name="file" buttonText="选择文件" style="width: 200px" required="true"/></td>
            </tr>
        </table>
    </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onUploadOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onUploadCancel();">取消</a>
    </div>
</form>

<script>
    function onUploadOk() {
        var form = $('#fm');
        var flag = form.form('validate');
        if (!flag) return;
        var formData = form.serialize();
        $.messager.progress();
        $("#fm").ajaxSubmit({async: false,url:'${ctx}/func/config/upload', success: function (result) {
            $.messager.progress('close');
            if (result.success) {
                $("#dg").datagrid('reload');
                closeWindow();
                showMsg('温馨提示',"上传成功",true);
            }else{
                showMsg('温馨提示',result.msg,true);
                closeWindow();
            }
        }});
    }
    function onUploadCancel() {
        closeWindow();
    }
</script>
