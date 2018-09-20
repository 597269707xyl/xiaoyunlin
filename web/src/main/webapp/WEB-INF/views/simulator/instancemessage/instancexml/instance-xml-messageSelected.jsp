<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="form-message-select" method="post">
    <table>
    <tr>
        <td>选择要添加报文的仿真实例:</td>
        <td>
            <input id="simname" name="simname" style="width: 200px"/>
        </td>
        </tr>
        </table>
    <form id="queryForm">
        <table>
            <tr>
                <td>仿真系统:</td>
                <td>
                    <input id="simsys" name="simsys" style="width: 100px" data-options="limitToList:'true'"/>
                </td>
                <td>报文名称:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="searchmsgname" name="searchmsgname" style="width: 80px"
                           onenter="onSearchFrom"/>
                </td>
                <td>报文编号:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="msgtrsCode" name="msgtrsCode" style="width: 80px"
                           onenter="onSearchFrom"/>
                </td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearchFrom();">查询</a></td>
            </tr>
        </table>
    </form>
    <div style="height: 300px">
    <table id="dg-messages-select"></table>
    </div>
    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onSelOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onSelCancel();">取消</a>
    </div>
</form>

<script>
    var checkAllFlag = false;
    function onSearchFrom(){
        var name = $("#searchmsgname").textbox("getValue");
        var trsCode = $("#msgtrsCode").textbox("getValue");
        var simInsId=$("#simname").combobox("getValue");
        var simsys = $("#simsys").combobox("getValue");
        var p = {};
        if (name != null && name != "") {
            p['name|like'] = name;
        }
        if (trsCode != null && trsCode != "") {
            p['mesgType|like'] = trsCode;
        }
        if (simsys != null && simsys != "") {
            p['simSystem.id'] = simsys;
        }
        $("#dg-messages-select").datagrid('load', p);

    }
    $('#simname').combobox({
        valueField:'id',
        textField:'text',
        required:true,
        limitToList:true,
        url:'${ctx}/sim/instance/getAllSimSystemOrder?type=XML' ,panelHeight:'auto',
        onSelect: function(data){
            $.post("${ctx}/sim/instance/message/getSimSystem",{siminsId:data.id},function(d){
                $('#simsys').combobox('setValue',d);
                onSearch();
                //$("#dg-messages-select").datagrid('reload',{'simSystem.id':d});
            });

        }
    });

    $('#simsys').combobox({
        valueField:'id',
        textField:'text',
        url:'${ctx}/sim/system/getAllSimSystemOrder?type=XML' ,
        panelHeight:'200',
        panelWidth:'150',
        onSelect: function(data){

        }
    });

    $("#dg-messages-select").datagrid({
        fit:true,
        fitColumns: true,
        url:'${ctx}/generic/commonQuery/simMessage',
        height: 300,
        pagination: true,
        pageSize: 10,
        pageList: [10, 20, 30, 50],
        columns: [[
            {field: 'ck', checkbox: true},
            {field: 'id', title: 'id', hidden: true},
            {field: 'name', title: '报文名称', width: 100},
            {field: 'mesgType', title: '报文编号', width: 100},
            {field: 'signFlag', title: '是否加签', width: 100,
                formatter: function(value,row,index){
                    if (row.signFlag){
                        return '是';
                    } else {
                        return '否';
                    }
                }},
        ]],
        onSelectAll:function(){
            checkAllFlag = true;
        },
        onUnselectAll:function(){
            checkAllFlag = false;
        }
    });

    function onSelOk(){
        var form = $('#form-message-select');
        if (!form.form('validate')){
            return;
        }
        var rows = $("#dg-messages-select").datagrid('getSelections');
        if (rows == null || rows.length < 1){
            showMsg("提示", "请选择报文", true);
            return;
        }
        $.messager.progress({
            title : '添加报文',
            msg : '报文添加中，请不要关闭页面。'
        });
        if (checkAllFlag == false){
            var ids = new Array();
            if(rows && rows.length>0){
                for(var i= 0;i<rows.length;i++){
                    ids.push(rows[i].id);
                }
            }
            var simid=$("#simname").combobox('getValue');
            $.post("${ctx}/sim/instance/message/addInstanceMessage", {simid:simid, ids: ids}, function (data) {
                $.messager.progress('close');
                if (data.success) {
                    onSelCancel();
                } else {
                    showMsg("提示",data.msg,true);
                }
            });
        }else {
            var name = $("#searchmsgname").textbox("getValue");
            var trsCode = $("#msgtrsCode").textbox("getValue");
            var simInsId=$("#simname").combobox('getValue');
            var simSysId=$("#simsys").combobox("getValue");

            var p = {};
            if (name != null && name != "") {
                p['name|like'] = name;
            }
            if (trsCode != null && trsCode != "") {
                p['trsCode|like'] = trsCode;
            }
            $.post("${ctx}/sim/instance/message/addAllInstanceMessage", {simInsId:simInsId, simSysId:simSysId,name:name,mesgType:trsCode}, function (data) {
                $.messager.progress('close');
                if (data.success) {
                    onSelCancel();
                } else {
                    showMsg("提示",data.msg,true);
                }
            });
        }

    }

    function onSelCancel(){
       closeWindow();
    }
</script>
