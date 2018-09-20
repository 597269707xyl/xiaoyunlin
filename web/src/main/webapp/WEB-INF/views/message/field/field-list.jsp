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
            $('#dg').datagrid({
                height: $(window).height() - 80,
                fitColumns: true,
                url: '${ctx}/generic/commonQuery/msgField',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'fieldId', title: '域id', width: 100},
                    {field: 'nameEn', title: '域英文名称', width: 100},
                    {field: 'nameZh', title: '域名称', width: 100,editor:'text'},
                    {field: 'startPos', title: '起始位置', width: 100},
                    {field: 'length', title: '长度', width: 100},
                    {field: 'lengthLength', title: '长度的长度', width: 100},
                    {field: 'defaultValue', title: '默认值', width: 100},
                    {
                        field: 'dataType', title: '数据类型', width: 100,
                        formatter: function (value, row, index) {
                            if (row.dataType) {
                                return row.dataType.code;
                            } else {
                                return '';
                            }
                        }
                    },
                    {field: 'fieldTypeDis', title: '域类型', width: 100},
                    {field: 'msgTypeDis', title: '报文类型', width: 100}
                ]]
            }).datagrid('enableCellEditing');
        });
        $.extend($.fn.datagrid.methods, {
            editCell: function(jq,param){
                return jq.each(function(){
                    var opts = $(this).datagrid('options');
                    var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
                    for(var i=0; i<fields.length; i++){
                        var col = $(this).datagrid('getColumnOption', fields[i]);
                        col.editor1 = col.editor;
                        if (fields[i] != param.field){
                            col.editor = null;
                        }
                    }
                    $(this).datagrid('beginEdit', param.index);
                    var ed = $(this).datagrid('getEditor', param);
                    if (ed){
                        if ($(ed.target).hasClass('textbox-f')){
                            $(ed.target).textbox('textbox').focus();
                        } else {
                            $(ed.target).focus();
                        }
                    }
                    for(var i=0; i<fields.length; i++){
                        var col = $(this).datagrid('getColumnOption', fields[i]);
                        col.editor = col.editor1;
                    }
                });
            },
            enableCellEditing: function(jq){
                return jq.each(function(){
                    var dg = $(this);
                    var opts = dg.datagrid('options');
                    opts.oldOnClickCell = opts.onClickCell;
                    opts.onClickCell = function(index, field){
                        if (opts.editIndex != undefined){
                            if (dg.datagrid('validateRow', opts.editIndex)){
                                dg.datagrid('endEdit', opts.editIndex);
                                var upda=$('#dg').datagrid("getChanges","updated");
                                if(upda.length>0){
                                    var str=JSON.stringify(upda);
                                    $.ajax({
                                        type: "POST",
                                        url: "${ctx}/msg/field/update",
                                        data:str,
                                        cache: false,
                                        async: false,
                                        dataType: "json",
                                        contentType:"application/json",
                                        success: function (data) {

                                        }
                                    });
                                }
                                opts.editIndex = undefined;

                            } else {
                                return;
                            }

                        }
                        dg.datagrid('selectRow', index).datagrid('editCell', {
                            index: index,
                            field: field
                        });
                        opts.editIndex = index;
                        opts.oldOnClickCell.call(this, index, field);
                    }
                });
            }
        });
        function onSearch() {
            var fieldId = $("#fId").textbox("getValue");
            var nameEn = $("#nEn").textbox("getValue");
            var fieldType = $("#fType").combobox("getValue");
            var messageType = $("#mType").combobox("getValue");
            var p = {};
            if (fieldId != null && fieldId != "") {
                p['fieldId|like'] = fieldId;
            }
            if (nameEn != null && nameEn != "") {
                p['nameZh|like'] = nameEn;
            }
            if (fieldType != null && fieldType != "") {
                p['fieldType'] = fieldType;
            }
            if (messageType != null && messageType != "") {
                p['msgType|like'] = messageType;
            }
            $("#dg").datagrid('load', p);
        }

        function addOrUpdate(type) {
            var title = "新增";
            var param = {action: type};
            if (type == 'update') {
                var rows = $("#dg").datagrid('getSelections');
                if (rows == null || rows.length != 1) {
                    showMsg("提示", "请选择一条记录进行修改", true);
                    return;
                }
                var row = $("#dg").datagrid('getSelected');
                if (row == null) {
                    showMsg("提示", "请选择要修改的记录", true);
                    return;
                } else {
                    param.id = row.id;
                    title = "编辑";
                }
            } else {
                title = "新增";
            }
            commonShowWindow(param, title, "${ctx}/msg/field/add", 530, 240, true, false, false);
        }

        function del() {
            var nodes = $("#dg").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            if (nodes && nodes.length > 0) {
                if (showConfirm("提示", "确定要删除用户吗？", function () {
                            $.post("${ctx}/msg/field/del", {ids: ids}, function (data) {
                                if (data.success) {
                                    $("#dg").datagrid('reload');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });

                        }));
            } else {
                showMsg("提示", "请选择要删除的用户", true);
            }
        }

        function editField(){
            var rows = $("#dg").datagrid('getSelections');
            if (rows == null || rows.length != 1) {
                showMsg("提示", "请选择一条记录进行修改", true);
                return;
            }
            var row = $("#dg").datagrid('getSelected');
            if (row == null) {
                showMsg("提示", "请选择要修改的记录", true);
                return;
            }

            $('#myWindow').window({
                title: '修改域类型',
                width: 460,
                height: 420,
                href: '${ctx}/msg/field/editField/' +row.id,
                modal: true,
                minimizable: false,
                maximizable: false,
                shadow: false,
                cache: false,
                closed: false,
                collapsible: false,
                resizable: false,
                loadingMessage: '正在加载数据，请稍等......',
                onClose: function () {
                    $("#enumTable").datagrid('reload');
                }
            });

            /*var url = "${ctx}/msg/field/editField/" + row.id;
            showWindow("域类型修改", url, 850, 775, true, false, false);*/
        }

    </script>
</head>
<body>
<div class="toolBar">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                   onclick="addOrUpdate('add');">增加</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="del();">删除</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                   onclick="addOrUpdate('update');">修改</a>
                <%--<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                   onclick="editField();">域类型修改</a>--%>
            </td>
        </tr>
    </table>
    <form id="queryForm">
        <table>
            <tr>
                <td>域id:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="fId" name="fId" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>域名称:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="nEn" name="nEn" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>域类型:</td>
                <td>
                    <input class="easyui-combobox" id="fType" name="fType" style="width: 100px" panelHeight="auto"
                           valueField="id" textField="text" limitToList="true" url="${ctx}/generic/dict/FIELDTYPE" onenter="onSearch"/>
                </td>
                <td>报文类型:</td>
                <td>
                    <input class="easyui-combobox" id="mType" name="mType" style="width: 100px" panelHeight="auto"
                           valueField="id" textField="text" limitToList="true" url="${ctx}/generic/dict/MSGTYPE" onenter="onSearch"/>
                </td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>
            </tr>
        </table>
    </form>
</div>
<div id="windowCode"></div>
<table id="dg"></table>
</body>
</html>
