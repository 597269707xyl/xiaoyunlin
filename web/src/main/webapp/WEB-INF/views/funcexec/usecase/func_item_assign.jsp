<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="form-field-select" method="post">

    <table cellpadding="0" cellspacing="0">
        <tr>
            <td>
                <h4 style="margin:0;line-height:22px;font-size:13px;">人员列表：</h4>
                <table id="datalist1" style="width:200px;height:200px">
                </table>

            </td>
            <td style="padding:5px;">
                <input type="button" value=">>" onclick="selectUser()" style="width:55px;"/><br/>
                <input type="button" value="<<" onclick="unselectUser()"
                       style="width:55px;margin-bottom:2px;"/><br/>
            </td>
            <td>
                <h4 style="margin:0;line-height:22px;font-size:13px;">已选人员：</h4>
                <table id="datalist2" style="width:200px;height: 200px">
                </table>
            </td>
        </tr>
    </table>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
           onclick="onSelOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="closeUserDistribution()">取消</a>
    </div>

</form>

<script>
    $("#datalist1").datalist({
        checkbox: true,
        singleSelect: false,
        url: '${ctx}/func/exec/getUnselectedUser?itemId=' + '${itemId}',
        columns: [[
            {field: 'id', title: 'id', hidden: true},
            {field: 'name', title: '姓名', width: 100}
        ]]
    });
    $("#datalist2").datalist({
        checkbox: true,
        singleSelect: false,
        url: '${ctx}/func/exec/getSelectedUser?itemId=' + '${itemId}',
        columns: [[
            {field: 'id', title: 'id', hidden: true},
            {field: 'name', title: '姓名', width: 100}
        ]]
    });

    function selectUser() {
        var tempRows = $('#datalist1').datalist('getSelections');

        for (var i = 0; i < tempRows.length; i++) {
            $('#datalist2').datalist('appendRow', tempRows[i]);
            var tempindex = $('#datalist1').datalist('getRowIndex', tempRows[i]);
            $('#datalist1').datalist('deleteRow', tempindex);
        }

    }
    function unselectUser() {
        var tempRows = $('#datalist2').datalist('getSelections');

        for (var i = 0; i < tempRows.length; i++) {
            $('#datalist1').datalist('appendRow', tempRows[i]);
            var tempindex = $('#datalist2').datalist('getRowIndex', tempRows[i]);
            $('#datalist2').datalist('deleteRow', tempindex);
        }
    }

    function onSelOk() {
        var rows = $("#datalist2").datagrid('getRows');
        var ids = new Array();
        if (rows && rows.length > 0) {
            for (var i = 0; i < rows.length; i++) {
                ids.push(rows[i].id);
            }
        } else {
            ids = null;
        }
        var itemId = "${itemId}";
        $.post("${ctx}/func/exec/addMembers", {itemId: itemId, id: ids}, function (data) {
            if (data.success) {
                closeUserDistribution();
            } else {
                showMsg("提示", "操作失败", true);
            }
        });
    }

    function closeUserDistribution(){
        $('#window-user-distribution').window('close');
    }
</script>
