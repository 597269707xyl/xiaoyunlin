<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="height: 350px">
    <table id="dg-usecase-data-list"></table>
</div>
<div id="usecaseDataListAddShow" style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
       onclick="onUsecaseDataListAddOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
       onclick="onUsecaseDataListAddCancel();">取消</a>
</div>
<div id="usecaseDataListAddShow1" style="text-align:center;">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
       onclick="onUsecaseDataListAddCancel();">关闭</a>
</div>
<script>
    function setData(id, itemId) {
        var usecaseId = id;
        var url = '${ctx}/func/exec/usecaseDataList/' + usecaseId;
        if (typeof (usecaseId) == 'undefined' || usecaseId == '') {
            usecaseId = 0;
            url = '';
            document.getElementById('usecaseDataListAddShow').style.display = '';
            document.getElementById('usecaseDataListAddShow1').style.display = 'none';
        } else {
            itemId = 0;
            document.getElementById('usecaseDataListAddShow').style.display = 'none';
            document.getElementById('usecaseDataListAddShow1').style.display = '';
        }
        $("#dg-usecase-data-list").datagrid({
            fit: true,
            title: '用例数据列表',
            fitColumns: true,
            url: url,
            height: 350,
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {field: 'messageName', title: '报文名称', width: 150},
                {field: 'messageCode', title: '报文编号', width: 150},
                {field: 'usecaseDataList', title: '测试数据', width: 60,
                    formatter: function (value, row, index){
                        var msgId = row.messageId;
                        if(msgId == null || msgId == ''){
                            msgId = row.simMessage.id;
                        }
                    return "<a href='#' onclick='showmsgdata(" + msgId + "," + row.id + "," + row.usecaseDataList + "," + index +")'>测试数据</a>";
                }},
                {field: 'expectedDataList', title: '预期值', width: 60,
                    formatter: function (value, row, index){
                        var msgId = row.messageId;
                        if(msgId == null || msgId == ''){
                            msgId = row.simMessage.id;
                        }
                    return "<a href='#' onclick='showexpecteddata(" + msgId + ","   + row.id +"," + row.expectedDataList + "," + index + ")'>预期值</a>";
                }},
//                {field: 'replyDataList', title: '业务回复值', width: 60,
//                    formatter: function (value, row, index){
//                        var replyDataList = row.replyDataList;
//                        if(typeof (replyDataList) == 'undefined' || replyDataList == '' || replyDataList == null){
//                            replyDataList = '[]';
//                        }
//                    return "<a href='#' onclick='showreplydata(" + row.id +"," + replyDataList + "," + index + ")'>业务回复值</a>";
//                }},
                {field: 'ruleDataList', title: '取值规则', width: 60,
                    formatter: function (value, row, index){
                        if(index == 0){
                            return;
                        }
                        var ruleDataList = row.ruleDataList;
                        if(typeof (ruleDataList) == 'undefined' || ruleDataList == '' || ruleDataList == null){
                            ruleDataList = '[]';
                        }
                        var messageId = row.messageId;
                        if(typeof (messageId) == 'undefined'){
                            messageId = row.simMessage.id;
                        }
                    return "<a href='#' onclick='showruledata(" + row.id +"," + ruleDataList + "," + index + "," + messageId + ")'>取值规则</a>";
                }},
                {field: 'name', title: '所属实例', width: 100,
                    formatter: function(value, row, index){
                        if(row.simMessage){
                            return row.simMessage.systemInstance.name;
                        }
                    return "";
                }}
            ]],
            toolbar: [{
                text: '添加',
                iconCls: 'icon-add',
                handler: function () {
                    $('#window-msg-list').window({
                        title: '选择报文',
                        width: 680,
                        height: 500,
                        href: '${ctx}/func/exec/msgList?itemId='+itemId+'&usecaseId='+usecaseId,
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
                            setData(id);
                        }
                    });
                }
            }, '-', {
                text: '删除',
                iconCls: 'icon-remove',
                handler: function () {
                    var rows = $("#dg-usecase-data-list").datagrid('getSelections');
                    var ids = new Array();
                    if (rows && rows.length > 0) {
                        for (var i = 0; i < rows.length; i++) {
                            ids.push(rows[i].id);
                        }
                    } else {
                        showMsg("提示", "请选择要删除的记录", true);
                        return;
                    }
                    if (usecaseId == 0) {
                        var data = $('#dg-usecase-data-list').datagrid('getRows');
                        for (var i = 0; i < data.length; i++) {
                            for (var j = 0; j < rows.length; j++) {
                                if (data[i].fieldId == rows[j].fieldId) {
                                    data.remove(i);
                                    i--;
                                    break;
                                }
                            }
                        }
                        $('#dg-usecase-data-list').datagrid('loadData', data);
                        $('#usecaseDataList').textbox('setValue', JSON.stringify(data));
                    } else {
                        $.post("${ctx}/func/exec/delUsecaseData", {ids: ids}, function (data) {
                            if (data.success) {
                                $("#dg-usecase-data-list").datagrid('reload');
                            } else {
                                showMsg("提示", data.msg, true);
                            }
                        });
                    }
                }
            }]
        });
        if(usecaseId == 0){
            var jsonStr = $('#usecaseAllDataList').textbox('getValue');
            if (jsonStr != '' && usecaseId == 0) {
                $('#dg-usecase-data-list').datagrid('loadData', JSON.parse(jsonStr));
            } else {
                $('#dg-usecase-data-list').datagrid({url: '${ctx}/func/exec/usecaseDataList/' + usecaseId});
            }
        }
    }
    //测试数据
    function showmsgdata(messageId, usecaseDataId, usecaseDataList, index){
        usecaseDataList = JSON.stringify(usecaseDataList);
        var param = {messageId: messageId, usecaseDataList: usecaseDataList, usecaseDataId: usecaseDataId, source: 'list', index: index};
        $('#window-usecase-data-detail').window({
            title: '测试数据',
            width: 650,
            height:400,
            href: '${ctx}/func/exec/msgPage',
            modal: false,
            minimizable: false,
            maximizable: false,
            shadow: false,
            cache: false,
            closed: false,
            collapsible: false,
            resizable: true,
            loadingMessage: '正在加载数据，请稍等......',
            onLoad: function () {
                setData(param);
            }
        });
    }
    //预期值
    function showexpecteddata(messageId, usecaseDataId, expectedDataList, index){
        expectedDataList = JSON.stringify(expectedDataList);
        var param = {messageId: messageId, expectedDataList: expectedDataList, usecaseDataId: usecaseDataId, type: 'expect', source: 'list', index: index};
        $('#window-expected-list').window({
            title: "预期值",
            width: 530,
            height: 460,
            href: '${ctx}/func/exec/expected',
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
                setMyData(param)
            },
            onClose: function () {
            }
        });
    }
    //业务回复值
    function showreplydata(usecaseDataId, replyDataList, index){
        replyDataList = JSON.stringify(replyDataList);
        var param = {replyDataList: replyDataList, usecaseDataId: usecaseDataId, type: 'next', source: 'list', index: index};
        $('#window-expected-list').window({
            title: "业务回复值",
            width: 530,
            height: 460,
            href: '${ctx}/func/exec/expected',
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
                setMyData(param)
            },
            onClose: function () {
            }
        });
    }
    //取值规则
    function showruledata(usecaseDataId, ruleDataList, index, messageId){
        ruleDataList = JSON.stringify(ruleDataList);
        var usecaseDataList = $("#dg-usecase-data-list").datagrid('getRows');
        var arrMsg = new Array();
        for(var i=0; i<index; i++){
            var messageIdd = usecaseDataList[i].messageId;
            if(typeof (messageIdd) == "undefined"){
                messageIdd = usecaseDataList[i].simMessage.id;
            }
            arrMsg.push({messageId: messageIdd, messageCode: usecaseDataList[i].messageCode, index: i+1});
        }
        var messageCode = usecaseDataList[index].messageCode;
        var param = {ruleDataList: ruleDataList, usecaseDataId: usecaseDataId, source: 'list', index: index, messageId: messageId, messageCode: messageCode, arrData: arrMsg};
        $('#window-rule-list').window({
            title: "取值规则列表",
            width: 700,
            height: 460,
            href: '${ctx}/func/exec/ruleListPage',
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
                setMyData(param)
            },
            onClose: function () {
            }
        });
    }
    //添加测试用例数据
    function onUsecaseDataListAddOk() {
        var rows = $('#dg-usecase-data-list').datagrid('getRows');
        var jsonStr = jsonify(rows);
        $('#usecaseAllDataList').textbox('setValue', jsonStr);
        onUsecaseDataListAddCancel();
    }
    var jsonify = function(obj){
        var seen = [];
        var json = JSON.stringify(obj, function(key, value){
            if (typeof value === 'object') {
                if ( !seen.indexOf(value) ) {
                    return '__cycle__' + (typeof value) + '[' + key + ']';
                }
                seen.push(value);
            }
            return value;
        }, 4);
        return json;
    };
    function onUsecaseDataListAddCancel() {
        $('#window-usecase-data-list').window('close');
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
