<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="height: 310px">
    <form id="tg-usecase-data-detail1-form">
        <table id="tg-usecase-data-detail1">
        </table>
    </form>
    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="onUsecaseDataDetailFormOk();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="onUsecaseDataDetailFormCancel();">取消</a>
    </div>
</div>
<script>
    var messageId;
    var usecaseDataId;
    var source;
    var index;
    function setData(param){
        var usecaseDataList = param.usecaseDataList;
        messageId = param.messageId;
        usecaseDataId = param.usecaseDataId;
        source = param.source;
        index = param.index;
        var params = 'messageId='+messageId;
        if(typeof (usecaseDataId) != 'undefined' && usecaseDataId != '' && usecaseDataId != 0){
            params = 'usecaseDataId='+usecaseDataId;
        }
        $.ajax({
            url: '${ctx}/func/exec/msgDataNew?'+params,
            type: 'POST',
            cache: false,
            success: function(data){
                var str = "<tr>";
                for(var i=0; i<data.length; i++){
                    if(data[i].moFlag=='1'){
                        str += '<td><label style="color: red">' + data[i].fieldName + ':</label></td>' +
                                '<td><input name="' + data[i].fieldId + '" value="' + htmlEscape(data[i].fieldValue) + '" class="easyui-textbox form-input" style="width: 200px;" /></td>';
                    } else {
                        str += '<td><label>' + data[i].fieldName + ':</label></td>' +
                                '<td><input name="' + data[i].fieldId + '" value="' + htmlEscape(data[i].fieldValue) + '" class="easyui-textbox form-input" style="width: 200px;" /></td>';
                    }
                    if(i %2 == 1){
                        str += "</tr><tr>";
                    }
                }
                str += "</tr>";
                $('#tg-usecase-data-detail1').html(str).find('input').textbox();
//                $.parser.parse();
                if(typeof(usecaseDataList) != "undefined" && usecaseDataList != ''){
                    usecaseDataList = decodeURIComponent(usecaseDataList);
                    var obj = {};
                    var arr = usecaseDataList.split("&");
                    for(var i=0;i<arr.length; i++){
                        var m = arr[i];
                        var newArr = m.split("=");
                        if(newArr.length==2){
                            obj[newArr[0]] = newArr[1];
                        } else {
                            obj[newArr[0]] = "";
                        }
                    }
                    $('#tg-usecase-data-detail1-form').form('load', obj);
                }
            }
        });

    }
    function htmlEscape(text){
        return text.replace(/[<>"'&]/g, function(match, pos, originalText){
            switch(match){
                case "<": return "&lt;";
                case ">":return "&gt;";
                case "&":return "&amp;";
                case "\"":return "&quot;";
                case "'":return "&#39;";
            }
        });
    }
    function onUsecaseDataDetailFormOk(){
        var form = $('#tg-usecase-data-detail1-form');
        var flag = form.form('validate');
        if (!flag) return;
        var rows = form.serialize();
        var strJson = rows;
        if(typeof (usecaseDataId) == 'undefined' || usecaseDataId == 0){
            if(source == 'data'){
                $('#usecaseDataList').textbox('setValue',strJson);
            } else if(source == 'list'){
                var rows = $('#dg-usecase-data-list').datagrid('getRows');
                rows[index].usecaseDataList = strJson;
                $('#dg-usecase-data-list').datagrid('loadData', rows);
            }
            onUsecaseDataDetailFormCancel();
        } else {
            $.ajax({
                url: '${ctx}/func/exec/updateUsecaseDataMsg',
                type: 'POST',
                data: {usecaseDataId: usecaseDataId, msg: strJson},
                success: function(data){
                    if(data.success){
                        onUsecaseDataDetailFormCancel();
                    } else {
                        showMsg('提示', '修改失败!', true);
                    }
                }
            })
        }
    }

    function onUsecaseDataDetailFormCancel(){
        $('#window-usecase-data-detail').window('close');
    }
</script>