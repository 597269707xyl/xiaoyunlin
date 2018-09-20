<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<input type="hidden" id="simSysinsId" name="simSysinsId" />
<div style="height: 300px">
<table id="dg-param-select"></table>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onSetOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onSetCancel();">取消</a>
</div>

<script>
    function setData(param){
        $('#simSysinsId').val(param.id);
        $("#dg-param-select").datagrid({
            fit:true,
            title: '域列表',
            fitColumns: true,
            url:'${ctx}/sim/instance/dict/SIM_INS_CONF/'+param.id,
            height: 300,
            singleSelect:true,
            pagination: false,
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: '实例编码', width: 100},
                {field: 'text', title: '实例名称', width: 100},
            ]]
        });
    }

    function onSetCancel(){
        $('#window-set-param').window('close');
    }
    function onSetOk(){
        var rows = $("#dg-param-select").datagrid('getSelections');
        if(rows.length == 0) {
            showMsg('提示','请选择实例参数',true);
            return;
        }
        var simSysinsId = $("#simSysinsId").val();
        $('#window-paramset').window({
            title: '实例参数值',
            width: 400,
            height: 200,
            href: '${ctx}/sim/instance/paramSet',
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
                param.adapterId = simSysinsId;
                param.paramKey = rows[0].id;
                param.paramKeyName = rows[0].text;
                setParamData(param)
            },
            onClose: function () {
                $("#dg-param-list").datagrid('reload');
            }
        });
        onSetCancel();
    }

</script>
