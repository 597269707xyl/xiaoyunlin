<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>来账报文标识列表</title>
    <script src="${ctx}/asset/js/boot.js"></script>
    <script src="${ctx}/asset/js/app.js"></script>
    <script>
        $(function () {
            $('#dg').datagrid({
                //fit: true,
                height: $(window).height() - 20,
                fitColumns: true,
                url: '${ctx}/generic/commonQuery/asyncNotice',
                pagination: true,
                pageSize: 20,
                pageList: [10, 20, 30, 50],
                columns: [[
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: 'id', hidden: true},
                    {field: 'code', title: '报文编号', width: 70},
                    {field: 'name', title: '报文名称', width: 70},
                    {field: 'createTime', title: '来账时间', width: 70},
                    {field: 'msg', title: '测试数据', width: 100,
                        formatter:function(value, row, index){
                            return '<a href="javascript:data(' + row.id + ')">异步通知报文数据</a>';
                        }}
                ]]
            });
        });
        function data(id){
            $('#window-msg-panel').window({
                title: '异步通知报文数据',
                width: 1000,
                height:600,
                href: '${ctx}/func/asyncNotice/dataPage?id='+id,
                modal: false,
                minimizable: false,
                maximizable: false,
                shadow: false,
                cache: false,
                closed: false,
                collapsible: false,
                resizable: true,
                loadingMessage: '正在加载数据，请稍等......',
                onLoad: function () {
                }
            });
        }
        function onSearch() {
        }
    </script>
</head>
<body>
<div>
    <table id="dg"></table>
</div>
<div id="window-msg-panel"></div>
</body>
</html>
