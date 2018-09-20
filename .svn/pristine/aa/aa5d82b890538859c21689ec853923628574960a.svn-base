<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>配置文件复制</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
</head>
<body>
<div>
    <div style="text-align: center">
        <input class="easyui-combobox" id="configcopy" style="width: 200px"
               valueField="id" textField="name" limitToList="true" url="${ctx}/func/config/getAll" panelHeight="200">
    </div>
    <div style="text-align:center">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
           onclick="onCopyOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onCopyCancel();">取消</a>
    </div>
</div>
<script>
    var configIds;
    function setConfigCopyData(ids) {
        configIds = ids;
    }
    function onCopyOk() {
        var node = $('#configcopy').combobox('getValue');
        if(node != ''){
            $.ajax({
                url: '${ctx}/func/config/copy',
                data: {configIds: configIds, id: node},
                type: 'POST',
                cache: false,
                async: true,
                beforeSend: function(){
                    $.messager.progress({
                        title : '复制',
                        msg : '复制数据生成中，请不要关闭页面。'
                    });

                },
                success: function(data) {
                    $.messager.progress('close');
                    if (data.success) {
                        $("#dg").datagrid('reload');
                        if(data.msg=''){
                            showMsg("提示", "复制成功!", true);
                        } else {
                            showMsg("提示", data.msg, true);
                        }
                        onCopyCancel();
                    } else {
                        showMsg("提示", "复制失败!", true);
                    }
                }
            });
        } else {
            showMsg("提示", "请选择复制目的!", true);
        }
    }

    function onCopyCancel() {
        $("#copy-window").window('close');
    }
</script>
</body>
</html>
