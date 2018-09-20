<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div>
    <table>
        <tr>
            <td><a href="javascript:showmsgdatafromdata()">添加测试数据</a>
                <input type="hidden" id="usecaseDataList" class="easyui-textbox form-input"/>
            </td>
        </tr>
        <tr>
            <td><a href="javascript:showexpecteddatafromdata()">添加结果值</a>
                <input type="hidden" id="expectedDataList" class="easyui-textbox form-input"/>
            </td>
        </tr>
        <%--<tr>--%>
            <%--<td><a href="javascript:showreplydatafromdata()">添加业务回复值</a>--%>
                <%--<input type="hidden" id="replyDataList" class="easyui-textbox form-input"/>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <tr>
            <td><a href="javascript:showruledatafromdata()">添加取值规则</a>
                <input type="hidden" id="ruleDataList" class="easyui-textbox form-input"/>
            </td>
        </tr>
    </table>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onUsecaseDataOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onUsecaseDataCancel();">取消</a>
</div>
<script>
    var params={};
    function setData(param){
        params = param;
        var rows = $('#dg-usecase-data-list').datagrid('getRows');
    }
    function showmsgdatafromdata(){
        var usecaseDataList = $('#usecaseDataList').textbox('getValue');
        params.source = 'data';
        params.usecaseDataList = usecaseDataList;
        $('#window-usecase-data-detail').window({
            title: '测试数据',
            width: 600,
            height:400,
            href: '${ctx}/func/mark/msgPage',
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
                setData(params);
            }
        });
    }
    function showexpecteddatafromdata(){
        var expectedDataList = $('#expectedDataList').textbox('getValue');
        params.type = 'expect';
        params.source = 'data';
        params.expectedDataList = expectedDataList;
        $('#window-expected-list').window({
            title: "结果值",
            width: 530,
            height: 460,
            href: '${ctx}/func/mark/expected',
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
                setMyData(params)
            },
            onClose: function () {
            }
        });
    }
    function showreplydatafromdata(){
        var replyDataList = $('#replyDataList').textbox('getValue');
        params.type = 'next';
        params.source = 'data';
        params.replyDataList = replyDataList;
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
                setMyData(params)
            },
            onClose: function () {
            }
        });
    }
    function showruledatafromdata(){
        var ruleDataList = $('#ruleDataList').textbox('getValue');
        params.source = 'data';
        params.ruleDataList = ruleDataList;
        var usecaseDataList = $("#dg-usecase-data-list").datagrid('getRows');
        for(var i=0; i<usecaseDataList.length; i++){
            if(typeof (usecaseDataList[i].messageId) == "undefined"){
                usecaseDataList[i].messageId = usecaseDataList[i].simMessage.id;
            }
            usecaseDataList[i].index = i+1;
        }
        params.arrData = usecaseDataList;
        $('#window-rule-list').window({
            title: "取值规则列表",
            width: 530,
            height: 460,
            href: '${ctx}/func/mark/ruleListPage',
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
                setMyData(params)
            },
            onClose: function () {
            }
        });
    }
    function onUsecaseDataOk(){
        var usecaseDataList = $('#usecaseDataList').textbox('getValue');
        var expectedDataList = $('#expectedDataList').textbox('getValue');
        var ruleDataList = $('#ruleDataList').textbox('getValue');
        if(usecaseDataList == '' || usecaseDataList == '[]'){
            showMsg("提示", "请添加测试数据!", true);
            return;
        }
//        if(expectedDataList == '' || expectedDataList == '[]'){
//            showMsg("提示", "请添加预期值!", true);
//            return;
//        }
        params.ruleDataList = ruleDataList;
        params.usecaseDataList = usecaseDataList;
        params.expectedDataList = expectedDataList;
        var usecaseId = params.usecaseId;
        if(typeof (usecaseId) == 'undefined' || usecaseId == '' || usecaseId == 0){
            var rows = $('#dg-usecase-data-list').datagrid('getRows');
            rows.push(params);
            $('#dg-usecase-data-list').datagrid('loadData', rows);
            onUsecaseDataCancel();
        } else {
            $.messager.progress();
            $.ajax({
                url: '${ctx}/func/exec/saveUsecaseData',
                type: 'POST',
                data: params,
                success: function(data){
                    $.messager.progress('close');
                    if(data.success){
                        $('#dg-usecase-data-list').datagrid('reload');
                        onUsecaseDataCancel();
                    } else {
                        showMsg('提示', '添加失败!', true);
                    }
                }
            })
        }
    }
    function onUsecaseDataCancel(){
        $('#window-usecase-data').window('close');
    }
</script>
