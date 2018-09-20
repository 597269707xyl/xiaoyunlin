<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="param-set">
    <div class="popup">
        <table align="center">
            <tr>
                <td>
                    参数编码：<input id="paramKey" type="text" class="easyui-textbox" name="paramKey" readonly="readonly"/>
                </td>
            </tr>
            <tr>
                <td>
                    参数名：<input id="paramKeyName" type="text" class="easyui-textbox" name="paramKeyName"
                               readonly="readonly"/>
                </td>
            </tr>
            <tr>
                <td id="set-param">
                    参数值：<input id="paramValue" type="text" class="easyui-textbox" name="paramValue"/>
                </td>
            </tr>
        </table>
    </div>
    <input id="paramId" type="hidden" name="id"/>
    <input id="simSysinsId" type="hidden" name="adapterId"/>
</form>

<div class="buttonBar">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
       onclick="onParamOk();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
       onclick="onParamCancel();">取消</a>
</div>

<script>
    function setParamData(param) {
        param['paramId'] = param.id;
        if (param.paramKey == 'PASSWORD_FIELD_ONOFF' || param.paramKey == 'FE_MAC_ONOFF') {
            var html = '参数值：<select id="paramValue" name="paramValue" class="easyui-combobox">' +
                    '<option value="ON">ON</option>' +
                    '<option value="OFF">OFF</option>' +
                    '</select> ';
            $('#set-param').html(html);
        } else {
            var html = '参数值：<input id="paramValue" type="text" class="easyui-textbox" name="paramValue" />';
            //$('#set-param').html(html);
        }
        $('#param-set').form('load', param);
    }

    function onParamOk() {
        var id = $('#paramId').val();
        var simSysinsId = $('#simSysinsId').val();
        var paramKey = $('#paramKey').val();
        var paramKeyName = $('#paramKeyName').val();
        var paramValue = $('#paramValue').val();
        if (endWith(paramKey, "_IP")) {
            var re = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
            if (!re.test(paramValue)) {
                showMsg("提示", "请正确输入IP地址", true);
                return;
            }
        }
        if (endWith(paramKey, "_PORT")) {
            var re = /^[0-9]+$/;
            if (!re.test(paramValue)) {
                showMsg("提示", "端口请输入正整数", true);
                return;
            }
        }
        if (paramValue == '') {
            showMsg("提示", "请输入实例参数值", true);
            return;
        }
        $.post("${ctx}/sys/adapter/saveParam", {
            id: id,
            adapterId: simSysinsId,
            paramKey: paramKey,
            paramKeyName: paramKeyName,
            paramValue: paramValue
        }, function (data) {
            if (data.success) {
                onParamCancel();
//                closeWindow();
                $("#dg-param-list").datagrid('reload');
            } else {
                showMsg("提示", "操作失败", true);
            }
        });
    }
    function onParamCancel() {
        $('#window-paramset').window('close');
    }
    function endWith(str, endStr) {
        var d = str.length - endStr.length;
        return (d >= 0 && str.lastIndexOf(endStr) == d)
    }

</script>
