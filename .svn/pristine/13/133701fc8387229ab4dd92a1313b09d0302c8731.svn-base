<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/asset/js/jquery.form.min.js"></script>
<form method="post" action="#">
    <div style="height: 300px">
        <table id="instance-list"></table>
    </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onCloneDataOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onCloneDataCancel();">取消</a>
    </div>
</form>
<script>

    var type;
    var simId;
    function setCloneToInstance(id, systemId, types) {
        type = types;
        simId = id;
        $("#instance-list").datagrid({
            fitColumns: true,
            url:'${ctx}/generic/commonQuery/simSystemInstance?simSystem.id='+systemId,
            height: 300,
            singleSelect:false,
            pagination: true,
            pageSize: 10,
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {field: 'name', title: '实例名称', width: 100},
                {field: 'simSystem', title: '所属系统', width: 100,
                    formatter: function (value, row, index) {
                        return row.simSystem.name;
                    }
                }
            ]]
        });
    }
    function onCloneDataOk() {
        var instanceIds = new Array();
        var rows = $("#instance-list").datagrid('getSelections');
        if (rows && rows.length > 0) {
            for (var i = 0; i < rows.length; i++) {
                instanceIds.push(rows[i].id);
            }
        } else {
            showMsg("提示", "请选择一条记录", true);
        }
        $.messager.progress();
        $.ajax({
            url: '${ctx}/sim/message/cloneDataToInstance',
            type: 'post',
            data: {simId:simId,type:type,instanceIds:instanceIds},
            cache: false,
            async: false,
            success: function (text) {
                $.messager.progress('close');
                if(text.success){
                    closeWindow();
                    showMsg("提示", "同步数据成功", true);
                } else {
                    showMsg("提示", text.msg, true);
                }
            }
        });
    }
    function onCloneDataCancel() {
        closeWindow();
    }
</script>
