<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>来账报文标识列表</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <script>
        $(function () {
            $('#dg').datagrid({
                //fit: true,
                height: $(window).height() - 80,
                fitColumns: true,
                url: '${ctx}/generic/commonQuery/epccMark',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'msgCode', title: '报文编号', width: 70},
                    {field: 'msgName', title: '报文名称', width: 70},
                    {field: 'fieldId', title: '域ID', width: 140,editor:'text'},
                    {field: 'standardDis', title: '报文标准', width: 70}
                ]]
            }).datagrid('enableCellEditing');
        });
        function onSearch() {
            var msgCode = $("#msgCode").textbox("getValue");
            var standard = $("#standard").combobox("getValue");
            var p = {};
            if (msgCode != null && msgCode != "") {
                p['msgCode|like'] = msgCode;
            }
            if(standard != null && standard != ""){
                p['standard'] = standard;
            }
            $("#dg").datagrid('load', p);
        }

        function add() {
            commonShowWindow(null, "选择报文", "${ctx}/func/epcc/msgList", 680, 500, true, false, false);
        }
        function del() {
            var nodes = $("#dg").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            if (nodes && nodes.length > 0) {
                if (showConfirm("提示", "确定要删除该标识吗？", function () {
                            $.post("${ctx}/func/epcc/del", {ids: ids}, function (data) {
                                if (data.success) {
                                    $("#dg").datagrid('reload');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });

                        }));
            } else {
                showMsg("提示", "请选择要删除的标识", true);
            }
        }

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
                                        url: "${ctx}/func/epcc/update",
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
    </script>
</head>
<body>
<div class="toolBar">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="add();">增加</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="del();">删除</a>
            </td>
        </tr>
    </table>
    <form id="queryForm">
        <table>
            <tr>
                <td></td>
                <td>报文编号:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="msgCode" name="msgCode" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>报文标准:</td>
                <td><input class="easyui-combobox" id="standard" name="standard" style="width: 200px"
                           valueField="id" textField="text" limitToList="true" url="${ctx}/generic/dict/STANDARD" panelHeight="auto"
                           onenter="onSearch"/></td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>
            </tr>
        </table>
    </form>
</div>
<div style="height: auto">
    <table id="dg"></table>
</div>
<div id="window-mark-add-panel"></div>
</body>
</html>
