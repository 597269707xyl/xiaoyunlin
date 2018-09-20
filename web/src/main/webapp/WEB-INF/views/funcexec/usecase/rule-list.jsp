<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="width: 100%;height: 93%">
    <table id="dg-rule-list"></table>
</div>
<div id="isShowAddRule" style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
       onclick="onAddRuleListOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
       onclick="onAddRuleListCancel();">取消</a>
</div>
<div id="isShowAddRule1" style="text-align:center;">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
       onclick="onAddRuleListCancel();">关闭</a>
</div>
<script>
    var usecaseDataId;
    var source;
    var index;
    function setMyData(param) {
        source=param.source;
        index = param.index;
        usecaseDataId = param.usecaseDataId;
        var url;
        if (typeof (usecaseDataId) == 'undefined' || usecaseDataId == '' || usecaseDataId == 0) {
            url = '';
            usecaseDataId = 0;
            document.getElementById('isShowAddRule').style.display = '';
            document.getElementById('isShowAddRule1').style.display = 'none';
        } else {
            url = '${ctx}/func/exec/rules/' + usecaseDataId ;
            document.getElementById('isShowAddRule').style.display = 'none';
            document.getElementById('isShowAddRule1').style.display = '';
        }
        $("#dg-rule-list").datagrid({
            fit: true,
            title: '取值规则列表',
            fitColumns: true,
            url: url,
            height: 350,
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {field: 'destMsgCode', title: '原报文编码', width: 60},
                {field: 'destFieldId', title: '原域Id', width: 100},
                {field: 'destFieldZh', title: '原域名称', width: 50},
                {field: 'srcMsgCode', title: '目标报文编码', width: 60},
                {field: 'srcFieldId', title: '目标域Id', width: 100},
                {field: 'srcFieldZh', title: '目标域名称', width: 50},
                {field: 'from', title: '类型', width: 40}
            ]],
            toolbar: [{
                text: '添加',
                iconCls: 'icon-add',
                handler: function () {
                    $('#window-rule-add').window({
                        title: '添加取值规则',
                        width: 480,
                        height: 300,
                        href: '${ctx}/func/exec/ruleAddPage/',
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
            }, '-', {
                text: '删除',
                iconCls: 'icon-remove',
                handler: function () {
                    var rows = $("#dg-rule-list").datagrid('getSelections');
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
                        var data = $('#dg-rule-list').datagrid('getRows');
                        for (var i = 0; i < data.length; i++) {
                            for (var j = 0; j < rows.length; j++) {
                                if (data[i].destFieldId == rows[j].destFieldId) {
                                    data.remove(i);
                                    i--;
                                    break;
                                }
                            }
                        }
                        $('#dg-rule-list').datagrid('loadData', data);
                        $('#ruleDataList').textbox('setValue', JSON.stringify(data));
                    } else {
                        $.post("${ctx}/func/exec/deleteRules", {ids: ids}, function (data) {
                            if (data.success) {
                                $("#dg-rule-list").datagrid('reload');
                            } else {
                                showMsg("提示", data.msg, true);
                            }
                        });
                    }
                }
            }]
        });
        var jsonStr = param.ruleDataList;
        if (typeof (jsonStr) != 'undefined' && jsonStr != '') {
            $('#dg-rule-list').datagrid('loadData', JSON.parse(jsonStr));
        } else {
            $('#dg-rule-list').datagrid({url: '${ctx}/func/exec/rules/' + usecaseDataId});
        }
    }
    function onAddRuleListOk() {
        var rows = $('#dg-rule-list').datagrid('getRows');
        var jsonStr = JSON.stringify(rows);
        if(source == 'data'){
            $('#ruleDataList').textbox('setValue', jsonStr);
        } else if(source == 'list'){
            var rows = $('#dg-usecase-data-list').datagrid('getRows');
            rows[index].ruleDataList = jsonStr;
            $('#dg-usecase-data-list').datagrid('loadData', rows);
        }
        onAddRuleListCancel();
    }
    function onAddRuleListCancel() {
        $('#window-rule-list').window('close');
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
