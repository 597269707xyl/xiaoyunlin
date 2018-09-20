<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" class="easyui-form" method="post">
    <input type="hidden" id="id" name="id"/>
    <div class="popup">
    <table class="single">
        <tr>
            <td  class="popup_label">仿真系统名称:</td>
            <td><input class="easyui-textbox" id="name" name="name" required="true" style="width: 120px"/></td>
        </tr>
        <tr><td  class="popup_label">版本号:</td>
            <td><input class="easyui-textbox" id="version" name="version" style="width: 120px"/></td>
        </tr>
        <tr>
            <td  class="popup_label">通信协议:</td>
            <td><input id="protocol" name="protocol" required="true" style="width: 120px" /></td>
        </tr>
        <tr>
        <tr style="display: none">
            <td  class="popup_label">通信模式:</td>
            <td><input id="tcpMode" name="tcpMode" style="width: 120px" /></td>
        </tr>
        <tr>
            <td  class="popup_label">报文类型:</td>
            <td><input id="msgType" name="msgType" required="true" style="width: 120px" ></td>
        </tr>
        <tr style="display: none">
            <td  class="popup_label">是否MD5校验:</td>
            <td>
                <select id="md5Flag" name="md5Flag" class="easyui-combobox" style="width:120px;"
                        data-options="panelHeight:'auto',limitToList:'true'">
                    <option value="true">是</option>
                    <option value="false">否</option>
                </select>
            </td>
        </tr>
        <tr style="display: none">
            <td  class="popup_label">是否MAC加押:</td>
            <td>
                <select id="macFlag" name="macFlag" class="easyui-combobox" style="width:120px;"
                        data-options="panelHeight:'auto',limitToList:'true'">
                    <option value="true">是</option>
                    <option value="false">否</option>
                </select>
            </td>
        </tr>
        <tr>
            <td  class="popup_label">是否schema校验:</td>
            <td>
                <select id="schemaFlag" name="schemaFlag" class="easyui-combobox" style="width:120px;"
                        data-options="panelHeight:'auto',limitToList:'true'">
                    <option value="true">是</option>
                    <option value="false">否</option>
                </select>
            </td>
        </tr>
        <tr>
            <td  class="popup_label">报文组成格式:</td>
            <td><input id="msgFormat" name="msgFormat" required="true" style="width: 120px" /></td>
        </tr>
        <tr><td class="popup_label">报文头:</td>
            <td><input id="headFieldSet" name="headFieldSet" style="width: 120px" ></td>
        <tr><td class="popup_label">描述:</td>
            <td><input class="easyui-textbox" data-options="multiline:true"  id="descript" name="descript" style="height:40px ;width: 120px" ></td>
        </tr>
    </table>
        </div>
    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onCancel();">取消</a>
    </div>
</form>
<script>
    $("#msgFormat").combogrid({
        panelWidth: 200,
        idField: 'id',
        textField: 'name',
        editable:false,
        url: '${ctx}/msg/format/getAll',
        method: 'get',
        columns: [[
            {field:'id',title:'id', hidden: true},
            {field:'name',title:'名称',width:70},
            {field:'typeDis',title:'类型',width:70}
        ]],
        fitColumns: true
    });

    $("#protocol").combobox({
        url:'${ctx}/generic/dict/PROTOCOL',
        valueField:'id',
        textField:'text',
        panelHeight:'auto',
        limitToList:true
    });
    $("#tcpMode").combobox({
        url:'${ctx}/generic/dict/TCPMODE',
        valueField:'id',
        textField:'text',
        panelHeight:'auto',
        limitToList:true
    });
$("#headFieldSet").combogrid({
    panelWidth: 200,
    idField: 'id',
    textField: 'name',
    editable:false,
    url: '${ctx}/msg/fieldset/getAllBySetType?setType=HEAD',
    method: 'get',
    columns: [[
        {field:'id',title:'id', hidden: true},
        {field:'name',title:'名称',width:120}
    ]],
    fitColumns: true
});
    $("#msgType").combobox({
        url:'${ctx}/generic/dict/MSGTYPE',
        valueField:'id',
        textField:'text',
        panelHeight:'auto',
        limitToList:true
    });
    function setData(param){
        if (param.action == 'update'){
            $.ajax({
                url: "${ctx}/sim/system/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load',vo);
                    $('#md5Flag').combobox('setValue',''+vo.md5Flag);
                    $('#macFlag').combobox('setValue',''+vo.macFlag);
                    $('#schemaFlag').combobox('setValue',''+vo.schemaFlag);
                }
            });
        }
    }
    function getData(){
        var formData = {};
//        formData.tcpMode = $("input[name='tcpMode']:checked").val();
        formData.tcpMode = $('#tcpMode').combobox('getValue');
        formData.md5Flag = $('#md5Flag').combobox('getValue');
        formData.macFlag  = $('#macFlag').combobox('getValue');
        formData.schemaFlag  = $('#schemaFlag').combobox('getValue');

        formData.id = $('#id').val();
        formData.msgFormatId = $('#msgFormat').combogrid('getValue');
        formData.headFieldSetId = $('#headFieldSet').combogrid('getValue');

        formData.name = $('#name').textbox('getValue');
        formData.version = $('#version').textbox('getValue');
        formData.protocol = $('#protocol').combobox('getValue');
        formData.msgType = $('#msgType').combobox('getValue');

        formData.descript = $('#descript').textbox('getValue');

        return formData;
    }
    function onOk() {
        commonSubmitForm('${ctx}/sim/system/add');
    }
    function onCancel() {
        closeWindow();
    }
</script>
