<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2018/7/11
  Time: 10:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <meta charset="UTF-8">
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
</head>
<body>
<div style="margin:20px 0;"></div>
<div class="popup" style="margin-bottom:20px">
    <table>
        <tr>
            <td>测试项:</td>
            <td>
                <input class="easyui-combobox" id="itemId" name="itemId" style="width: 150px"
                       valueField="id" textField="name" limitToList="true" url="${ctx}/func/config/getAll" panelHeight="150"
                       required="true"/>
            </td>
        </tr>
        <tr>
            <td>文档名称:</td>
            <td><input class="easyui-textbox" type="text" name="variableZh" style="width: 150px"/></td>
        </tr>
        <tr>
            <td class="popup_label">文档路径:</td>
            <td><input class="easyui-filebox" name="file1" data-options="buttonText:'选择',prompt:'选择文件...'"
                   style="width:150px"></td>
        </tr>
        <tr>
            <td>文档描述:</td>
            <td><input class="easyui-textbox" id="adapterStatus" name="adapterStatus" data-options="multiline:true" id="descript" name="descript"
                       style="height:60px ;width:150px"/></td>
        </tr>
    </table>
</div>
<div class="buttonBar">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
       onclick="submitForm();">上传</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
       onclick="closeWindow();">取消</a>
</div>
</div>
</body>
</html>
