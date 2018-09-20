<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="height: 300px">
<table id="dg-param-list"></table>
</div>
<script>
    function setData(param){
        $("#dg-param-list").datagrid({
            fit:true,
            title: '适配器参数列表',
            fitColumns: true,
            url:'${ctx}/sys/adapter/paramListData/'+param.id,
            height: 300,
            singleSelect:true,
            pagination: false,
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'id', title: 'id', hidden: true},
                {field: 'adapterId', title: 'simSysinsId', hidden: true},
                {field: 'paramKey', title: '参数编码', width: 100},
                {field: 'paramKeyName', title: '参数名称', width: 100},
                {field: 'paramValue', title: '参数值', width: 100}
            ]],
            toolbar: [{
                text:'添加',
                iconCls: 'icon-add',
                handler: function(){
                    $('#window-set-param').window({
                        title: '选择适配器参数',
                        width: 480,
                        height: 420,
                        href: '${ctx}/sim/instance/paramSelect/',
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
                            setData(param);
                        },
                        onClose: function () {
                            $("#dg-param-list").datagrid('reload');
                        }
                    });
                }
            },'-',{
                text:'修改',
                iconCls: 'icon-edit',
                handler: function(){
                    var rows = $("#dg-param-list").datagrid('getSelections');
                    if(rows.length != 1){
                        showMsg("提示","请选择一条记录",true);
                        return;
                    }
                    //var state = operation(param.id);
                    $('#window-paramset').window({
                        title: '修改适配器参数',
                        width: 400,
                        height: 200,
                        href: '${ctx}/sim/instance/paramSet/',
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
//                            setData(param)
                            setParamData(rows[0]);
                        },
                        onClose: function () {
                            $("#dg-param-list").datagrid('reload');
                        }
                    });
                }
            },'-',{
                text:'删除',
                iconCls: 'icon-remove',
                handler: function(){
                    var state = operation(param.id);
                    var rows = $("#dg-param-list").datagrid('getSelections');
                    var ids = new Array();
                    if(rows && rows.length>0){
                        for(var i= 0;i<rows.length;i++){
                            ids.push(rows[i].id);
                        }
                    }else {
                        showMsg("提示","请选择要删除的记录",true);
                        return;
                    }
                    $.post("${ctx}/sys/adapter/deleteParam", {ids: ids}, function (data) {
                        if (data.success) {
                            $("#dg-param-list").datagrid('reload');
                        } else {
                            showMsg("提示",data.msg,true);
                        }
                    });
                }
            }]
        });
    }

    function operation(id){
        var state = "";
        $.ajax({
            url: "${ctx}/sim/instance/get/" + id,
            cache: false,
            async: false,
            success: function (vo) {
                state = vo.state;
            }
        });
        return state;
    }

    function onSetCancel(){
        closeWindow();
    }
</script>
