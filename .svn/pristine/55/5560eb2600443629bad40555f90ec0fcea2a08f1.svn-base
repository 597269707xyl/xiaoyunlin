<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>配置文件</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <script>
        $(function () {
            $('#dg').datagrid({
                //fit: true,
                height: $(window).height() - 80,
                fitColumns: true,
                url: '${ctx}/func/config/query',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {
                        field: 'name', title: '测试项', width: 70,
                        formatter: function (value, row, index) {
                            if (value == null) {
                                return "公共属性";
                            }
                            return value;
                        }
                    },
                    {
                        field: 'projectName', title: '测试项目', width: 70,
                        formatter: function (value, row, index) {
                            if (value == null) {
                                return "公共属性";
                            }
                            return value;
                        }
                    },
                    {field: 'variable_zh', title: '名称', width: 70},
                    {field: 'variable_en', title: '标识', width: 70},
                    {field: 'variable_value', title: '值', width: 70, editor: 'text'}
                ]]
            }).datagrid('enableCellEditing');
            $('#project').combobox({
                onChange: function (newValue, oldValue) {
                    if(newValue != -1){
                        $('#item').combobox({url:"${ctx}/func/config/getItemListByProject?projectId=" + newValue});
                    } else {
                        $('#item').combobox("loadData", [{id:'-1',name:'公共属性'}]);
                    }
                }
            });
        });
        function onSearch() {
            var name = $("#name").textbox("getValue");
            var item = $("#item").combobox("getValue");
            var project = $("#project").combobox("getValue");
            if(project != '' && item == ""){
                showMsg("提示", "请选择一个测试项!", true);
            }
            var p = {};
            if (name != null && name != "") {
                p['name'] = name;
            }
            if (item != null && item != "") {
                p['itemId'] = item;
            }
            $("#dg").datagrid('load', p);
        }

        function add() {
            commonShowWindow(null, "新增", "${ctx}/func/config/addmain", 320, 260, true, false, false);
        }
        function edit() {
            var rows = $("#dg").datagrid('getSelections');
            if (rows == null || rows.length != 1) {
                showMsg("提示", "请选择一条记录进行修改", true);
                return;
            }
            var row = $("#dg").datagrid('getSelected');
            if (row == null) {
                showMsg("提示", "请选择要修改的记录", true);
            } else {
                var url = "${ctx}/func/config/addmain";
                var param = {action: "update", id: row.id};
                commonShowWindow(param, "修改", url, 320, 260, true, false, false);
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
                if (showConfirm("提示", "确定要删除数据吗？", function () {
                            $.post("${ctx}/func/config/del", {ids: ids}, function (data) {
                                if (data.success) {
                                    $("#dg").datagrid('reload');
                                } else {
                                    showMsg("提示", "操作出错", true);
                                }
                            });

                        }));
            } else {
                showMsg("提示", "请选择要删除的数据", true);
            }
        }

        function copy() {
            var nodes = $("#dg").datagrid('getSelections'), ids = new Array();
            if (nodes && nodes.length > 0) {
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
            }
            if (nodes && nodes.length > 0) {
                $('#copy-window').window({
                    title: '目标路径',
                    width: 350,
                    height: 100,
                    href: '${ctx}/func/config/copyPage',
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
                        setConfigCopyData(ids)
                    },
                    onClose: function () {
                    }
                });
            } else {
                showMsg("提示", "请选择要复制的数据", true);
            }
        }

        function uploadConfig() {
            commonShowWindow(null, "上传", "${ctx}/func/config/uploadPage", 330, 150, true, false, false);
        }

        $.extend($.fn.datagrid.methods, {
            editCell: function (jq, param) {
                return jq.each(function () {
                    var opts = $(this).datagrid('options');
                    var fields = $(this).datagrid('getColumnFields', true).concat($(this).datagrid('getColumnFields'));
                    for (var i = 0; i < fields.length; i++) {
                        var col = $(this).datagrid('getColumnOption', fields[i]);
                        col.editor1 = col.editor;
                        if (fields[i] != param.field) {
                            col.editor = null;
                        }
                    }
                    $(this).datagrid('beginEdit', param.index);
                    var ed = $(this).datagrid('getEditor', param);
                    if (ed) {
                        if ($(ed.target).hasClass('textbox-f')) {
                            $(ed.target).textbox('textbox').focus();
                        } else {
                            $(ed.target).focus();
                        }
                    }
                    for (var i = 0; i < fields.length; i++) {
                        var col = $(this).datagrid('getColumnOption', fields[i]);
                        col.editor = col.editor1;
                    }
                });
            },
            enableCellEditing: function (jq) {
                return jq.each(function () {
                    var dg = $(this);
                    var opts = dg.datagrid('options');
                    opts.oldOnClickCell = opts.onClickCell;
                    opts.onClickCell = function (index, field) {
                        if (opts.editIndex != undefined) {
                            if (dg.datagrid('validateRow', opts.editIndex)) {
                                dg.datagrid('endEdit', opts.editIndex);
                                var upda = $('#dg').datagrid("getChanges", "updated");
                                if (upda.length > 0) {
                                    var str = JSON.stringify(upda);
                                    $.ajax({
                                        type: "POST",
                                        url: "${ctx}/func/config/update",
                                        data: str,
                                        cache: false,
                                        async: false,
                                        dataType: "json",
                                        contentType: "application/json",
                                        success: function (data) {

                                        }
                                    });
                                }
                                opts.editIndex = undefined;

                            } else {
                                return;
                            }

                        }
                        dg.datagrid('selectRow', index).datagrid('editCell', {
                            index: index,
                            field: field
                        });
                        opts.editIndex = index;
                        opts.oldOnClickCell.call(this, index, field);
                    }
                });
            }
        });
        function downloadConfig(){
            commonShowWindow(null, "下载", "${ctx}/func/config/downloadPage", 250, 130, true, false, false);
        }
    </script>
</head>
<body>
<div class="toolBar">
    <table style="width:100%;">
        <tr>
            <td style="width:100%;">
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="add();">增加</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="del();">删除</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="edit();">修改</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="copy();">复制</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="downloadConfig();">下载</a>
                <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="uploadConfig();">上传</a>
            </td>
        </tr>
    </table>
    <form id="queryForm">
        <table>
            <tr>
                <td></td>
                <td>测试项目:</td>
                <td><input class="easyui-combobox" id="project" name="project" style="width: 200px"
                           valueField="id" textField="name" limitToList="true" url="${ctx}/proj/proj/findAll?type=config"
                           panelHeight="200"
                           onenter="onSearch"/></td>
                <td>测试项:</td>
                <td><input class="easyui-combobox" id="item" name="item" style="width: 200px"
                           valueField="id" textField="name" limitToList="true"
                           panelHeight="200" data-options="onLoadSuccess:function (data) {
                                for (var item in data[0]) {
                                    if (item == 'id') {
                                        $(this).combobox('select', data[0][item]);
                                    }
                                }
                            }"
                           onenter="onSearch"/></td>
                <td>名称/标识:</td>
                <td>
                    <input class="easyui-textbox" type="text" id="name" name="name" style="width: 100px"
                           onenter="onSearch"/>
                </td>
                <td><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'"
                       onclick="onSearch();">查询</a></td>
            </tr>
        </table>
    </form>
</div>
<div style="height: auto">
    <table id="dg"></table>
</div>
<div id="copy-window"></div>
</body>
</html>
