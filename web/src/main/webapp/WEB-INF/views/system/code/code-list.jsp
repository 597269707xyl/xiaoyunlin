<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <link href="${ctx}/asset/css/style.css" rel="stylesheet" type="text/css"/>
    <script>
        $(function () {
            $('#tg').treegrid({
                height: $(window).height() -48,
                idField:'id',
                treeField:'value',
                fitColumns: true,
                url: '${ctx}/sys/code/treeList',
                columns: [[
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'value', title: '值', width: 100},
                    {field: 'category', title: '分类', width: 100},
                    {field: 'key', title: '键', width: 100},
                    {field: 'seqNo', title: '序号', width: 100}
                ]]
            });
        });

        function addOrUpdate(type, codeType, title) {
            var param = {action: type, codeType: codeType};
            var treeGrid = $("#tg");
            if (type == 'update') {
                var node = treeGrid.treegrid('getSelected');
                if (node) {
                    param.id = node.id;
                } else {
                    showMsg("提示","请选择要修改的字典!",true);
                    return;
                }
            }
            if (type == 'add') {
                if (codeType == 'sub') {
                    var node = treeGrid.treegrid('getSelected');
                    param.category = node.key;
                }
            }
            $('#myWindow').window({
                title: title,
                width: 360,
                height: 210,
                href: '${ctx}/sys/code/add',
                modal: true,
                minimizable: false,
                maximizable: false,
                shadow: false,
                cache: false,
                closed: false,
                collapsible: false,
                resizable: true,
                loadingMessage: '正在加载数据，请稍等......',
                onLoad:function(){
                    setData(param)
                },
                onClose:function(){
                    if (codeType == 'top') {
                        treeGrid.treegrid('reload');
                    } else if (codeType == 'sub') {
                        var node = treeGrid.treegrid('getSelected');
                        refreshNode(node);

                    }
                    if (type == 'update') {
                        var node = treeGrid.treegrid('getSelected');
                        refreshNode(node, "parent");
                    }
                }
            });
        }
        function refreshNode(node, type) {
            if (node) {
                var treeGrid = $("#tg");
                if (type && type == "parent") {
                    var parentNode = treeGrid.treegrid('getParent',node.id);
                    if (parentNode) {
                        treeGrid.treegrid('reload',parentNode.id);
                    }else {
                        treeGrid.treegrid('reload');
                    }
                } else {
                    if (node.state == 'open'){
                        treeGrid.treegrid('update',{
                            id: node.id,
                            row: {
                                state: 'closed'
                            }
                        });
                    }

                    treeGrid.treegrid('reload',node.id);
                }
            }
        }

        function del(){
            var treeGrid = $("#tg");
            var node = treeGrid.treegrid('getSelected');
            if (node) {
                if (showConfirm("提示","确定要删除吗？",function(){
                            $.post("${ctx}/sys/code/del",{id:node.id},function(data){
                                if(data.success){
                                    refreshNode(node,"parent");
                                }else{
                                    showMsg("提示","操作出错",true);
                                }
                            });

                        }));
            }else{
                showMsg("提示","请选择要删除的功能",true);
            }
        }

    </script>
</head>
<body>
<div class="toolBar">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a href="#" class="easyui-splitbutton" data-options="menu:'#mm',iconCls:'icon-add'">增加</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="del();">删除</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="addOrUpdate('update','','修改');">修改</a>
            </td>
        </tr>
    </table>
    <div id="mm" style="width:100px;">
        <div data-options="iconCls:'icon-add'" onclick="addOrUpdate('add','top','添加一级字典');">一级字典</div>
        <div data-options="iconCls:'icon-add'" onclick="addOrUpdate('add','sub','添加子字典');">子字典</div>
    </div>
</div>
<table id="tg"></table>
</body>
</html>
