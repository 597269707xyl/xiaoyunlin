<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" class="easyui-form" method="post">
    <input type="hidden" id="srcId" name="srcId"/>
    <div class="popup">
        <table>
            <tr>
                <td>实例名称:</td>
                <td><input class="easyui-textbox" id="name" name="name" required="true" style="width: 200px"/>
                </td>
                <td>适配器:</td>
                <td><input class="easyui-combobox" id="institution" name="institution" style="width: 200px"/></td>
            </tr>
            <tr>
                <td>描述:</td>
                <td><input class="easyui-textbox" data-options="multiline:true" id="descript" name="descript"
                           style="height:40px ;width: 200px"></td>
            </tr>
        </table>
    </div>
    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onCancel();">取消</a>
    </div>
</form>
<script>
    function setData(param) {
        $('#srcId').val(param.id);
        $.ajax({
            url: "${ctx}/sim/instance/get/" + param.id,
            cache: false,
            success: function (vo) {
                $('#fm').form('load', vo);
            }
        });
        $("#testEnvironment").combobox({
            url:'${ctx}/generic/dict/TEST_ENVIRONMENT_LIST',
            valueField:'id',
            textField:'text',
            panelHeight:'auto',
            limitToList:true
        });
    }


    function getData() {
        return $("#fm").serialize();
    }
    function onOk() {
        var form = $('#fm');
        var flag = form.form('validate');
        if (!flag) return;
        var formData = getData();
        $.messager.progress({
            title : '实例复制',
            msg : '正在复制仿真实例，请不要关闭页面。'
        });
        $.ajax({
            url: '${ctx}/sim/instance/copy',
            type: 'post',
            data: formData,
            cache: false,
            success: function (data) {
                $.messager.progress('close');
                if (data.success) {
                    $("#dg").datagrid('reload');
                    closeWindow();
                } else {
                    showMsg('温馨提示', data.msg,true);
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
    function showRevcQueue(){
        $('#window-recv-queue').window({
            title: "接收队列列表",
            width: 530,
            height: 430,
            href: '${ctx}/sim/instance/recvQueue',
            modal: true,
            minimizable: false,
            maximizable: false,
            shadow: false,
            cache: false,
            closed: false,
            collapsible: false,
            resizable: true,
            loadingMessage: '正在加载数据，请稍等......',
            onLoad: function () {
                setData(0)
            },
            onClose: function () {
            }
        });
    }
</script>
