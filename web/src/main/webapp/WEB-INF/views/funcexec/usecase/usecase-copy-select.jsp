<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>自动化执行</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
</head>
<body>
<div>
    <table id="projectSelectTree"></table>
    <div style="text-align: center">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
           onclick="onCopyOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onCopyCancel();">取消</a>
    </div>
</div>

<script>
    var UsecaseIds;
    function setItemCopyData(usecaseIds) {
        UsecaseIds = usecaseIds;
        $('#projectSelectTree').tree({
            animate: true,
            url: '${ctx}/func/exec/tree?mark=send&caseType=t&id=-1',
            onSelect: function (node) {
                var type = node.type;
                $(this).tree('options').url = '${ctx}/func/exec/tree?mark=send&caseType=t&type='+type;
                if (node.state == 'closed') {
                    $(this).tree('expand', node.target);
                } else {
                    $(this).tree('collapse', node.target);
                }
            },
            onExpand: function (node) {
            },
            onBeforeExpand: function (node) {
                var type = node.type;
                $(this).tree('options').url = '${ctx}/func/exec/tree?mark=send&caseType=t&type='+type;
            },
            onLoadError: function (Error) {
                $.messager.alert('提示', '查询语句出错', 'error');
                $("#projectSelectTree").children().remove();
            },
            onLoadSuccess: function (success) {
            }
        });
    }
    function onCopyOk() {
        var node = $('#projectSelectTree').tree('getSelected');
        if(node){
            var id = node.id;
            if(node.type == 'project'){
                showMsg("提示", "只能复制到测试项!", true);
                return;
            }
            <%--$('#instance-window').window({--%>
                <%--title: '选择仿真实例',--%>
                <%--width: 250,--%>
                <%--height: 100,--%>
                <%--href: '${ctx}/func/exec/selectInstancePage',--%>
                <%--modal: true,--%>
                <%--minimizable: false,--%>
                <%--maximizable: false,--%>
                <%--shadow: false,--%>
                <%--cache: false,--%>
                <%--closed: false,--%>
                <%--collapsible: false,--%>
                <%--resizable: true,--%>
                <%--loadingMessage: '正在加载数据，请稍等......',--%>
                <%--onLoad: function () {--%>
                    <%--setInstanceData({usecaseIds: UsecaseIds, id: id, type: 'item'}, 'copyUsecase');--%>
                <%--},--%>
                <%--onClose: function () {--%>
                <%--}--%>
            <%--});--%>
            <%--onCopyCancel();--%>
            $.ajax({
                url: '${ctx}/func/exec/usecase/copy',
                data: {usecaseIds: UsecaseIds, id: id, type: 'item'},
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
                        onCopyCancel();
                    } else {
                        showMsg("提示", data.msg, true);
                    }
                }
            });
        } else {
            showMsg("提示", "请选择复制目的!", true);
        }
    }

    function onCopyCancel() {
        $("#copy-window").window('close');
        $("#copy-window1").window('close');
    }
</script>
</body>
</html>
