<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/3/3
  Time: 16:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post">
    <div class="popup">
        <input type="hidden" id="id" name="id"/>
        <table class="single">
            <tr>
                <td>数据类型:</td>
                <td><input id="fieldValueType" class="easyui-textbox" name="fieldValueType" data-options="required:true" value="${msgField.fieldValueType}"
                           onChange="showHide"/>
                </td>
            </tr>
        </table>
    </div>
    <div id="enum" style="display: block;width: auto">
        <div class="toolBar" data-options="region:'north'">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                           onclick="addOrEditCode('add');">增加</a>
                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
                           onclick="delCodes();">删除</a>
                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                           onclick="addOrEditCode('edit');">修改</a>
                    </td>
                </tr>
            </table>
        </div>
        <table id="enumTable"></table>
    </div>
    <div id="others" class="popup" style="display: none">
        <table class="single">
            <tr>
                <td>数据格式:</td>
                <td><input id="valueParam" class="easyui-textbox" name="valueParam" data-options="required:true"
                           value="${msgField.fieldValueTypeParam}"/>
                </td>
            </tr>
        </table>
        <div class="buttonBar">
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
               onclick="editFieldValueType();">确定</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
               onclick="closeWindow();">取消</a>
        </div>
    </div>
</form>

<script>
    $("#fieldValueType").combobox({
        url: '${ctx}/generic/dict/FIELD_VALUE_TYPE',
        valueField: 'id',
        textField: 'text',
        panelHeight: 'auto',
        multiple: false,
        limitToList: true,
        onLoadSuccess:function () {
            var actionCode = $("#fieldValueType").combobox("getValue");
            if (actionCode == 'enum') {
                $("#enum").show();
                $("#others").hide();
            } else {
                $("#enum").hide();
                $("#others").show();
            }
        },
        onChange: function () {
            var actionCode = $("#fieldValueType").combobox("getValue");
            if (actionCode == 'enum') {
                $("#enum").show();
                $("#others").hide();
            } else {
                $("#enum").hide();
                $("#others").show();
            }
        }
    });
    $(function () {
        $('#enumTable').datagrid({
            fitColumns: true,
            singleSelect: false,
            url: '${ctx}/generic/commonQuery/messageFieldCode?msgField.id=' +${msgField.id},
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'key', title: '代码', width: 70, editor: 'text'},
                {field: 'value', title: '名称', width: 70, editor: 'text'}
            ]]
        });
    });
    /*$.extend($.fn.datagrid.methods, {
        editCell: function (jq, param) {
            return jq.each(function () {
                var opts = $(this).datagrid('options');
                var fields = $(this).datagrid('getColumnFields', true).concat($(this).datagrid('getColumnFields'));
                for (var i = 0; i < fields.length; i++) {
                    var col = $(this).datagrid('getColumnOption', fields[i]);
                    col.editor1 = col.editor;
                    if (fields[i] != param.field) {
                        col.editor = null;
                    }
                }
                $(this).datagrid('beginEdit', param.index);
                var ed = $(this).datagrid('getEditor', param);
                if (ed) {
                    if ($(ed.target).hasClass('textbox-f')) {
                        $(ed.target).textbox('textbox').focus();
                    } else {
                        $(ed.target).focus();
                    }
                }
                for (var i = 0; i < fields.length; i++) {
                    var col = $(this).datagrid('getColumnOption', fields[i]);
                    col.editor = col.editor1;
                }
            });
        },
        enableCellEditing: function (jq) {
            return jq.each(function () {
                var dg = $(this);
                var opts = dg.datagrid('options');
                opts.oldOnClickCell = opts.onClickCell;
                opts.onClickCell = function (index, field) {
                    if (opts.editIndex != undefined) {
                        if (dg.datagrid('validateRow', opts.editIndex)) {
                            dg.datagrid('endEdit', opts.editIndex);
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
    $('#enumTable').datagrid().datagrid('enableCellEditing');*/
    function getData() {
        var formData = {};
        formData.msgFieldId = ${msgField.id}
        formData.fieldValueType = $('#fieldValueType').combobox("getValue");
        formData.fieldValueTypeParam = $('#valueParam').textbox('getValue');
        return formData;
    }
    function editFieldValueType() {
        $.ajax({
            url: '${ctx}/msg/field/editField/',
            type: 'post',
            data: getData.call(),
            cache: false,
            success: function (data) {
                $("#enumTable").datagrid('reload');
                closeWindow();
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.responseText);
                closeWindow();
            }
        });
    }
    function addCode() {
        var url = "${ctx}/msg/field/addCode/?id=" + ${msgField.id}+"&msgId=" +${msgField.id};
        showWindow("域类型修改", url, 250, 175, true, false, false);
    }
    function addOrEditCode(type) {
        if (type == 'edit') {
            var rows = $("#enumTable").datagrid('getSelections');
            if (rows == null || rows.length != 1) {
                showMsg("提示", "请选择一条记录进行修改", true);
                return;
            }
            var row = $("#enumTable").datagrid('getSelected');
            if (row == null) {
                showMsg("提示", "请选择要修改的记录", true);
                return;
            } else {
                title = "编辑";var url = "${ctx}/msg/field/addCode/?codeId=" + row.id + "&msgFieldId=" + ${msgField.id} +"&type=" + type;
            }
        } else {
            title = "新增";
            var url = "${ctx}/msg/field/addCode/?codeId=0&msgFieldId=" + ${msgField.id} +"&type=" + type;
        }
        $('#windowCode').window({
            title: title,
            width: 270,
            height: 175,
            href: url,
            modal: true,
            minimizable: false,
            maximizable: false,
            shadow: false,
            cache: false,
            closed: false,
            collapsible: false,
            resizable: false,
            loadingMessage: '正在加载数据，请稍等......',
            onLoad:function(){
                if (type == 'edit'){
                    setData();
                }
            }
        });
    }
    function delCodes() {
        var nodes = $("#enumTable").datagrid('getSelections'),
                ids = new Array();
        if (nodes && nodes.length > 0) {
            for (var i = 0; i < nodes.length; i++) {
                ids.push(nodes[i].id);
            }
        }
        if (nodes && nodes.length > 0) {
            if (showConfirm("提示", "确定要删除吗？", function () {
                        $.post("${ctx}/msg/field/delCodes/", {ids: ids}, function (data) {
                            if (data.success) {
                                $("#enumTable").datagrid('reload');
                            } else {
                                showMsg("提示", "操作出错", true);
                            }
                        });

                    }));
        } else {
            showMsg("提示", "请选择要删除的项目", true);
        }
    }
    function closeWindowCode(){
        $('#windowCode').window('close');
    }
</script>
