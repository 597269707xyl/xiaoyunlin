<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="width:100%;height: 93%;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north',split:true" style="height:120px;">
            <div class="easyui-layout" data-options="fit:true">
                <%--<div data-options="region:'west',title:'发送报文列表',split:true" style="width:40%">--%>
                <div data-options="region:'west',split:true" style="width:40%">
                    <ul id="execTree">
                    </ul>
                </div>
                <div data-options="region:'center'">
                    <label style="font-family: Verdana;">说明：</label><a id="resultDescript"></a>
                    <%--<div style="width: 100%;height: 99%">--%>
                        <%--<div class="easyui-layout" data-options="fit:true">--%>
                            <%--<div data-options="region:'west',split:true,title:'发送报文',collapsible:false" style="width:50%;,height:80%">--%>
                                <%--<table id="dg-detail-send"></table>--%>
                            <%--</div>--%>
                            <%--<div data-options="region:'center',title:'接收报文'">--%>
                                <%--<table id="dg-detail-receive"></table>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                </div>
            </div>
        </div>
        <div data-options="region:'center'">
            <%--<label style="font-family: Verdana;">说明：</label><a id="resultDescript"></a>--%>
            <div style="width: 100%;height: 99%">
                <div class="easyui-layout" data-options="fit:true">
                    <div data-options="region:'west',split:true,title:'发送报文',collapsible:false" style="width:50%;,height:80%">
                        <table id="dg-detail-send"></table>
                    </div>
                    <div data-options="region:'center',title:'接收报文'">
                        <table id="dg-detail-receive"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
       onclick="onExecDataCloseOk();">关闭</a>
</div>
<script>
    function setData(execId){
        $('#execTree').datagrid({
            fit:true,
            url: '${ctx}/func/usecase/tree?id=' + execId,
            height: 530,
            fitColumns: true,
            singleSelect:true,
            onSelect: function(index, row){
                $('#dg-detail-receive').treegrid('loadData', new Array());
                if(row && row.id){
                    $('#dg-detail-send').treegrid({url: '${ctx}/func/usecase/sendMsg?id=' + row.id});
                    $('#dg-detail-receive').treegrid({url: '${ctx}/func/usecase/recvMsg?id=' + row.id});
                    $("#resultDescript").html(row.resultDescript);
                }
            },
            columns: [[
                {field: 'id', title: 'id', hidden: true},
                {field: 'messageName', title: '报文名称', width: 150},
                {field: 'resultDis', title: '执行结果', width: 100}
            ]],
            onLoadSuccess: function(data){
                $('#execTree').datagrid('selectRow', 0);
            }
        });
    }
    $('#dg-detail-send').treegrid({
        fit:true,
        height: 555,
        fitColumns:true,
        idField:'id',
        treeField:'name',
        columns: [[
            {field: 'name', title: '域id', width: 20},
            {field: 'nameZh', title: '域中文名称', width: 100},
            {field: 'value', title: '发送值', width: 150}
        ]]
    });
    $('#dg-detail-receive').treegrid({
        fit:true,
        height: 555,
        fitColumns:true,
        idField:'id',
        treeField:'name',
        columns: [[
            {field: 'name', title: '域id', width: 20},
            {field: 'nameZh', title: '域中文名称', width: 80},
            {field: 'value', title: '接收值', width: 150}
        ]]
    });
    function onExecDataCloseOk(){
        $('#window-exec-list').window('close');
    }
</script>