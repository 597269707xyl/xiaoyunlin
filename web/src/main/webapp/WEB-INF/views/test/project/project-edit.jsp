<%--
  Created by IntelliJ IDEA.
  User: huangbo
  Date: 2016/5/9
  Time: 10:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="fm" class="easyui-form" method="post">
    <div class="popup">
        <input type="hidden" id="id" name="id" value="${testProject.id}"/>
        <table class="single">
            <tr>
                <td>项目名称:</td>
                <td><input class="easyui-textbox" id="name1" name="name" data-options="required:true"
                           value="${testProject.name}"/></td>
            </tr>
            <tr>
                <td>项目标识:</td>
                <td><input class="easyui-textbox" id="abb" name="abb"
                           data-options="required:true" value="${testProject.abb}"/></td>
            </tr>
            <tr>
                <td>被测系统:</td>
                <td><input class="easyui-textbox" id="testsystem" name="testsystem" data-options="required:true"
                           value="${testProject.testsystem}"/></td>
            </tr>
            <tr>
                <td>依赖实例:</td>
                <td><input id="insname" name="insname" data-options="required:true" value="${insname}"/></td>
            </tr>
            <tr>
                <td>预计截止日期:</td>
                <td><input class="easyui-datetimebox" id="endtime" name="endtime" value="${testProject.endtime}"></td>
            </tr>

            <tr>
                <td>说明:</td>
                <td><input class="easyui-textbox" id="descript" name="descript" value="${testProject.descript}"/></td>
            </tr>
        </table>
        <div class="buttonBar">
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
               onclick="submitForm();">确定</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
               onclick="closeWindow();">取消</a>
        </div>
    </div>
</form>
<script>
    $("#insname").combobox({
        url: '${ctx}/proj/proj/insEdit/' + '${testProject.id}',
        valueField: 'id',
        textField: 'name',
        panelHeight: 'auto',
        multiple: true,
        limitToList:true
    });
    function getData() {
        var formData = {};
        formData.id = "${testProject.id}";
        formData.rawIns = "${insname}";
        formData.newIns = $('#insname').combobox('getValue');
        formData.tempTime = $('#endtime').datebox('getText');
        formData.name = $('#name1').textbox('getValue');
        formData.testsystem = $('#testsystem').textbox('getValue');
        formData.descript = $('#descript').textbox('getValue');
        formData.insname = $('#insname').combobox('getText');
        formData.abb = $('#abb').textbox('getValue');

        return formData;
    }
    function submitForm() {
        commonSubmitForm('${ctx}/proj/proj/edit');
    }
    /*
     function onSelOk(){
     var formData;
     formData = getData().call();
     var rows = $("#insname").combobox('getSelections');
     var ids = new Array();
     if(rows && rows.length>0){
     for(var i= 0;i<rows.length;i++){
     ids.push(rows[i].id);
     }
     }
     var projectSetId = "${projectSetId}";
     $.post("${ctx}/proj/proj/addMembers", {projectSetId: projectSetId, ids: ids}, function (data) {
     if (data.success) {
     onSelCancel();
     } else {
     showMsg("提示","操作失败",true);
     }
     });
     }*/
</script>
