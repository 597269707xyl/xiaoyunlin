<%--
  Created by IntelliJ IDEA.
  User: huangbo
  Date: 2016/5/18
  Time: 16:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" class="easyui-form" method="post"><div class="popup">
    <table class="single">
<%--        <tr>
            <td>id:</td>
            <td><input class="easyui-textbox" id="sd" name="sd" style="width:100%" value="${msgCharset.id}"/></td>
        </tr>--%>
        <tr>
            <td>字符集名称:</td>
            <td><input class="easyui-textbox" id="name1" name="name" style="width:100%" value="${msgCharset.name}" data-options="required:true"/></td>
        </tr>
        <tr>
            <td>字符集代号:</td>
            <td><input class="easyui-textbox" id="code" name="code" style="width:100%" value="${msgCharset.code}" data-options="required:true"/></td>
        </tr>
        <tr>
            <td>取值范围:</td>
            <td><input class="easyui-textbox" id="enumeration" name="enumeration" style="width:100%" data-options="required:true,validType: 'enumeration'" value="${enumeration}"/></td>
        </tr>
        <tr>
            <td>说明:</td>
            <td><input class="easyui-textbox" id="descript" name="descript" style="width:100%;height:26px" value="${msgCharset.descript}"></td>
        </tr>
    </table></div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="submitForm();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeWindow();">取消</a>
    </div>
</form>
<script>
    function getData(){
        var formData = {};
        formData.id = "${msgCharset.id}";
        formData.name = $('#name1').textbox('getValue');
        formData.code = $('#code').textbox('getValue');
        formData.descript = $('#descript').textbox('getValue');
        formData.enumeration = $('#enumeration').textbox('getText');

        formData.basicName = "${msgCharset.name}";
        formData.basicCode = "${msgCharset.code}";
        return formData;
    }
    function submitForm(){
        commonSubmitForm('${ctx}/msg/charset/edit');
    };
    $.extend($.fn.validatebox.defaults.rules, {
        enumeration: {
            validator : function(value) {
                return /^\S{1}(\,[^,])*$/i.test(value);
            },
            message : '格式不正确，请输入以英文逗号间隔的不重复单个字符，并且不以逗号结尾'
        }
    });
</script>
