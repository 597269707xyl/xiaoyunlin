<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post">

    <div class="popup">
        <input type="hidden" id="id" name="id"/>
        <table class="single">
            <tr>
                <td>分配人员:</td>
                <td><input class="easyui-combobox" id="name" name="name" style="width: 150px"
                           valueField="id" textField="name" limitToList="true" url="${ctx}/test/task/getAssignMember" panelHeight="150"
                           required="true"/>
                </td>
            </tr>
        </table>
    </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="submitForm();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="closeWindow();">取消</a>
    </div>

</form>

<script>
    function setData(params){
        $("#id").val(params.id);
    }
    function submitForm() {
        <%--commonSubmitForm('${ctx}/test/task/addAssignMember');--%>
        closeWindow();
    }
</script>
