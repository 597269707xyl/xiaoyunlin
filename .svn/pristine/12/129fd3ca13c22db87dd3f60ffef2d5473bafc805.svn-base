<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="fm" class="easyui-form" method="post">
    <input type="hidden" id="id" name="id" value="${user.id}"/>
    <div class="popup">
        <table>
            <tr>
                <td class="popup_label">账号:</td>
                <td><input class="easyui-textbox" id="sysName" name="sysName" value="${sysUser.sysName}"
                           data-options="required:true"
                /></td>
            </tr>
            <tr>
                <td class="popup_label">姓名:</td>
                <td><input class="easyui-textbox" id="name1" name="name" value="${user.name}"
                           data-options="required:true"/>
                </td>
            </tr>
            <tr>
                <td class="popup_label">角色:</td>
                <td><input id="roleIds" name="roleIds" value="${roleIds}" data-options="required:true"/></td>
            </tr>
            <tr>
                <td class="popup_label">密码:</td>
                <td><input class="easyui-textbox" type="password" id="passWd" name="passWd" value="${sysUser.passWd}"
                           data-options="required:true"
                /></td>
            </tr>
            <tr>
                <td class="popup_label">确认密码:</td>
                <td><input class="easyui-textbox" type="password" id="checkPsw" name="checkPsw" required="required" value="${sysUser.passWd}"
                           validType="equals['#passWd']"/></td>
            </tr>
        </table>

    </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
           onclick="submitForm();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="closeWindow();">取消</a>
    </div>
</form>
<script>
    $("#roleIds").combobox({
        url: '${ctx}/sys/role/all',
        valueField: 'id',
        textField: 'name',
        panelHeight: 'auto',
        multiple: true,
        editable: false
    });
    $.extend($.fn.validatebox.defaults.rules, {
        equals: {
            validator: function (value, param) {
                return value == $(param[0]).val();
            },
            message: '两次密码输入不一致，请检查'
        }
    });

    function submitForm() {
        commonSubmitForm('${ctx}/sys/user/add');
    }
</script>
