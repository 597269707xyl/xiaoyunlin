<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="height: 310px">
    <table id="tg-usecase-data-detail"></table>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onUsecaseDataDetailOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onUsecaseDataDetailCancel();">取消</a>
</div>
<script>
    var messageId;
    var usecaseDataId;
    var source;
    var index;
    function setData(param){
        var usecaseDataList = param.usecaseDataList;
        messageId = param.messageId;
        usecaseDataId = param.usecaseDataId;
        source = param.source;
        index = param.index;
        var param = 'messageId='+messageId;
        if(typeof (usecaseDataId) != 'undefined' && usecaseDataId != '' && usecaseDataId != 0){
            param = 'usecaseDataId='+usecaseDataId;
        }
        if(typeof(usecaseDataList) == "undefined" || usecaseDataList == '' || usecaseDataList == '[]'){
            $('#tg-usecase-data-detail').treegrid({
                fit:true,
                url:'${ctx}/func/exec/msgData?'+param,
                idField:'id',
                treeField:'name',
                fitColumns: true,
                height: 410,
                editable:true,
                columns:[[
                    {title: 'id', field: 'id', hidden: true},
                    {title:'域名称',field:'name',width:150},
                    {title:'中文名称',field:'nameZh',width:100},
                    {title:'值',field:'value',width:100,editor:'text'}
                ]],
                onClickCell:function(field,row){

                }
            });
        } else {
            $('#tg-usecase-data-detail').treegrid({
                fit:true,
                idField:'id',
                treeField:'name',
                fitColumns: true,
                height: 410,
                editable:true,
                columns:[[
                    {title: 'id', field: 'id', hidden: true},
                    {title:'域名称',field:'name',width:150},
                    {title:'中文名称',field:'nameZh',width:80},
                    {title:'值',field:'value',width:150,editor:'text'}
                ]]
            });
            $('#tg-usecase-data-detail').treegrid('loadData',JSON.parse(usecaseDataList));
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
        $('#tg-usecase-data-detail').treegrid().treegrid('enableCellEditing');

    }
    function onUsecaseDataDetailOk(){
        var node = $('#tg-usecase-data-detail').treegrid('getSelected');
        if (node != null){
            $('#tg-usecase-data-detail').treegrid('endEdit',node.id);
        }
        var rows = $('#tg-usecase-data-detail').treegrid('getData');
        var strJson = JSON.stringify(rows);
        if(typeof (usecaseDataId) == 'undefined' || usecaseDataId == 0){
            if(source == 'data'){
                $('#usecaseDataList').textbox('setValue',strJson);
            } else if(source == 'list'){
                var rows = $('#dg-usecase-data-list').datagrid('getRows');
                rows[index].usecaseDataList = strJson;
                $('#dg-usecase-data-list').datagrid('loadData', rows);
            }
            onUsecaseDataDetailCancel();
        } else {
            $.messager.progress();
            $.ajax({
                url: '${ctx}/func/exec/updateUsecaseDataMsg',
                type: 'POST',
                data: {usecaseDataId: usecaseDataId, msg: strJson},
                success: function(data){
                    $.messager.progress('close');
                    if(data.success){
                        onUsecaseDataDetailCancel();
                    } else {
                        showMsg('提示', '修改失败!', true);
                    }
                }
            })
        }
    }

    function onUsecaseDataDetailCancel(){
        $('#window-usecase-data-detail').window('close');
    }
</script>
