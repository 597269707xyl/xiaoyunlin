<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" class="easyui-form" method="post">
    <input type="hidden" id="id" name="id"/>
    <div class="popup">
        <table>
            <tr>
                <td>仿真实例名称:</td>
                <td><input class="easyui-textbox" id="name" name="name" required="true" style="width: 150px"/></td>
                <td>依赖仿真系统:</td>
                <td>
                    <input id="simSystem" name="simSystem" required="true" style="width: 150px"/>
                </td>
            </tr>
            <tr>
                <%--<td>所属环境:</td>--%>
                <%--<td><input id="testEnvironment" name="testEnvironment" required="true" style="width: 150px" /></td>--%>
                <td>适配器:</td>
                <td><input class="easyui-combobox" id="institution" name="adapterId" style="width: 150px"/></td>
            </tr>
            <tr>
                <td>仿真服务IP:</td>
                <td><input class="easyui-textbox" id="insServerIp" name="insServerIp" value="${ip}" style="width: 150px"/></td>
                <td>仿真服务端口:</td>
                <td><input class="easyui-numberbox" id="insServerPort" name="insServerPort" value="${port}" style="width: 150px"/></td>
            </tr>
            <tr>
                <td>描述:</td>
                <td><input class="easyui-textbox" data-options="multiline:true" id="descript" name="descript"
                           style="height:40px ;"/></td>
            </tr>
        </table>
    </div>
    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="onCancel();">取消</a>
    </div>
</form>
<script>
    <%--$("#tcpMode").combobox({--%>
        <%--//url:'${ctx}/generic/dict/TCPMODE',--%>
        <%--valueField:'id',--%>
        <%--textField:'text',--%>
        <%--data:[{--%>
            <%--id: 'FULLDUPLEX',--%>
            <%--text: '前置系统(FE)'--%>
        <%--},{--%>
            <%--id: 'HALFDUPLEX',--%>
            <%--text: '行内系统(MB)'--%>
        <%--}],--%>
        <%--panelHeight:'auto',--%>
        <%--limitToList:true--%>
    <%--});--%>
    <%--$("#testEnvironment").combobox({--%>
        <%--url:'${ctx}/generic/dict/TEST_ENVIRONMENT_LIST',--%>
        <%--valueField:'id',--%>
        <%--textField:'text',--%>
        <%--panelHeight:'auto',--%>
        <%--limitToList:true--%>
    <%--});--%>
    $("#simSystem").combogrid({
        panelWidth: 200,
        idField: 'id',
        textField: 'text',
        url: '${ctx}/sim/system/getAllSimSystemOrder',
        method: 'post',
        fitColumns: true,
        columns: [[
            {field: 'id', title: 'id', hidden: true},
            {field: 'text', title: '名称', width: 70},
            {field: 'protocol', title: '通信协议', width: 70, hidden: true}
        ]],
        onSelect: function (record) {
            var record = $("#simSystem").combogrid('grid');
            var r = record.datagrid('getSelected');
            if (r.protocol == "TCP" || r.protocol == "TCPSHORTCONN") {
                $("#institution").combobox({
                    url:'${ctx}/sys/adapter/getAdapterList?type=TCP',
                    valueField:'id',
                    textField:'name',
                    panelHeight:'auto',
                    limitToList:true
                });
            }
            if (r.protocol == "TONGLINK" || r.protocol == "MQ") {
                $("#institution").combobox({
                    url:'${ctx}/sys/adapter/getAdapterList?type=MQ',
                    valueField:'id',
                    textField:'name',
                    panelHeight:'auto',
                    limitToList:true
                });
            }
            if (r.protocol == "HTTPS") {
                $("#institution").combobox({
                    url:'${ctx}/sys/adapter/getAdapterList?type=HTTP',
                    valueField:'id',
                    textField:'name',
                    panelHeight:'auto',
                    limitToList:true
                });
            }
        }
    });

    function setData(param) {
        if (param.action == 'update') {
            $.ajax({
                url: "${ctx}/sim/instance/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
                    $("#simSystem").combogrid({ disabled: true});
                    $("#simSystem").combogrid('setValue', '' + vo.simSystem.id);
                    var r = vo.simSystem;
                    if (r.protocol == "TCP" || r.protocol == "TCPSHORTCONN") {
                        $("#institution").combobox({
                            url:'${ctx}/sys/adapter/getAdapterList?type=TCP&instanceId='+param.id,
                            valueField:'id',
                            textField:'name',
                            panelHeight:'auto',
                            limitToList:true
                        });
                    }
                    if (r.protocol == "TONGLINK" || r.protocol == "MQ") {
                        $("#institution").combobox({
                            url:'${ctx}/sys/adapter/getAdapterList?type=MQ&instanceId='+param.id,
                            valueField:'id',
                            textField:'name',
                            panelHeight:'auto',
                            limitToList:true
                        });
                    }
                    if (r.protocol == "HTTPS") {
                        $("#institution").combobox({
                            url:'${ctx}/sys/adapter/getAdapterList?type=HTTP&instanceId='+param.id,
                            valueField:'id',
                            textField:'name',
                            panelHeight:'auto',
                            limitToList:true
                        });
                    }
                    if(vo.adapter) {
                        $('#institution').combobox('setValue', '' + vo.adapter.id);
                    }
                }
            });
        }
    }

    function getData() {
        var formData = {};
        formData.id = $('#id').val();
        var g = $('#simSystem').combogrid('grid');
        formData.id = $('#id').val();
        formData.simSystemId = $('#simSystem').combogrid('getValue');
        formData.adapterId = $('#institution').combogrid('getValue');
        formData.name = $('#name').textbox('getValue');
        formData.insServerIp = $('#insServerIp').textbox('getValue');
        formData.insServerPort = $('#insServerPort').numberbox('getValue');
        formData.descript = $('#descript').textbox('getValue');
        return formData;
    }
    function onOk() {
        var re =  /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        var insServerIp = $("#insServerIp").textbox('getValue');
        if(insServerIp != '' && !re.test(insServerIp)){
            showMsg("提示","请正确输入仿真服务IP地址",true);
            return;
        }
        commonSubmitForm('${ctx}/sim/instance/add');
    }
    function onCancel() {
        closeWindow();
    }
    $.extend($.fn.validatebox.defaults.rules, {
        equals: {
            validator: function (value, param) {
                return value != $(param[0]).val();
            },
            message: '服务端端口和本地端口不能相同'
        }
    });

    function showRevcQueue(){
        var instanceId = $('#id').val();
        $('#window-recv-queue').window({
            title: "接收队列列表",
            width: 530,
            height: 430,
            href: '${ctx}/sim/instance/recvQueue',
            modal: true,
            minimizable: false,
            maximizable: false,
            shadow: false,
            cache: false,
            closed: false,
            collapsible: false,
            resizable: true,
            loadingMessage: '正在加载数据，请稍等......',
            onLoad: function () {
                setData(instanceId)
            },
            onClose: function () {
            }
        });
    }
</script>
