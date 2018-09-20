<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<form id="fm" class="easyui-form">
    <div class="popup">
        <input type="hidden" id="id" name="id"/>
        <div class="easyui-panel" style="padding:5px;height: 350px">
            <ul id="tree" name="tree"/>
        </div>
    </div>
        <div class="buttonBar">
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
               onclick="onOk();">确定</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
               onclick="onCancel();">取消</a>
        </div>
</form>



<script>

    function setData(param) {
        var roleId = param.id;
        if (roleId) {
            $("#id").val(roleId);
            $("#tree").tree({
                url: '${ctx}/sys/func/allTreeList',
                method: 'get',
                animate: true,
                checkbox: true,
                cascadeCheck: true,
                onLoadSuccess: function (node, data) {
                    $.post("${ctx}/sys/role/funcs", {rid: roleId}, function (data) {
                        var arr = new Array();
                        arr = data.split(",");
                        for (i = 0; i < arr.length; i++) {
                            var node = $("#tree").tree('find', arr[i]);
                            if (node){
                                var children = $("#tree").tree('getChildren', node.target);
                                if (children == null || children.length < 1) {
                                    $("#tree").tree('check', node.target);
                                }
                            }


                        }
                    });
                }
            });

        }
    }
    function getData() {
        var data = {};
        data.rid = $("#id").val();
        var nodes = $('#tree').tree('getChecked', ['checked','indeterminate']);
        var ids = new Array();
        for (var i = 0; i < nodes.length; i++) {
            ids.push(nodes[i].id);
        }
        data.funcIds = ids;
        return data;
    }
    function onOk() {
        commonSubmitForm('${ctx}/sys/role/funAlloc');
    }
    function onCancel() {
        closeWindow();
    }
</script>
