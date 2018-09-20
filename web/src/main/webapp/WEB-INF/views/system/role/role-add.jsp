<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post">
    <div class="popup">
        <input type="hidden" id="id" name="id"/>
        <%--        <div class="easyui-panel panel-body" title="" style="padding: 30px 60px; width: 400px;">
                    <div style="margin-bottom:20px">
                        <label class="label-top">名称:</label>
                        <input class="easyui-textbox"
                               style="height: 32px; display: none;">
                    </div>
                    <div style="margin-bottom:20px">
                        <label class="label-top">标识:</label>
                        <input class="easyui-textbox" style="height: 32px; display: none;">
                    </div>
                    <div style="margin-bottom:20px">
                        <label class="label-top">描述:</label>
                        <input class="easyui-textbox" style="height: 32px; display: none;">
                    </div>
                    <div>
                        <a href="#" class="easyui-linkbutton l-btn l-btn-small easyui-fluid" iconcls="icon-ok"
                           style="width: 278px; height: 32px;" group="" id=""></a>
                    </div>
                </div>--%>
        <table class="single">
            <tr>
                <td><label class="popup_label">名称:</label>
                <td><input class="easyui-textbox popup_input" id="name" name="name" data-options="required:true"
                           width="100%"/></td>
            </tr>
            <tr>
                <td class="popup_label">标识:</td>
                <td><input class="easyui-textbox popup_input" id="role" name="role" data-options="required:true"/></td>
            </tr>
            <tr>
                <td class="popup_label">描述:</td>
                <td><input class="easyui-textbox popup_input" id="notes" name="notes" data-options="multiline:true"
                           style="height:60px;"/></td>
            </tr>
        </table>
        <div class="buttonBar">
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
               onclick="onOk();">确定</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
               onclick="onCancel();">取消</a>
        </div>
    </div>
</form>
<script>
    function setData(param) {
        if (param.action == 'update') {
            $.ajax({
                url: "${ctx}/sys/role/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
                }
            });
        }
    }
    function getData() {
        var data = {};
        data.name = $("#name").val();
        data.role = $("#role").val();
        data.notes = $("#notes").val();
        return data;
    }
    function onOk() {
        commonSubmitForm('${ctx}/sys/role/add');
    }
    function onCancel() {
        closeWindow();
    }
</script>
