<%--
  Created by IntelliJ IDEA.
  User: huangbo
  Date: 2018/8/1
  Time: 13:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/asset/js/jquery.form.min.js"></script>
<form id="fm" method="post" action="#" enctype="multipart/form-data">
    <input type="hidden" id="usecaseId" name="usecaseId"/>
    <div class="popup">
        <table class="single">
            <tr>
                <td>缺陷标识:</td>
                <td><input class="easyui-textbox" type="text" name="abbreviation" id="abbreviation" style="width: 150px"/></td>
            </tr>
            <tr>
                <td>缺陷等级:</td>
                <td><input id="grade" name="grade" class="easyui-combobox" editable="false"
                           data-options="valueField: 'value',textField: 'text',panelHeight:'auto',
                            icons:[{iconCls:'icon-clear',handler: function(e){$(e.data.target).combobox('clear');}}],
                            data: [{value: 'H',text: '高'},{value: 'M',text: '中'},{value: 'L',text: '低'}]" required>
                    </input>
                </td>
            </tr>
            <tr>
                <td>缺陷描述:</td>
                <td><input class="easyui-textbox"  data-options="multiline:true"
                           id="descript" name="descript"
                           style="height:40px ;"/></td>
            </tr>
        </table>
    </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onUploadCancel();">取消</a>
    </div>
</form>

<script>
    function setData(param) {
        $('#usecaseId').val(param.usecaseId);
    }
    function onOk() {
        commonSubmitForm('${ctx}/defect/addImp');
    }
    function onUploadCancel() {
        closeWindow();
    }
</script>

