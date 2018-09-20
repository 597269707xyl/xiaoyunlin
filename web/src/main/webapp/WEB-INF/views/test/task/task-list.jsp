<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>测试任务权限分配</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <link href="${ctx}/asset/css/style.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        .menu_lr a {
            height: 25px;
            width: 85px;
            border: solid 1px #c9c7c2;
            display: block;
            float: left;
            margin: 0 2px;
            padding: 0 2px;
            line-height: 24px;
            text-decoration: none;
            color: #818181;
            background: #f0f1f4;
            border-radius: 4px;
            font-size: 12px
        }

        .menu_lr a:hover {
            background: #e8f1ff
        }

        .menu_lr img {
            vertical-align: middle;
            margin: 0 1px;
            padding: 0
        }
    </style>
</head>
<body style="width: 100%;height: 100%">
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center',title:'功能测试项',split:true" style="width:100%">
        <input id="projectId" name="projectId" type="hidden">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin:4px 0">
            <tbody>
            <tr class="menu_lr">
                <td><a href="#" style="background-color: #E6EFFF;" onclick="assignMember();"><img src="${ctx}/asset/images/btn_add.png" width="16" height="16" alt=""/>指定人员</a>
                <a href="#" style="background-color: #E6EFFF;" onclick="reporting();"><img src="${ctx}/asset/images/btn_add.png" width="16" height="16" alt=""/>生成报告</a></td>
            </tr>
            </tbody>
        </table>
        <div>
            <ul id="projectTree" class="easyui-tree">
            </ul>

        </div>
    </div>
</div>

<script>
    //功能测试项目树形列表
    $(function () {
        $('#projectTree').tree({
//            checkbox:function(node){
//                var type = node.type;
////                if (type=='project' || node.state == 'closed' || node.children != null){
//                if (type=='project'){
//                    return false;
//                }
//                return true;
//            },
            animate: true,
            url: '${ctx}/test/task/tree?mark=send&caseType=t&id=-1',
//            checkbox: true,
//            cascadeCheck: true,
            onSelect: function (node) {
                $('#projectTree').tree('check',node.target);
                var type = node.type;
                if (type == 'project') {
                    $("#projectId").val(node.id);
                }
                $(this).tree('options').url = '${ctx}/test/task/tree?mark=send&caseType=t&type='+type;
                if (node.state == 'closed') {
                    $(this).tree('expand', node.target);
                } else {
                    $(this).tree('collapse', node.target);
                }
                var p = {id: node.id, type: node.type};
                $("#usecaseList").datagrid('load', p);
            },
            onExpand:function(node){
            },
            onBeforeExpand:function(node){
                var type = node.type;
                $(this).tree('options').url = '${ctx}/test/task/tree?mark=send&caseType=t&type='+type;
            },
            onLoadError: function (Error) {
                $.messager.alert('提示', '查询语句出错', 'error');
                $("#projectTree").children().remove();
            },
            onLoadSuccess: function (success) {
            }
        });
    });
    //刷新功能测试项列表
    function reloadTree(node,type){
        var nodeType = node.type;
        var tree = $('#projectTree');
        if (type=='add'){
            if (node.state == 'open' && nodeType != 'project') {
                var node = tree.tree('getParent', node.target);
                tree.tree('reload', node.target);
            } else {
                tree.tree('reload', node.target);
            }
        }else if(type == 'update'){
            var parent = tree.tree('getParent', node.target);
            if (parent) {
                tree.tree('reload', parent.target);
            }
        }
    }
    function reporting() {
//        var rows = $("#projectTree").datagrid('getSelections');
//        if (rows.length < 1) {
//            showMsg("提示", "请选择一条记录", true);
//            return;
//        }
        location.href="${ctx}/test/task/report";
    }

    function assignMember() {
        var node = $('#projectTree').tree('getSelected');
        if(node && node.type=='item_one'){
            commonShowWindow({action:'update',id:node.id}, '分配人员', '${ctx}/test/task/assign', 300, 120, true, false, false);
        } else {
            showMsg("提示", "请选择第一级测试任务分配人员!", true);
        }
    }
</script>
</body>
</html>
