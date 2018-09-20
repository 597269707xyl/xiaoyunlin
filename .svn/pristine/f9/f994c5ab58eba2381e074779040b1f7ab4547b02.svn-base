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
                <td><input id="insname" name="insname" data-options="required:true" value="${msgField.fieldValueType}"
                           onChange="showHide"/>
                </td>
            </tr>
        </table>
    </div>
    <div id="enum" style="display: block;width: auto" >
        <%--<div class="toolBar"  data-options="region:'north'">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                           onclick="addCode();">增加</a>
                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
                           onclick="delCodes();">删除</a>
                        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                           onclick="editCode();">修改</a>
                    </td>
                </tr>
            </table>
        </div>--%>
        <div id="tb" style="height:auto">
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="append()">Append</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">Remove</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="accept()">Accept</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="reject()">Reject</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="getChanges()">GetChanges</a>
        </div>
        <table id="enumTable"></table>
    </div>
    <div id="others" style="display: none">
        <table class="single">
            <tr>
                <td>数据格式:</td>
                <td><input id="valueParam" name="valueParam" data-options="required:true"
                           value="${msgField.fieldValueTypeParam}"/>
                </td>
            </tr>
        </table>
    </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="submitForm();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="closeWindow();">取消</a>
    </div>

</form>
<script>
    $("#insname").combobox({
        url: '${ctx}/generic/dict/FIELD_VALUE_TYPE',
        valueField: 'id',
        textField: 'text',
        panelHeight: 'auto',
        multiple: false,
        limitToList: true,
        onChange: function () {
            var actionCode = $("#insname").combobox("getValue");
            if (actionCode == 'ENUMFORMAT') {
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
            singleSelect: true,
            onClickCell: onClickCell,
            onEndEdit: onEndEdit,
            required:true,
            url: '${ctx}/msg/field/enum/'+${msgField.id},
            columns: [[
                {field: 'ck', width: 40},
                {field: 'key', title: '代码', width: 70,editor:'text',required:true},
                {field: 'value', title: '名称', width: 70,editor:'text',required:true}
            ]]
        });
    });
    /*$.extend($.fn.datagrid.methods, {
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
    function addCode(){
        $('#window-replyset').window({
            title: '选择应答报文',
            width: 480,
            height: 420,
            href: '${ctx}/msg/field/addCode/'+${msgField.id},
            modal: true,
            minimizable: false,
            maximizable: false,
            shadow: false,
            cache: false,
            closed: false,
            collapsible: false,
            resizable: false,
            loadingMessage: '正在加载数据，请稍等......',
            onLoad: function () {
                setData(param)
            },
            onClose: function () {
                $("#enumTable").datagrid('reload');
            }
        });
    }
    function delCodes(){
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


    var editIndex = undefined;
    function endEditing(){
        if (editIndex == undefined){return true}
        if ($('#enumTable').datagrid('validateRow', editIndex)){
            $('#enumTable').datagrid('endEdit', editIndex);
            editIndex = undefined;
            return true;
        } else {
            return false;
        }
    }
    function onClickCell(index, field){
        if (editIndex != index){
            if (endEditing()){
                $('#enumTable').datagrid('selectRow', index)
                        .datagrid('beginEdit', index);
                var ed = $('#enumTable').datagrid('getEditor', {index:index,field:field});
                if (ed){
                    ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                }
                editIndex = index;
            } else {
                setTimeout(function(){
                    $('#enumTable').datagrid('selectRow', editIndex);
                },0);
            }
        }
    }
    function onEndEdit(index, row){
        var ed = $(this).datagrid('getEditor', {
            index: index,
            field: 'key'
        });
        //row.key = $(ed.target).textbox('getText');
    }
    function append(){
        if (endEditing()){
            $('#enumTable').datagrid('appendRow',{status:'P'});
            editIndex = $('#enumTable').datagrid('getRows').length-1;
            $('#enumTable').datagrid('selectRow', editIndex)
                    .datagrid('beginEdit', editIndex);
        }
    }
    function removeit(){
        if (editIndex == undefined){return}
        $('#enumTable').datagrid('cancelEdit', editIndex)
                .datagrid('deleteRow', editIndex);
        editIndex = undefined;
    }
    function accept(){
        if (endEditing()){
            $('#enumTable').datagrid('acceptChanges');
        }
    }
    function reject(){
        $('#enumTable').datagrid('rejectChanges');
        editIndex = undefined;
    }
    function getChanges(){
        var rows = $('#enumTable').datagrid('getChanges');
        alert(rows.length+' rows are changed!');
    }
</script>
