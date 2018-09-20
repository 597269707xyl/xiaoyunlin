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
    var ItemIds;
    var UsecaseIds;
    var Type;
    function setCopyData(mark,type,ids) {
        ItemIds = ids;
        UsecaseIds = ids;
        Type = type
        $('#projectSelectTree').tree({
            animate: true,
            url: '${ctx}/func/exec/tree?mark='+mark+'&caseType=k&id=-1',
            onSelect: function (node) {
                var type = node.type;
                $(this).tree('options').url = '${ctx}/func/exec/tree?mark='+mark+'&caseType=k&type='+type;
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
                $(this).tree('options').url = '${ctx}/func/exec/tree?mark='+mark+'&caseType=k&type='+type;
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
            var type = node.type;
            if(Type=='item'){
                if(node.children == null){
                    showMsg("提示", "请选择正确的测试项!", true);
                    return;
                }
                $.ajax({
                    url: '${ctx}/casebase/item/copy',
                    data: {itemIds: ItemIds, id: id, type: type},
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
                            <%--$('#projectTree').tree({url: '${ctx}/func/exec/tree?mark='+mark+'&caseType=k&id=-1'});--%>
                            showMsg("提示", "复制成功!", true);
                            onCopyCancel();
                        } else {
                            showMsg("提示", data.msg, true);
                        }
                    }
                });
            } else {
                if(node.type == 'project'){
                    showMsg("提示", "只能复制到测试项!", true);
                    return;
                }
                $.ajax({
                    url: '${ctx}/casebase/usecase/copy',
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
            }
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
