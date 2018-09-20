<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <link href="${ctx}/asset/css/style.css" rel="stylesheet" type="text/css"/>
    <script>
        $(function () {
            $('#dg').datagrid({
                height: $(window).height() -80,
                fitColumns: true,
                url: '${ctx}/generic/commonQuery/simSystem',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'name', title: '仿真系统名称', width: 150},
                    {field: 'version', title: '版本号', width: 50},
                    {field: 'protocolDis', title: '通信协议', width: 100},
                    /*{field: 'tcpMode', title: '通信模式', width: 100,
                        formatter:function(value,row,index){
                            if (row.tcpMode=="HALFDUPLEX"){
                                return "半双工";
                            }else if(row.tcpMode=="FULLDUPLEX"){
                                return '全双工';
                            }else{
                                return '';
                            }
                        }},*/
                    {field: 'msgType', title: '报文类型', width: 100,
                        formatter:function(value,row,index){
                        if (row.msgType.toUpperCase().indexOf("XML")>=0){
                            return "xml报文";
                        }else if(row.msgType.toUpperCase().indexOf("SELF")>=0){
                            return 'Self报文';
                        }else{
                            return '';
                        }
                    }
                    },
                    {field: 'md5Flag', title: '是否MD5校验', width: 100,hidden:true,
                        formatter: function(value,row,index){
                            if (row.md5Flag){
                                return '是';
                            } else {
                                return '否';
                            }
                        }},
                    {field: 'macFlag', title: '是否MAC加押', width: 100,hidden:true,
                        formatter: function(value,row,index){
                            if (row.macFlag){
                                return '是';
                            } else {
                                return '否';
                            }
                        }},
                    {field: 'schemaFlag', title: '是否schema校验', width: 100,
                        formatter: function(value,row,index){
                            if (row.schemaFlag){
                                return '是';
                            } else {
                                return '否';
                            }
                        }},
                    {field: 'msgFormat', title: '报文组成格式', width: 100,
                        formatter:function(value,row,index){
                            if (row.msgFormat){
                                return row.msgFormat.name;
                            }else {
                                return '';
                            }
                        }},
                    {field: 'headFieldSet', title: '报文头', width: 100,
                        formatter:function(value,row,index){
                            if (row.headFieldSet){
                                return row.headFieldSet.name;
                            }else {
                                return '';
                            }
                        }
                    },
                    {field: 'createTime', title: '创建日期', width: 100},
                    {field: 'descript', title: '描述', width: 100}
                ]]
            });
        });
        function onSearch() {
            var name = $("#name1").textbox("getValue");
            var version = $("#version1").textbox("getValue");
            var msgType = $("#mType").combobox("getValue");
            var p = {};
            if (name != null && name != "") {
                p['name|like'] = name;
            }
            if (version != null && version != "") {
                p['version|like'] = version;
            }
            if (msgType != null && msgType != "") {
                p['msgType'] = msgType;
            }
            $("#dg").datagrid('load', p);
        }

        function addOrUpdate(type){
            var title = "新增";
            var param = {action: type};
            if (type == 'update'){
                var rows = $("#dg").datagrid('getSelections');
                if (rows == null || rows.length != 1) {
                    showMsg("提示", "请选择一条记录进行修改", true);
                    return;
                }
                var row = $("#dg").datagrid('getSelected');
                if (row == null){
                    showMsg("提示","请选择要修改的记录",true);
                    return;
                }else {
                    param.id = row.id;
                    title = "编辑";
                }
            }else {
                title = "新增";
            }
            commonShowWindow(param,title,"${ctx}/sim/system/add",380,430,true,false,false);
        }

        function del(){
            var nodes = $("#dg").datagrid('getSelections'),ids = new Array();
            if(nodes && nodes.length>0){
                for(var i= 0;i<nodes.length;i++){
                    ids.push(nodes[i].id);
                }
            }
            if(nodes && nodes.length>0){
                if (showConfirm("提示","确定要删除该系统吗？",function(){
                            $.post("${ctx}/sim/system/del",{ids:ids},function(data){
                                if(data.success){
                                    $("#dg").datagrid('reload');
                                }else{
                                    showMsg("提示","操作出错",true);
                                }
                            });

                        }));
            }else{
                showMsg("提示","请选择要删除的系统",true);
            }
        }

    </script>
</head>
<body>
<div class="toolBar">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addOrUpdate('add');">增加</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="del();">删除</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="addOrUpdate('update');">修改</a>
            </td>
        </tr>
    </table>
    <form id="queryForm">
        <table>
            <tr>
                <td>仿真系统名称:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="name1" name="name1" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>版本号:</td>
                <td>
                    <input  class="easyui-textbox" id="version1" name="version1" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td>报文类型:</td>
                <td>
                <input class="easyui-combobox" id="mType" name="mType" style="width: 100px"
                       valueField="id" textField="text" limitToList="true" panelHeight="auto" url="${ctx}/generic/dict/MSGTYPE" onenter="onSearch"/>
                </td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>
            </tr>
        </table>
    </form>
</div>
<table id="dg"></table>
</body>
</html>
