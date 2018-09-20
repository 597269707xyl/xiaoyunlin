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
        <input class="easyui-combobox" id="instancecopy" style="width: 200px"
               valueField="id" textField="name" limitToList="true" url="" panelHeight="200">
    </div>
    <div style="text-align:center">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
           onclick="onInstanceOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onInstanceCancel();">取消</a>
    </div>
</div>
<script>
    var params;
    var type;
    function setInstanceData(data, msg) {
        params = data;
        type = msg;
        $('#instancecopy').combobox({url:'${ctx}/func/exec/selectInstance?id='+ data.id+"&type="+data.type})
    }
    function onInstanceOk() {
        var node = $('#instancecopy').combobox('getValue');
        if(node != ''){
            params['instanceId'] = node;
            if(type=="copyUsecase"){
                $.ajax({
                    url: '${ctx}/func/exec/usecase/copy',
                    data: params,
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
                            showMsg("提示", data.msg, true);
                            onInstanceCancel();
                        } else {
                            showMsg("提示", data.msg, true);
                        }
                    }
                });
            } else {
                $.ajax({
                    url: '${ctx}/func/exec/item/copy',
                    data: params,
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
                            $('#projectTree').tree({url: '${ctx}/func/exec/tree?mark=send&id=-1'});
                            showMsg("提示", "复制成功!", true);
                            onInstanceCancel();
                        } else {
                            showMsg("提示", data.msg, true);
                        }
                    }
                });
            }
        } else {
            showMsg("提示", "请选择复制目的仿真实例!", true);
        }
    }

    function onInstanceCancel() {
        $("#instance-window").window('close');
    }
</script>
</body>
</html>
