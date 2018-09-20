<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="replyQueryForm">
    <input type="hidden" id="requestid" name="requestid"/>
    <table>
        <tr>
            <td>报文名称:</td>
            <td>
                <input class="easyui-textbox" type="text" id="searchReplyname" name="searchReplyname" style="width: 100px"
                       onenter="onSearch"/>
            </td>
            <td>报文代码:</td>
            <td>
                <input class="easyui-textbox" type="text" id="replymsgtrsCode" name="replymsgtrsCode" style="width: 100px"
                       onenter="onSearch"/>
            </td>
            <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                   onclick="onMySearch();">查询</a></td>
        </tr>
    </table>
</form>
<div style="height: 300px;">
<table id="dg-reply-select"></table>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onSetOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onSetCancel();">取消</a>
</div>

<script>
    function setData(param){
        $('#requestid').val(param.id);
        $.ajax({
            url: "${ctx}/sim/message/get/" + param.id,
            cache: false,
            success: function (vo) {
                $("#dg-reply-select").datagrid({
                    fit:true,
                    title: '报文列表',
                    fitColumns: true,
                    url:'${ctx}/generic/commonQuery/simMessage?type=XML&simSystem.id='+vo.simSystem.id,
                    height: 300,
                    singleSelect:true,
                    pagination: true,
                    pageSize: 10,
                    pageList: [10, 20, 30, 50],
                    columns: [[
                        {field: 'id', title: 'id', hidden: true},
                        {field: 'name', title: '报文名称', width: 200},
                        {field: 'mesgType', title: '报文代码', width: 50}
                    ]]
                });
            }
        });

    }

    function onMySearch(){
        var name = $("#searchReplyname").textbox("getValue");
        var trsCode = $("#replymsgtrsCode").textbox("getValue");
        var p = {};
        if (name != null && name != "") {
            p['name|like'] = name;
        }
        if (trsCode != null && trsCode != "") {
            p['mesgType|like'] = trsCode;
        }
        $("#dg-reply-select").datagrid('load', p);
    }

    function onSetCancel(){
        $('#window-replyset').window('close');
    }
    function onSetOk(){
        var rows = $("#dg-reply-select").datagrid('getSelections');
        var ids = new Array();
        if(rows && rows.length>0){
            for(var i= 0;i<rows.length;i++){
                ids.push(rows[i].id);
            }
        }else {
            showMsg('提示','请选择报文',true);
            return;
        }
        var requestid = $("#requestid").val();
        $('#window-reply-param').window({
            title: '选择应答报文类型',
            width: 200,
            height: 130,
            href: '${ctx}/sim/message/listReponseMsgParam?type=XML',
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
                var param = {};
                param.requestId = requestid;
                param.responsIds = ids;
                setParamData(param)
            },
            onClose: function () {
                $("#dg-reply-list").datagrid('reload');
            }
        });
        onSetCancel();
    }

</script>
