<%--
  Created by IntelliJ IDEA.
  User: huangbo
  Date: 2016/5/24
  Time: 17:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" class="easyui-form" method="post">
    <div class="popup">
        <table>
            <tr>
                <td>文件:</td>
                <td><input id="uploadFileBox" class="easyui-filebox" name="file"
                           style="width:100%" buttonText="选择文件"></td>

            </tr>
        </table>
    </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="uploadFile();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="closeWindow();">取消</a>
    </div>
</form>
<script>
    function uploadFile() {
        $("#fm").ajaxSubmit({
            type: 'post',
            url: '${ctx}/msg/schema/upload',
            success: function (data) {
                if (data.success) {
                    closeWindow();
                    $("#dg").datagrid('reload');
                } else {
                    tipError("操作失败!");
                }
            }
        });
    };
</script>
