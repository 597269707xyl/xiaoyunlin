<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/3/3
  Time: 10:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post">
    <div class="popup">
        <input type="hidden" id="realId" name="id"/>
        <table class="single">
            <tr>
                <td>key:</td>
                <td><input class="easyui-textbox" id="key" name="key"
                           data-options="required:true"/></td>
            </tr>
            <tr>
                <td>value:</td>
                <td><input class="easyui-textbox" id="value" name="value"
                           data-options="required:true"/></td>
            </tr>
        </table>
    </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="submitForm();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="closeCode();">取消</a>
    </div>

</form>

<script>
    function getData() {
        var formData = {};
        formData.id = $('#realId').val();
        formData.msgFieldId = ${msgFieldId};
        formData.key = $('#key').textbox('getValue');
        formData.value = $('#value').textbox('getValue');
        return formData;
    }
    function setData() {
        var flag = '${type}';
        if (flag == 'edit') {
            $.ajax({
                url: '${ctx}/msg/field/getCurrentParam/?codeId=' + ${codeId},
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
                }
            });
        }
    }
    function submitForm() {
        $.ajax({
            url: '${ctx}/msg/field/addCode/',
            type: 'post',
            data: getData.call(),
            cache: false,
            success: function () {
                $('#fm').form('load');
                $("#enumTable").datagrid('reload');
                closeCode();
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.responseText);
                closeCode();
            }
        });
    }
    function closeCode() {
        $('#windowCode').window('close');
    }
</script>
