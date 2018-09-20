<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post">
    <input type="hidden" id="requestid" name="requestid"/>
    <input type="hidden" id="type" name="type"/>
    <table>
        <tr>
            <td>请求报文名称:</td>
            <td>
                <input class="easyui-textbox" id="requestname" name="requestname" style="width: 200px" editable="false"/>
            </td>
        </tr>
    </table>
    <form id="queryForm">
        <table>
            <tr>
                <td>报文名称:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="searchReplyname" name="searchReplyname" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>交易类代码:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="searchReplymesgType" name="searchReplymesgType" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>
            </tr>
        </table>
    </form>
    <table id="dg-replymessage-select"></table>
    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onSetOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onSetCancel();">取消</a>
    </div>
</form>

<script>
    function setData(param){
        $.ajax({
            url: "${ctx}/sim/instance/message/get/" + param.id,
            cache: false,
            success: function (vo) {
                $('#fm').form('load',vo);
                $('#requestid').val(vo.id);
                $("#requestname").textbox('setValue',vo.name);
            }
        });
    }
    function onSearch(){
        var name = $("#searchReplyname").textbox("getValue");
        var mesgType = $("#searchReplymesgType").textbox("getValue");
        var p = {};
        if (name != null && name != "") {
            p['name|like'] = name;
        }
        if (mesgType != null && mesgType != "") {
            p['mesgType|like'] = mesgType;
        }
        $("#dg-replymessage-select").datagrid('load', p);
    }
    $("#dg-replymessage-select").datagrid({
        title: '选择应答报文',
        fitColumns: true,
        url:'${ctx}/generic/commonQuery/simSysInsMessage?type=XML',
        height: 280,
        pagination: true,
        pageSize: 10,
        pageList: [10, 20, 30, 50],
        columns: [[
            {field: 'ck', checkbox: true},
            {field: 'id', title: 'id', hidden: true},
            {field: 'name', title: '应答报文名称', width: 100},
            {field: 'msgType', title: '应答报文编码', width: 100}
        ]]
    });

    function onSetOk(){
        var rows = $("#dg-replymessage-select").datagrid('getSelections');
        var ids = new Array();
        if(rows && rows.length>0){
            for(var i= 0;i<rows.length;i++){
                ids.push(rows[i].id);
            }
        }
        var requestid = $("#requestid").val();
        $.post("${ctx}/sim/instance/message/addReplyRule", {requestid: requestid, ids: ids}, function (data) {
            if (data.success) {
                onSetCancel();
            } else {
                showMsg("提示","操作失败",true);
            }
        });
    }

    function onSetCancel(){
        closeWindow();
    }
</script>
