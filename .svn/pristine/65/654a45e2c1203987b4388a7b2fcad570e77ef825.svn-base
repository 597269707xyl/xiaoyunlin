<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/asset/js/jquery.form.min.js"></script>
<form id="fm" method="post" action="#" enctype="multipart/form-data">
    <div class="popup">
        <table class="single">
            <tr id="hidden1">
                <td>测试项:</td>
                <td>
                    <input class="easyui-combobox" id="itemId" name="itemId" style="width: 150px"
                           valueField="id" textField="name" limitToList="true" url="${ctx}/func/config/getAll" panelHeight="150"
                           required="true"/>
                </td>
            </tr>
            <tr id="hidden2">
                <td>案例编号:</td>
                <td><input class="easyui-textbox" type="text" name="variableZh" style="width: 150px"/></td>
            </tr>
            <tr>
                <td>缺陷标识:</td>
                <td><input class="easyui-textbox" type="text" name="variableZh" style="width: 150px"/></td>
            </tr>
            <tr  id="hidden3">
                <td>缺陷状态:</td>
                <td><select name="type" class="easyui-combobox" style="width:150px;"
                            data-options="panelHeight:'auto',limitToList:'true'">
                    <option></option>
                    <option value="1">已修复</option>
                    <option value="0">未修复</option>
                </select></td>
            </tr>
            <tr>
                <td>缺陷等级:</td>
                <td><select id="type" name="type" class="easyui-combobox" style="width:150px;"
                            data-options="panelHeight:'auto',limitToList:'true'">
                    <option></option>
                    <option value="G">高</option>
                    <option value="Z">中</option>
                    <option value="D">底</option>
                </select>
                </td>
            </tr>
            <tr  id="hidden4">
                <td>创建时间:</td>
                <td><input class="easyui-datetimebox" name="sendBegin" style="width: 150px"></td>
            </tr>
            <tr  id="hidden5">
                <td>修复时间:</td>
                <td><input class="easyui-datetimebox" name="sendBegin" style="width: 150px"></td>
            </tr>
            <tr>
                <td>缺陷描述:</td>
                <td><input class="easyui-textbox" id="adapterStatus" name="adapterStatus" data-options="multiline:true" id="descript" name="descript"
                           style="height:40px ;"/></td>
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
    function setData(param){
        document.getElementById("hidden1").style.display="none";
        document.getElementById("hidden2").style.display="none";
        document.getElementById("hidden3").style.display="none";
        document.getElementById("hidden4").style.display="none";
        document.getElementById("hidden5").style.display="none";
    }
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
