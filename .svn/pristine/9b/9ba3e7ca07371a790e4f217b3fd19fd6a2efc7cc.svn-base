<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" class="easyui-form" method="post">
    <div class="popup">
        <table>
            <tr>
                <td class="popup_label">行号:</td>
                <td class="popup_input"><input class="easyui-textbox" id="bankNo" name="bankNo" data-options="required:true,validType:'email'"
                /></td>
            </tr>
            <tr>
                <td class="popup_label">行名:</td>
                <td><input class="easyui-textbox" id="bankName" name="bankName"/></td>

            </tr>
            <tr>
                <td class="popup_label">队列信息:</td>
                <td><input id="queue" name="queue"/></td>
            </tr>
            <tr>
                <td class="popup_label">DN值:</td>
                <td><input class="easyui-textbox" id="DN" name="DN"
                /></td>
            </tr>
            <tr>
                <td class="popup_label">实例参数:</td>
                <td><input class="easyui-textbox" id="param" name="param"
                /></td>
            </tr>
            <tr>
                <td class="popup_label">是否可作为发起行:</td>
                <td>
                    <input type="radio" name="leaf" id="leaf1" value="false">是</input>
                    <input type="radio" name="leaf" id="leaf2" value="true">否</input>
                </td>
            </tr>
            <tr>
                <td class="popup_label">是否自动应答:</td>
                <td>
                    <input type="radio" name="leaf" id="leaf3" value="false">是</input>
                    <input type="radio" name="leaf" id="leaf4" value="true">否</input>
                </td>
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
<script></script>