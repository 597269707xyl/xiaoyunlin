<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>添加用例</title>
</head>
<body>
<form id="usecaseForm">
    <input type="hidden" id="id" name="id" class="easyui-textbox form-input"/>
    <table>
        <tr>
            <td class="form-label">案例编号：</td>
            <td>
                <input id="caseNumberId" name="caseNumber" class="easyui-textbox form-input" style="width: 250px;" required/>
            </td>
        </tr>
        <tr>
            <td class="form-label">序号：</td>
            <td>
                <input id="seqNo" name="seqNo" type="text" class="easyui-numberbox" value="0" style="width: 250px;" data-options="min:0" required/>
            </td>
        </tr>
        <tr>
            <td class="form-label">用例目的：</td>
            <td>
                <input id="purposeId" name="purpose" class="easyui-textbox form-input" data-options="multiline:true" style="width: 250px;height: 100px;" required/>
            </td>
        </tr>
        <tr>
            <td class="form-label">用例步骤：</td>
            <td>
                <input id="stepId" name="step" class="easyui-textbox form-input" data-options="multiline:true" style="width: 250px;height: 100px;" required/>
            </td>
        </tr>
        <c:if test="${toolType=='nxyw'}">
            <tr>
                <td class="form-label">预期结果：</td>
                <td>
                    <input id="expected" name="expected" class="easyui-textbox form-input" data-options="multiline:true" style="width: 250px;height: 100px;"/>
                </td>
            </tr>
        </c:if>
        <c:if test="${toolType=='atsp'}">
            <input id="expected" name="expected" type="hidden" class="easyui-textbox form-input"/>
        </c:if>
        <tr id="updateUsecaseData">
            <td colspan="2"><a href="javascript:showUsecaseListData('${itemId}');">添加用例数据</a> </td>
            <input type="hidden" id="usecaseAllDataList" class="easyui-textbox form-input"/>
        </tr>
    </table>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
           onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onCancel();">取消</a>
    </div>
</form>
<script>
    //编辑
    function setData(param){
        document.getElementById("updateUsecaseData").style.display="none"
        $.ajax({
            url: '${ctx}/func/exec/getUsecase?id=' + param.id,
            success: function(vo){
                $("#usecaseForm").form('load', vo);
            }
        })
    }
    //添加用例数据
    function showUsecaseListData(itemId){
        var usecaseAllDataList = $('#usecaseAllDataList').textbox('getValue');
        var usecaseId = $('#id').textbox('getValue');
        $('#window-usecase-data-list').window({
            title: '添加用例数据',
            width: 650,
            height:450,
            href: '${ctx}/func/mark/usecaseDataListPage',
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
                setData(usecaseId, itemId);
            }
        });
    }
    function getData(){
        var formData = {};
        formData.id = $("#id").textbox('getValue');
        formData.purpose = $("#purposeId").textbox('getValue');
        formData.step = $("#stepId").textbox('getValue');
        formData.caseNumber = $("#caseNumberId").textbox('getValue');
        formData.seqNo = $("#seqNo").textbox('getValue');
        formData.expected = $("#expected").textbox('getValue');
        return formData;
    }
    function onOk() {
        var form = $('#usecaseForm');
        var flag = form.form('validate');
        if (!flag) return;
        var formData = getData();
        var usecaseAllDataList = $('#usecaseAllDataList').textbox('getValue');
        formData.usecaseAllDataList = usecaseAllDataList;
        if(usecaseAllDataList == '' && form.id == ''){
            showMsg("提示", "请添加测试数据", true);
            return;
        }
        $.messager.progress();
        $.ajax({
            url: '${ctx}/func/exec/addUsecase?itemId=${itemId}',
            type: 'POST',
            data: formData,
            success: function(data){
                $.messager.progress('close');
                if(data.success){
                    showMsg("提示", "添加测试用例成功", true);
                    $('#usecaseList').datagrid('reload');
                    onCancel();
                } else {
                    showMsg("提示", "添加测试用例失败", true);
                }
            }
        })
    }
    function onCancel() {
        closeWindow();
    }
</script>
</body>
</html>