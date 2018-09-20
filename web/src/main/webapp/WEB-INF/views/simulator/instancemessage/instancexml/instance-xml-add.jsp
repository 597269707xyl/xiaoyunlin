<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/asset/js/jquery.form.min.js"></script>
<form id="fm" method="post" action="#" enctype="multipart/form-data">
    <input type="hidden" id="id" name="id"/>
    <input type="hidden" id="type" name="type"/>
    <div class="popup">
        <table class="single">
            <tr>
                <td>仿真实例:</td>
                <td>
                    <input class="easyui-combobox" id="simname" name="simname" style="width: 200px"
                           data-options="valueField:'id',textField:'text',limitToList:'true',url:'${ctx}/sim/instance/getAllSimSystemOrder?type=XML',panelHeight:'auto'"
                           onenter="onSearch" required="true"/>
                </td>
            </tr>
            <tr>
                <td>报文名称:</td>
                <td><input class="easyui-textbox" id="msgname" name="msgname" style="width: 200px" required="true"/>
                </td>
            </tr>
            <tr>
                <td>报文编码:</td>
                <td><input class="easyui-textbox" id="msgType" name="msgType" style="width: 100px" required="true"/>
                </td>
            </tr>
            <tr>
                <td><label>加签验签:</label></td>
                <td><select id="signFlag" name="signFlag" class="easyui-combobox" style="width:180px;"
                            data-options="panelHeight:'auto',limitToList:'true'">
                    <option value="true">是</option>
                    <option value="false">否</option>
                </select></td>
            </tr>
            <tr>
                <td><label>报文标准:</label></td>
                <td><input class="easyui-combobox" id="standard" name="standard" style="width: 200px"
                           valueField="id" textField="text" limitToList="true" url="${ctx}/generic/dict/STANDARD" panelHeight="auto"
                           required="true"/>
                </td>
            </tr>
            <tr>
                <td><label>报文模板:</label></td>
                <td><input class="f1 easyui-filebox" style="width:200px" id="file1" name="file1"
                           buttonText="选择文件" required="true"></td>
            </tr>
            <tr>
                <td><label>schema校验文件:</label></td>
                <td><input class="f1 easyui-filebox" style="width:200px" id="file2" name="file2"
                           buttonText="选择文件"></td>
                <!--  <td><input class="easyui-combobox" id="schemaFilename" name="schemaFilename" style="width: 200px"
                           data-options="valueField:'id',textField:'text',url:'${ctx}/sim/message/getAllSchemaFile',panelHeight:'auto'"
                           onenter="onSearch"/></td>-->
            </tr>
        </table>
    </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onCancel();">取消</a>
    </div>
</form>

<script>

    function getData() {
        var formData = {};
        formData.id = $("#id").val();
        formData.type = '${type}';
        formData.name = $("#msgname").textbox('getValue');
        formData.msgType = $("#msgType").textbox('getValue');
      // formData.modelFileName = $("#modelFileName").textbox('getValue');
        formData.signFlag = $("#signFlag").combobox("getValue");
        formData.standard = $("#standard").combobox("getValue");
        return formData;
    }
   /* function setData(param) {
        if (param.action == 'update') {
            $.ajax({
                url: "${ctx}/sim/instance/message/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
                    $("#simname").combobox('setValue', vo.systemInstance.id);
                    $("#msgname").textbox('setValue', vo.name);
                    $("#signFlag").combobox('setValue', '' + vo.signFlag);
                    $("#msgType").textbox('setValue', vo.msgType);
                   // $("#schemaFilename").combobox('setValue', vo.schemaFile.id);
                    $("#type").val(vo.type);
                }
            });
        }
    }*/
    function onOk() {
        var form = $('#fm');
        var flag = form.form('validate');
        if (!flag) return;
        var formData = form.serialize();
        formData = getData();
        var simid = $("#simname").combobox('getValue');
      //  var schemaFileid = $("#schemaFilename").combobox('getValue');
        $.messager.progress();
        $.ajax({
            url: '${ctx}/sim/instance/message/addXml/' + simid,
            type: 'post',
            data: formData,
            cache: false,
            async: false,
            success: function (text) {
                $("#id").val(text);
                if(text == "null" ||text ==''){
                    $.messager.progress('close');
                    showMsg('温馨提示','该报文已经存在',true);
                    closeWindow();
                }else{
                var s = $('#file2').filebox('getValue');
                if (s == null || s == ''){
                    $("#fm").ajaxSubmit({async: false,url:'${ctx}/sim/instance/message/upload', success: function (result) {
                        $.messager.progress('close');
                        if (result == "true") {
                            closeWindow();
                        }else{
                            showMsg('温馨提示',result.msg,true);
                            closeWindow();
                        }
                    }});
                    return;
                }
                $("#fm").ajaxSubmit({async: false,url:'${ctx}/sim/instance/message/uploads', success: function (result) {
                    $.messager.progress('close');
                    if (result == "true") {
                        closeWindow();
                    }else{
                        showMsg('温馨提示',result.msg,true);
                        closeWindow();
                    }
                }});
              /*  $("#fm").ajaxSubmit({async: false, success: function (result) {
                    if (result == "true") {
                        closeWindow();
                    }else{
                        showMsg('温馨提示',result.msg,true);
                        closeWindow();
                    }
                }})*/
            }},
           /* success: function (data) {
                if (data.success) {
                    closeWindow();
                } else {
                    showMsg('温馨提示', data.msg, true);
                }
            },*/
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $.messager.progress('close');
                alert(XMLHttpRequest.responseText);
                closeWindow();
            }
        });
    }
    function onCancel() {
        closeWindow();
    }
</script>
