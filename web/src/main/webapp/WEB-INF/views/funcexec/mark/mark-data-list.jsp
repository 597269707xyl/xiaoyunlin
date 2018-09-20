<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="width:100%;height: 90%;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'west',split:true,title:'来账报文',collapsible:false" style="width:50%;,height:80%">
            <table id="dg-detail-recv"></table>
        </div>
        <div data-options="region:'center',title:'应答报文'">
            <table id="dg-detail-send"></table>
        </div>
    </div>
</div>
<div style="text-align:center;padding:5px">
    <a id="close" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
       onclick="onMarkDataCloseOk();">关闭</a>
    <a id="send" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-tip'"
       onclick="send();">发送</a>
</div>
<script>
    var id;
    function setData(recvId, type) {
        id = recvId;
        $('#dg-detail-send').treegrid('loadData', new Array());
        $('#dg-detail-recv').treegrid('loadData', new Array());
        if (recvId) {
            $('#dg-detail-send').treegrid({url: '${ctx}/func/mark/sendMsg?id=' + recvId});
            $('#dg-detail-recv').treegrid({url: '${ctx}/func/mark/recvMsg?id=' + recvId});
        }
        $.extend($.fn.datagrid.methods, {
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
        if (type == 1) {
            $('#dg-detail-send').treegrid().treegrid('enableCellEditing');
            $('#send').show();
            $('#close').hide();
        } else {
            $('#send').hide();
            $('#close').show();
        }
    }
    $('#dg-detail-recv').treegrid({
        fit: true,
        height: 555,
        fitColumns: true,
        idField: 'id',
        treeField: 'name',
        columns: [[
            {field: 'name', title: '域id', width: 80},
            {field: 'nameZh', title: '域中文名称', width: 100},
            {field: 'value', title: '发送值', width: 150}
        ]]
    });
    $('#dg-detail-send').treegrid({
        fit: true,
        height: 555,
        fitColumns: true,
        idField: 'id',
        treeField: 'name',
        editable: true,
        columns: [[
            {field: 'name', title: '域id', width: 80},
            {field: 'nameZh', title: '域中文名称', width: 100},
            {field: 'value', title: '接收值', width: 150, editor: 'text'}
        ]]
    });
    function onMarkDataCloseOk() {
        $('#window-mark-list').window('close');
    }
    function send() {
        var json = JSON.stringify($('#dg-detail-send').treegrid('getData'));
        $.ajax({
            url: '${ctx}/func/mark/sendManualMsg',
            type: 'POST',
            data: {id:id,json: json},
            success: function(data){
                if(data.success){
                    $('#window-mark-list').window('close');
                    showMsg('提示', '发送成功!', true);
                } else {
                    showMsg('提示', data.msg, true);
                }
            }
        })
    }
</script>