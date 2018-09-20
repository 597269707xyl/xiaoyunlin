<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<table>
    <tr>
        <td>请求报文：</td>
        <td>
            <input class="easyui-textbox" type="text" id="requestname" name="requestname" style="width: 300px"/>
        </td>
    </tr>
</table>
<div style="height: 350px">
<table id="dg-reply-list"></table>
</div>

<script>
    function setMyData(param){
        $('#requestname').textbox('setValue',param.requestname);
        $("#dg-reply-list").datagrid({
            fit:true,
            title: '应答报文列表',
            fitColumns: true,
            url:'${ctx}/sim/instance/message/getResponseMsgs?requestId='+param.id,
            height: 350,
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {field: 'name', title: '应答报文名称', width: 200},
                {field: 'msgType', title: '报文代码', width: 50}
            ]],
            toolbar: [{
                text:'添加',
                iconCls: 'icon-add',
                handler: function(){
                    $('#window-replyset').window({
                        title: '选择应答报文',
                        width: 480,
                        height: 420,
                        href: '${ctx}/sim/instance/message/listReponseMsgs?type=XML',
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
                            $("#dg-reply-list").datagrid('reload');
                        }
                    });
                }
            },'-',{
                text:'删除',
                iconCls: 'icon-remove',
                handler: function(){
                    var rows = $("#dg-reply-list").datagrid('getSelections');
                    var ids = new Array();
                    if(rows && rows.length>0){
                        for(var i= 0;i<rows.length;i++){
                            ids.push(rows[i].id);
                        }
                    }else {
                        showMsg("提示","请选择要删除的记录",true);
                        return;
                    }
                    var request = param.id;
                    $.post("${ctx}/sim/instance/message/deleteResponses", {requestId: param.id, ids: ids}, function (data) {
                        if (data.success) {
                            $("#dg-reply-list").datagrid('reload');
                        } else {
                            showMsg("提示",data.msg,true);
                        }
                    });
                }
            }]
        });
    }
    function onSetCancel(){
        closeWindow();
    }
</script>
