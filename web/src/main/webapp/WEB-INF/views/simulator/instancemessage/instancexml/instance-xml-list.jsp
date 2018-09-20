<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Title</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <link href="${ctx}/asset/css/style.css" rel="stylesheet" type="text/css"/>
    <script>
        $(function () {
            $('#dg').datagrid({
                fit: true,
                fitColumns: true,
                singleSelect: true,
                rownumbers:true,
                url: '${ctx}/sim/instance/message/query?type=XML',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    //{field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'name', title: '报文名称', width: 100},
                    {field: 'msgType', title: '报文编号', width: 100},
                    {field: 'respMsgName', title: '应答报文名称', width: 100},
                    {field: 'respMesgType', title: '应答报文编号', width: 100},
                    {
                        field: 'signFlag', title: '是否加签', width: 50,
                        formatter: function (value, row, index) {
                            if (row.signFlag) {
                                return '是';
                            } else {
                                return '否';
                            }
                        }
                    },
                    {field: 'standardDis', title: '报文标准', width: 50},
                    {
                        field: 'sysinstancename', title: '仿真实例名称', width: 100,
                        formatter: function (value, row, index) {
                            if (row.systemInstance) {
                                return row.systemInstance.name;
                            } else {
                                return value;
                            }
                        }
                    },
                    {
                        field: 'modelFileContent',title: '模板文件',width: 100,
                        formatter: function(value, row, index){
                            return '<a href="#" onclick="fileModeContent('+ row.id +')">报文模板</a>&nbsp;&nbsp;|'+
                                    '&nbsp;&nbsp;<a href="#" onclick="fileSchemaContent(' + row.id + ')">schema模板</a>';
                        }
                    }
                ]],
                onClickRow: function (index, row) {
                    reloadData('fields');
                }
            });
            $('#dg-fields').datagrid({
                fit: true,
                fitColumns: true,
                url: '${ctx}/sim/instance/message/getFields',
                singleSelect:false,
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'seqNo', title: '序号', width: 30},
                    {
                        field: 'fieldId', title: '域id', width: 400,
                        formatter: function (value, row, index) {
                            if (row.msgField) {
                                return row.msgField.fieldId;
                            } else {
                                return value;
                            }
                        }
                    },
                    {
                        field: 'nameZh', title: '域名称', width: 100,
                        formatter: function (value, row, index) {
                            if (row.msgField) {
                                return row.msgField.nameZh;
                            } else {
                                return value;
                            }
                        }
                    },
                    {
                        field: 'moFlag', title: '必填', width: 100,
                        formatter: function (value, row, index) {
                            if (row.moFlag) {
                                return '是';
                            } else {
                                return '否';
                            }
                        }
                    },
//                    {
//                        field: 'fixFlag', title: '自动生成', width: 100,
//                        formatter: function (value, row, index) {
//                            if (row.fixFlag) {
//                                return '否';
//                            } else {
//                                return '是';
//                            }
//                        }
//                    },
                    {
                        field: 'signFlag', title: '是否加签域', width: 100,
                        formatter: function (value, row, index) {
                            if (row.signFlag) {
                                return '是';
                            } else {
                                return '否';
                            }
                        }
                    },{
                        field: 'encryptFlag', title: '是否加密', width: 100,
                        formatter: function (value, row, index) {
                            if (row.encryptFlag) {
                                return '是';
                            } else {
                                return '否';
                            }
                        }
                    },
                    {field: 'defaultValue', title: '默认值', width: 100},
                    {field: 'respValueTypeDis', title: '取值方式', width: 100},
                    {field: 'respValue', title: '取值', width: 100}
//                    ,
//                    {field: 'fieldTypeDis', title: '域类型', width: 100}
                ]]
            });
        });
        function fileModeContent(id){
            showWindow("报文模板", "${ctx}/sim/instance/message/file/" +id + "/xml", 600, 450, true, true, true);
        }
        function fileSchemaContent(id){
            showWindow("schema模板", "${ctx}/sim/instance/message/file/" +id + "/schema", 600, 450, true, true, true);
        }
        function onSearch() {
            var name = $("#searchname").textbox("getValue");
            var mesgType = $("#searchmesgType").textbox("getValue");
            var systemInstance = $("#searchsimname").combobox("getValue");
            var p = {};
            if (name != null && name != "") {
                p['name|like'] = name;
            }
            if (mesgType != null && mesgType != "") {
                p['msgType|like'] = mesgType;
            }
            if (systemInstance != null && systemInstance != "") {
                p['systemInstance.id'] = systemInstance;
            }
            $("#dg").datagrid('load', p);
        }
        function selectFromSimSystem() {
            var title = "从仿真系统中添加报文";
            var param = {};
            $('#myWindow').window({
                title: title,
                width: 600,
                height: 460,
                href: '${ctx}/sim/instance/message/addInstanceMessage?type=XML',
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
                },
                onClose: function () {
                    reloadData('simSysInsMessage');
                }
            });
        }
        function add() {
            var title = "新增";
            $('#myWindow').window({
                title: title,
                width: 480,
                height: 300,
                href: '${ctx}/sim/instance/message/addMessage?type=XML',
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
                },
                onClose: function () {
                    reloadData('simSysInsMessage');
                }
            });
        }
        function Update() {
            var title = "编辑";
            var param = {};
            var row = $("#dg").datagrid('getSelected');
            if (row == null) {
                showMsg("提示", "请选择要修改的记录", true);
                return;
            } else {
                param.id = row.id;
            }
            $('#myWindow').window({
                title: title,
                width: 480,
                height: 270,
                href: '${ctx}/sim/instance/message/updateXml',
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
                    setXmlData(param)
                },
                onClose: function () {
                    reloadData('simSysInsMessage');
                }
            });
        }
        function setReponseRule() {
            var param = {};
            var row = $("#dg").datagrid('getSelected');
            if (row == null) {
                showMsg("提示", "请选择要设置的报文", true);
                return;
            } else {
                param.id = row.id;
                param.requestname = row.name;
                title = "设置请求报文应答规则";
            }
            $('#myWindow').window({
                title: title,
                width: 530,
                height: 430,
                href: '${ctx}/sim/instance/message/getReponseRule?type=XML',
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
                    setMyData(param)
                },
                onClose: function () {
                    reloadData('simSysInsMessage');
                }
            });
        }
        function reloadData(type) {
            if (type == 'simSysInsMessage') {
                $("#dg").datagrid('reload');
                $("#dg-fields").datagrid('reload', {id: null});
            } else if (type == 'fields') {
                var dg = $("#dg");
                var row = dg.datagrid('getSelected');
                if (row) {
                    $("#dg-fields").datagrid('reload', {id: row.id});
                }

            }
        }
        function del() {
            var nodes = $("#dg").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            if (nodes && nodes.length > 0) {
                if (showConfirm("提示", "确定要删除吗？", function () {
                            $.post("${ctx}/sim/instance/message/del", {ids: ids}, function (data) {
                                if (data.success) {
                                    reloadData('simSysInsMessage');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });
                        }));
            } else {
                showMsg("提示", "请选择要删除的记录", true);
            }
        }
        function addMessageField() {
            var dg = $("#dg");
            var dgComp = $("#dg-fields");
            var dgRow = dg.datagrid('getSelected');
            if (!dgRow) {
                showMsg('提示', '请选择报文', true);
                return;
            }
            $('#dlg-fields').window({
                title: '选择业务要素',
                width: 480,
                height: 430,
                href: '${ctx}/sim/instance/message/fieldList?id=' + dgRow.id + '&type=XML',
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

                },
                onClose: function () {
                    reloadData('fields');
                }
            });
        }
        function delMessageField() {
            var nodes = $("#dg-fields").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            var simMessageid = $("#dg").datagrid('getSelected').id;
            if (nodes && nodes.length > 0) {
                if (showConfirm("提示", "确定要删除吗？", function () {
                            $.post("${ctx}/sim/instance/message/delFields", {
                                simMessageid: simMessageid,
                                ids: ids
                            }, function (data) {
                                if (data.success) {
                                    reloadData('fields');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });
                        }));
            } else {
                showMsg("提示", "请选择要删除的记录", true);
            }
        }
        //批量加签
        function addSign(){
            var nodes = $("#dg-fields").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            var simMessageid = $("#dg").datagrid('getSelected').id;
            if (nodes && nodes.length > 0) {
                $.post("${ctx}/sim/instance/message/signFields", {
                    simMessageid: simMessageid,
                    ids: ids
                }, function (data) {
                    if (data.success) {
                        reloadData('fields');
                    } else {
                        showMsg("提示", "操作出错", true);
                    }
                });
            } else {
                showMsg("提示", "请选择要加签的记录", true);
            }
        }
        function addMoFlag(){
            var nodes = $("#dg-fields").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            var simMessageid = $("#dg").datagrid('getSelected').id;
            if (nodes && nodes.length > 0) {
                $.post("${ctx}/sim/instance/message/addMoFlagFields", {
                    simMessageid: simMessageid,
                    ids: ids
                }, function (data) {
                    if (data.success) {
                        reloadData('fields');
                    } else {
                        showMsg("提示", "操作出错", true);
                    }
                });
            } else {
                showMsg("提示", "请选择要加签的记录", true);
            }
        }
        function updateMessageField() {
            var rows = $("#dg-fields").datagrid('getSelections');
            if (rows == null || rows.length != 1) {
                showMsg("提示", "请选择一条记录进行修改", true);
                return;
            }
            var row = $("#dg-fields").datagrid('getSelected');
            if (row == null) {
                showMsg("提示", "请选择要修改的记录", true);
                return;
            } else {
                $('#dlg-fields').window({
                    title: '修改',
                    width: 480,
                    height: 430,
                    href: '${ctx}/sim/instance/message/field/edit?type=XML',
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
                        setData(row.id);
                    },
                    onClose: function () {
                        reloadData('fields');
                    }
                });
            }
        }
        function addDataTypeEnum() {
            var title = "新增";
            var dg = $("#dg-fields");
            var rows = dg.datagrid('getSelections');
            if (rows == null || rows.length != 1) {
                showMsg("提示", "请选择一条记录进行操作", true);
                return;
            }
            var dgRow = dg.datagrid('getSelected');
            if (!dgRow) {
                showMsg('提示', '请选择数据类型', true);
                return;
            }
            $('#dlg-fields').window({
                title: title,
                width: 420,
                height: 450,
                href: '${ctx}/sim/instance/message/addDataTypeEnum?id=' + dgRow.id,
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

                },
                onClose: function () {
                    reloadData('dataTypeEnum');
                }
            });
        }
        function cloneBaseData(){
            var row = $("#dg").datagrid('getSelected');
            if (row == null) {
                showMsg("提示", "请选择要同步的记录", true);
                return;
            }
            $.messager.progress();
            $.ajax({
                url: '${ctx}/sim/instance/message/cloneDataToSystem',
                type: 'post',
                data: {id:row.id},
                cache: false,
                async: false,
                success: function (text) {
                    $.messager.progress('close');
                    if(text.success){
                        closeWindow();
                        showMsg("提示", "同步数据成功", true);
                    } else {
                        showMsg("提示", text.msg, true);
                    }
                }
            });
        }
    </script>
