<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2018/7/16
  Time: 14:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post" action="#" enctype="multipart/form-data">
    <div class="popup">
        <div>
            <table>
                <td class="popup_label">文件:</td>
                <td><input class="easyui-filebox" name="file1" id="fileImport"
                           data-options="buttonText:'选择',prompt:'选择文件...'"
                           style="width:100%"></td>
                <td><a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
                       onclick="uploadFile();">上传</a></td>
            </table>
        </div>
        <table id="dg-sim-list"></table>
    </div>
</form>
<script>
    function setData(param) {
        $("#dg-sim-list").datagrid({
            fit: true,
            fitColumns: true,
            url: '${ctx}/proj/proj/getFileList?projectId=' + param.id,
            height: 350,
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {field: 'fieldId', title: 'fieldId', hidden: true},
                {field: 'fileName', title: '文件名称', width: 150},
                {field: 'createTime', title: '生成时间', width: 150},
                {field: 'systemName', title: '操作', width: 150}
            ]],
            /*            toolbar: [{
             text: '添加',
             iconCls: 'icon-add',
             handler: function () {
             $('#window-sim-list').window({
             title: '选择文件',
             width: 480,
             height: 420,
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
             setData(param)
             },
             onClose: function () {
             $("#dg-sim-list").datagrid('reload');
             $("#dg").datagrid('reload');
             }
             });
             }
             }, '-', {
             text: '删除',
             iconCls: 'icon-remove',
             handler: function () {
             var rows = $("#dg-sim-list").datagrid('getSelections');
             var ids = new Array();
             if (rows && rows.length > 0) {
             for (var i = 0; i < rows.length; i++) {
             ids.push(rows[i].id);
             }
             } else {
             showMsg("提示", "请选择要删除的记录", true);
             return;
             }
             var request = param.id;
             $.post("${ctx}/proj/proj/deleteAllocatedSimulators", {
             projectId: param.id,
             ids: ids
             }, function (data) {
             if (data.success) {
             $("#dg-sim-list").datagrid('reload');
             $("#dg").datagrid('reload');
             } else {
             showMsg("提示", data.msg, true);
             }
             });
             }
             }]*/
        });

    }
    function onSetCancel() {
        closeWindow();
    }

    function uploadFile() {
        var form = $('#fm');
        var formData = form.serialize();
        $("#fm").ajaxSubmit({
            async: false, url: '${ctx}/proj/proj/uploadFile', success: function (result) {
                if (result.success) {
                    $("#dg").datagrid('reload');
                    closeWindow();
                    showMsg('温馨提示', "上传成功", true);
                } else {
                    showMsg('温馨提示', result.msg, true);
                    closeWindow();
                }
            }
        });
    }

</script>

