<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>密码修改</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
</head>
<body>
<form id="fm" class="easyui-form" method="post">
    <input type="hidden" id="id" name="id" value="<shiro:principal property="id"></shiro:principal>"/>
    <table>
        <tr>
            <td>原密码:</td>
            <td><input name="oldPwd" class="easyui-textbox" type="password" data-options="required:true" style="width: 250px"/></td>
        </tr>
        <tr>
            <td>新密码:</td>
            <td><input id="newPwd" name="newPwd" class="easyui-textbox" type="password" data-options="required:true,validType:'length[8,20]'" style="width: 250px"/></td>
        </tr>
        <tr>
            <td>确认密码:</td>
            <td><input id="confirmPwd" name="confirmPwd" class="easyui-textbox" type="password" required="required" validType="equals['#newPwd']" style="width: 250px"/></td>
        </tr>
    </table>
    <div>
        <div style="padding:10px;">
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
               onclick="onOk();">确定</a>
        </div>
    </div>
</form>
<script>
    $.extend($.fn.validatebox.defaults.rules, {
        equals: {
            validator: function (value, param) {
                return value == $(param[0]).val();
            },
            message: '两次密码输入不一致，请检查'
        }
    });

    function onOk(){
        var form = $('#fm');
        var flag = form.form('validate');
        if (!flag) return;

        var data = form.serialize();
        $.post("${ctx}/sys/user/updatePassword",data,function(data){
            if(data.success){
                showMsg("提示","密码更新成功!",true);
            }else{
                showMsg("提示","密码更新失败!",true);
            }
        });
    }
</script>
</body>
</html>
