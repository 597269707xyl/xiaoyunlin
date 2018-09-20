<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="height: 350px">
    <table id="dg-expected-list"></table>
</div>
<div id="isShowAddExcepted" style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
       onclick="onAddExpectedOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
       onclick="onAddExpectedCancel();">取消</a>
</div>
<div id="isShowAddExcepted1" style="text-align:center;">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
       onclick="onAddExpectedCancel();">关闭</a>
</div>
<script>
    var usecaseDataId;
    var type;
    var source;
    var index;
    function setMyData(param) {
        type=param.type;
        source=param.source;
        index = param.index;
        usecaseDataId = param.usecaseDataId;
        var url;
        if (typeof (usecaseDataId) == 'undefined' || usecaseDataId == '' || usecaseDataId == 0) {
            url = '';
            usecaseDataId = 0;
            document.getElementById('isShowAddExcepted').style.display = '';
            document.getElementById('isShowAddExcepted1').style.display = 'none';
        } else {
            url = '${ctx}/func/exec/expectedList/' + usecaseDataId + "?type="+type;
            document.getElementById('isShowAddExcepted').style.display = 'none';
            document.getElementById('isShowAddExcepted1').style.display = '';
        }
        $("#dg-expected-list").datagrid({
            fit: true,
            title: '预期值列表',
            fitColumns: true,
            url: url,
            height: 350,
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {field: 'fieldId', title: '域Id', width: 300},
                {field: 'nameZh', title: '域名称', width: 100},
                {field: 'expectedValue', title: '预期值', width: 100}
            ]],
            toolbar: [{
                text: '添加',
                iconCls: 'icon-add',
                handler: function () {
                    $('#window-field-list').window({
                        title: '选择域',
                        width: 480,
                        height: 420,
                        href: '${ctx}/func/exec/field/',
                        modal: true,
                        minimizable: false,
                        maximizable: false,
                        shadow: false,
                        cache: false,
                        closed: false,
                        collapsible: false,
                        resizable: true,
                        loadingMessage: '正在加载数据，请稍等......',
                        onLoad: function () {
                            setData(param)
                        },
                        onClose: function () {
                        }
                    });
                }
            }, '-',{
                text: '修改',
                iconCls: 'icon-edit',
                handler: function () {
                    var rows = $("#dg-expected-list").datagrid('getSelections');
                    if(rows.length != 1){
                        showMsg("提示", "请选择一条记录", true);
                        return;
                    }
                    var row = $("#dg-expected-list").datagrid('getSelected');
                    $('#window-field-list').window({
                        title: '修改预期值',
                        width: 480,
                        height: 200,
                        href: '${ctx}/func/exec/updateExpected',
                        modal: true,
                        minimizable: false,
                        maximizable: false,
                        shadow: false,
                        cache: false,
                        closed: false,
                        collapsible: false,
                        resizable: true,
                        loadingMessage: '正在加载数据，请稍等......',
                        onLoad: function () {
                            setData(row);
                        },
                        onClose: function () {
                        }
                    });
                }
            }, '-', {
                text: '删除',
                iconCls: 'icon-remove',
                handler: function () {
                    var rows = $("#dg-expected-list").datagrid('getSelections');
                    var ids = new Array();
                    if (rows && rows.length > 0) {
                        for (var i = 0; i < rows.length; i++) {
                            ids.push(rows[i].id);
                        }
                    } else {
                        showMsg("提示", "请选择要删除的记录", true);
                        return;
                    }
                    if (typeof (usecaseDataId) == 'undefined' || usecaseDataId == 0) {
                        var data = $('#dg-expected-list').datagrid('getRows');
                        for (var i = 0; i < data.length; i++) {
                            for (var j = 0; j < rows.length; j++) {
                                if (data[i].fieldId == rows[j].fieldId) {
                                    data.remove(i);
                                    i--;
                                    break;
                                }
                            }
                        }
                        $('#dg-expected-list').datagrid('loadData', data);
                        $('#expectedData').textbox('setValue', JSON.stringify(data));
                    } else {
                        $.post("${ctx}/func/exec/deleteExcepted", {ids: ids}, function (data) {
                            if (data.success) {
                                $("#dg-expected-list").datagrid('reload');
                            } else {
                                showMsg("提示", data.msg, true);
                            }
                        });
                    }
                }
            }]
        });
        if(type == 'expect'){
            var jsonStr = param.expectedDataList;
            if (typeof (jsonStr) != 'undefined' && jsonStr != '') {
                $('#dg-expected-list').datagrid('loadData', JSON.parse(jsonStr));
            } else {
                $('#dg-expected-list').datagrid({url: '${ctx}/func/exec/expectedList/' + usecaseDataId + "?type="+type});
            }
        } else if(type == 'next'){
            var jsonStr = param.replyDataList;
            if (typeof (jsonStr) != 'undefined' && jsonStr != '') {
                $('#dg-expected-list').datagrid('loadData', JSON.parse(jsonStr));
            } else {
                $('#dg-expected-list').datagrid({url: '${ctx}/func/exec/expectedList/' + usecaseDataId + "?type="+type});
            }
        }
    }
    function onAddExpectedOk() {
        var rows = $('#dg-expected-list').datagrid('getRows');
        var jsonStr = JSON.stringify(rows);
        if(type == 'expect' && source == 'data'){
            $('#expectedDataList').textbox('setValue', jsonStr);
        } else if(type == 'next' && source == 'data'){
            $('#replyDataList').textbox('setValue', jsonStr);
        } else if(type == 'expect' && source == 'list'){
            var rows = $('#dg-usecase-data-list').datagrid('getRows');
            rows[index].expectedDataList = jsonStr;
            $('#dg-usecase-data-list').datagrid('loadData', rows);
        } else if(type == 'next' && source == 'list'){
            var rows = $('#dg-usecase-data-list').datagrid('getRows');
            rows[index].replyDataList = jsonStr;
            $('#dg-usecase-data-list').datagrid('loadData', rows);
        }
        onAddExpectedCancel();
    }
    function onAddExpectedCancel() {
        $('#window-expected-list').window('close');
    }
    Array.prototype.remove = function (dx) {
        if (isNaN(dx) || dx > this.length) {
            return false;
        }
        for (var i = 0, n = 0; i < this.length; i++) {
            if (this[i] != this[dx]) {
                this[n++] = this[i]
            }
        }
        this.length -= 1
    }
</script>
