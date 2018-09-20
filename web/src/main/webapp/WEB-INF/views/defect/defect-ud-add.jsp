<%--
  Created by IntelliJ IDEA.
  User: huangbo
  Date: 2018/8/1
  Time: 13:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post" action="#" enctype="multipart/form-data">
    <input type="hidden" id="id" name="id"/>
    <input type="hidden" id="stype" name="stype" class="easyui-textbox"/>
    <div class="popup">
        <table class="single">
            <tr>
                <td>测试项:</td>
                <td>
                    <input id="testItem1" name="testItem1" required/>
                </td>
            </tr>

            <tr>
                <td>缺陷标识:</td>
                <td><input class="easyui-textbox" type="text" name="abbreviation" id="abbreviation"/></td>
            </tr>
            <tr>
                <td>修复状态:</td>
                <td>
                    <input id="fixStatus" name="fixStatus" class="easyui-combobox" editable="false"
                           data-options="valueField: 'value',textField: 'text',panelHeight:'auto',
                            icons:[{iconCls:'icon-clear',handler: function(e){$(e.data.target).combobox('clear');}}],
                            data: [{value: '1',text: '已修复'},{value: '0',text: '未修复'}],
                            onSelect:function(record){
                            if(record.value == '0'){

                            $('#fixTime').datetimebox('disable');
                            }else if(record.value == '1'){$('#fixTime').datetimebox('enable');
                            }
                            }" required>
                    </input>


                </td>
            </tr>
            <tr>
                <td>缺陷等级:</td>
                <td>
                    <input id="grade" name="grade" class="easyui-combobox" editable="false"
                           data-options="valueField: 'value',textField: 'text',panelHeight:'auto',
                            icons:[{iconCls:'icon-clear',handler: function(e){$(e.data.target).combobox('clear');}}],
                            data: [{value: 'H',text: '高'},{value: 'M',text: '中'},{value: 'L',text: '低'}]" required>
                    </input>
                </td>
            </tr>
            <tr>
                <td>来往账类型:</td>
                <td>
                    <input id="mark1" name="mark" class="easyui-combobox" editable="false"
                           data-options="valueField: 'value',textField: 'text',panelHeight:'auto',
                            icons:[{iconCls:'icon-clear',handler: function(e){$(e.data.target).combobox('clear');}}],
                            data: [{value: 'send',text: '往账'},{value: 'recv',text: '来账'}]" required>
                    </input>
                </td>
            </tr>

            <tr>
                <td>修复时间:</td>
                <td><input class="easyui-datetimebox" name="fixTime" id="fixTime" required editable="false"
                           data-options="icons:[{iconCls:'icon-clear',handler: function(e){$(e.data.target).datetimebox('clear');}}]">
                </td>
            </tr>
            <tr>
                <td>缺陷描述:</td>
                <td><input class="easyui-textbox" id="descript" name="descript" data-options="multiline:true"
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
    var selectType;
    $(function () {
        $("#testItem1").combotree({
            url: '${ctx}/defect/getAllItemTree/',
            valueField: 'id',
            textField: 'text',
            icons: [{
                iconCls: 'icon-clear', handler: function (e) {
                    $(e.data.target).combotree('clear');
                }
            }],
            onSelect: function (node) {
                var type = node.type;
                selectType = type;
                $('#stype').textbox('setValue', selectType);
            },
            onLoadSuccess: function () {

                var id = $('#testItem1').combobox('getValue');
                if (id != "") {
                    var t = $('#testItem1').combotree('tree');
                    var node = t.tree('find', id);
                    t.tree('select', node.target);
                    t.tree('expandTo', node.target);
                }
            }
        });
    });


    function setData(param) {
        if (param.action == 'update') {
            if (param.type == 'IMP') {
                $('#mark1').combobox('disable');
                $('#testItem1').combobox('disable');
            }
            $.ajax({
                url: "${ctx}/defect/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
                    if (vo.nxyFuncItem != null) {
                        $('#testItem1').combotree('setValue', {id: vo.nxyFuncItem.id, text: vo.nxyFuncItem.name});
                        $('#stype').textbox('setValue', 'item');
                    } else if (vo.testProject != null) {
                        $('#testItem1').combotree('setValue', {id: vo.testProject.id, text: vo.testProject.name});
                        $('#stype').textbox('setValue', 'project');
                    }
                }
            });
        }
    }

    function onOk() {
        commonSubmitForm('${ctx}/defect/addUd');
    }
    function onUploadCancel() {
        closeWindow();
    }
</script>