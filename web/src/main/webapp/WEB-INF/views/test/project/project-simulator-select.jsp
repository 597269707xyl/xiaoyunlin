<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="simQueryForm">
    <input type="hidden" id="projectId" name="projectId"/>
    <table>
        <tr>
            <td>仿真实例:</td>
            <td>
                <input class="easyui-textbox" type="text" id="searchSimname" name="searchSimname" style="width: 100px"
                       onenter="onSearch"/>
            </td>
            <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                   onclick="onMySearch();">查询</a></td>
        </tr>
    </table>
</form>
<div style="height: 300px">
<table id="dg-sim-select"></table>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onSetOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onSetCancel();">取消</a>
</div>

<script>
    function setData(param){
        $('#projectId').val(param.id);
        $("#dg-sim-select").datagrid({
            fit:true,
            title: '仿真实例列表',
            fitColumns: true,
            url:'${ctx}/proj/proj/getNoUsedSimulators?projectId='+param.id,
            height: 300,
            singleSelect:false,
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {field: 'name', title: '实例名称', width: 150},
                {field: 'systemName', title: '所属仿真系统', width: 150,formatter:function(value,row,index){
                    if (row.simSystem.name){
                        return row.simSystem.name;
                    }
                    return '';
                }}
            ]]
        });

    }

    function onMySearch(){
        var name = $("#searchSimname").textbox("getValue");
        var p = {};
        if (name != null && name != "") {
            p['name'] = name;
        }

        $("#dg-sim-select").datagrid('load', p);
    }

    function onSetCancel(){
        $('#window-sim-list').window('close');
    }
    function onSetOk(){
        var rows = $("#dg-sim-select").datagrid('getSelections');
        var ids = new Array();
        if(rows && rows.length>0){
            for(var i= 0;i<rows.length;i++){
                ids.push(rows[i].id);
            }
        }else {
            showMsg('提示','请选择报文',true);
            return;
        }
        var projectId = $("#projectId").val();
        $.post("${ctx}/proj/proj/addSimulators", {projectId: projectId, ids: ids}, function (data) {
            if (data.success) {
                onSetCancel();
            } else {
                showMsg("提示",data.msg,true);
            }
        });

    }

</script>