</head>
<body>
<div class="easyui-layout content" data-options="fit:true">
    <div class="topContent" data-options="region:'north',split:true" style="height:60%;">
        <div class="easyui-layout" data-options="fit:true">
            <div class="toolBar" data-options="region:'north'">
                <table style="width:100%;">
                    <tr>
                        <td style="width:100%;">
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                               onclick="selectFromSimSystem();">从仿真系统中添加报文</a>
                            <%--<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"--%>
                               <%--onclick="add();">增加</a>--%>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
                               onclick="del();">删除</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                               onclick="Update();">修改</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                               onclick="setReponseRule();">设置请求报文应答规则</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                               onclick="cloneBaseData();">同步报文配置数据到仿真系统</a>
                        </td>
                    </tr>
                </table>
                <form id="queryForm">
                    <table>
                        <tr>
                            <td></td>
                            <td>仿真实例名称:</td>
                            <td>
                                <input class="easyui-combobox" id="searchsimname" name="searchsimname"
                                       style="width: 100px;"
                                       data-options="valueField:'id',textField:'text',limitToList:'true',url:'${ctx}/sim/instance/getAllSimSystemOrder?type=XML',panelHeight:'200'"
                                       onenter="onSearch"/>
                            </td>
                            <td>报文名称:</td>
                            <td>
                                <input class="easyui-textbox" type="text" id="searchname" name="searchname"
                                       style="width: 100px"
                                       onenter="onSearch"/>
                            </td>
                            <td>报文编码:</td>
                            <td>
                                <input class="easyui-textbox" type="text" id="searchmesgType" name="searchmesgType"
                                       style="width: 100px"
                                       onenter="onSearch"/>
                            </td>
                            <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                                   onclick="onSearch();">查询</a></td>
                        </tr>
                    </table>
                </form>
            </div>
            <div data-options="region:'center'">
                <table id="dg"></table>
            </div>
        </div>
    </div>
    <div class="bottomContent" data-options="region:'center',title:'业务要素列表'">
        <div class="easyui-layout" data-options="fit:true">
            <div class="toolBar" data-options="region:'north'">
                <table style="width:100%;">
                    <tr>
                        <td style="width:100%;">
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                               onclick="addMessageField();">增加</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
                               onclick="delMessageField();">删除</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"
                               onclick="updateMessageField();">修改</a>
                            <%--<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"--%>
                               <%--onclick="addDataTypeEnum();">自动化生成规则</a>--%>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                               onclick="addSign();">加签</a>
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
                               onclick="addMoFlag();">必填</a>
                        </td>
                    </tr>
                </table>
            </div>
            <div data-options="region:'center'">
                <table id="dg-fields"></table>
            </div>
        </div>
    </div>
</div>
<div id="dlg-fields"></div>
<div id="window-replyset"></div>
<div id="window-reply-param"></div>
</body>
</html>
