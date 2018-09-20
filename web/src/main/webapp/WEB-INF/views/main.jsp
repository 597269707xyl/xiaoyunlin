<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>自动化测试仿真平台</title>
    <link rel="stylesheet" type="text/css" href="${ctx}/asset/js/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/asset/js/easyui/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/asset/css/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/asset/css/globle.css"/>
    <script type="text/javascript" src="${ctx}/asset/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/asset/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${ctx}/asset/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script>
        $(function () {
            var userState = "${userState}";
            if (userState == "New"){
                $('#password').dialog('open');
            }else {
                pageLoad();
            }
        });
        function pageLoad(){
            var flag = false;
            $("#mainTab").tabs({
                onContextMenu: function (e, title, index) {
                    e.preventDefault();
                    $("#mainTab").tabs('select', title);
                    $('#tabMenu').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                },
                onSelect: function(title,index){
                    $("#mainTab").tabs('select',title);
                }
            });
            $("#menuAccordion").accordion({
                fillSpace: true,
                fit: true,
                border: false,
                animate: true
            });
            $.post("${ctx}/sys/func/userFunc", {}, function (data) {
                $.each(data, function (i, e) {
                    var id = e.id;
                    $('#menuAccordion').accordion('add', {
                        title: e.text,
                        content: "<ul id='tree" + id + "' ></ul>",
                        selected: true,
                        iconCls: e.iconCls
                    });
                    $.parser.parse();
                    $.post("${ctx}/sys/func/userFunc", {id: id}, function (data) {
                        $("#tree" + id).tree({
                            data: data,
                            onBeforeExpand: function (node, param) {
                                $("#tree" + id).tree('options').url = "${ctx}/sys/func/userFunc?id=" + node.id;
                            },
                            onClick: function (node) {

                            },
                            onSelect: function (node) {
                                if (node.state == 'closed') {
                                    $(this).tree('expand', node.target);
                                } else if (node.state == 'open') {
                                    $(this).tree('collapse', node.target);
                                    if (node.url == null || node.url == '' || node.url == undefined) {
                                        return;
                                    }
                                    var id = node.id;
                                    var tabTitle = node.text;
                                    var url = "${ctx}" + node.url;
                                    var icon = node.iconCls;
                                    addTab(id, tabTitle, url, icon);
                                } else {

                                }
                            },
                            onLoadSuccess: function (node, data) {
                                if (flag == false) {
                                    var n = data[0];
                                    var u = n.url;
                                    if (u == null || u == '') {

                                    } else {
                                        var id = n.id;
                                        var nd = $(this).tree('find', id);
                                        $(this).tree('select', n.target);
                                        flag = true;
                                        $('#menuAccordion').accordion('select', e.text);
                                    }
                                }
                            }
                        });
                    });
                });
            });
        }

        function addTab(id,title,url,icon){
            var mainTab = $("#mainTab");
            if(!mainTab.tabs('exists',title)){
                mainTab.tabs('add',{
                    id:id,
                    title:title,
                    content:createFrame(url),
                    closable:true,
                    icon:icon,
                    tools:[{
                        iconCls:'icon-mini-refresh',
                        handler:function(){
                            $('#mainTab').tabs("select", $(this).parent().parent().first().first().text());
                            var pp = mainTab.tabs('getSelected');
                            mainTab.tabs('update', {
                                tab: pp,
                                options: {content: createFrame(url)}
                            });
                        }
                    }]
                });
            }else{
                mainTab.tabs('select',title);
            }
        }
        function createFrame(url){
            var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
            return s;
        }
        function closeTabs(type){
            var selTitle = $("#mainTab").tabs('getSelected').panel('options').title;
            if (type == 'all'){
                //关闭所有
                $("#mainTab li").each(function(index, obj) {
                    //获取所有可关闭的选项卡
                    var tab = $(".tabs-closable", this).text();
                    $("#mainTab").tabs('close', tab);
                });
            }else {
                //关闭其它
                $("#mainTab li").each(function(index, obj) {
                    //获取所有可关闭的选项卡
                    var tab = $(".tabs-closable", this).text();
                    if (selTitle!=tab){
                        $("#mainTab").tabs('close', tab);
                    }
                });
            }
        }
        function editPassword() {
            addTab('0','修改密码','${ctx}' + '/sys/user/updatePassword')
        }
        //首次登陆修改密码
        function resetPwd(){
            var form = $('#fm');
            var flag = form.form('validate');
            if (!flag) return;

            var data = form.serialize();
            $.post("${ctx}/resetPwd",data,function(data){
                if(data.success){
                    $('#password').dialog('close');
                    pageLoad();
                }else{
                    showMsg("提示",data.msg,true);
                }
            });
        }
        $.extend($.fn.validatebox.defaults.rules, {
            equals: {
                validator: function (value, param) {
                    return value == $(param[0]).val();
                },
                message: '两次密码输入不一致，请检查'
            }
        });
    </script>
</head>
<body class="easyui-layout">
    <%--<div data-options="region:'north',border:false" style="height:70px;padding:0px; width: 100%; overflow: hidden">--%>
        <%--<div class="main">--%>
        <%--<!--头部-->--%>
        <%--<div class="head"><span class="head-bt">自动化测试仿真平台 </span> <span--%>
                <%--class="head-text"> 当前用户：<a><shiro:principal property="name"></shiro:principal></a>&nbsp;&nbsp;<a href="#" target="_self" onclick="editPassword();">修改密码</a>|&nbsp;&nbsp;<a href="${ctx}/logout" target="_self">退出</a> </span></div>--%>
        <%--</div>--%>
    <%--</div>--%>
    <div data-options="region:'west',split:true,title:'功能菜单'" style="width:200px;">
        <div id="menuAccordion" class="easyui-accordion" >

        </div>
    </div>
    <div data-options="region:'center',border:false">
        <div id='mainTab' class="easyui-tabs" data-options="fit:true">
        </div>
    </div>
    <div data-options="region:'south',border:false" style="height:50px;padding:10px;text-align:center">Copyright
        ©中电科技（北京）有限公司
    </div>
    <div id="tabMenu" class="easyui-menu" style="width:120px;">
        <div onclick="closeTabs('other')" data-options="iconCls:'icon-remove'">关闭其它</div>
        <div onclick="closeTabs('all')" data-options="iconCls:'icon-remove'">关闭所有</div>
    </div>
</body>
<div id="password" class="easyui-dialog"
     data-options="closed:true,closable:false,modal:true,title:'首次登陆，请修改密码',
     width:'350px',height:'200px'">
    <form id="fm" class="easyui-form" method="post" style="padding:10px;">
        <input type="hidden" id="id" name="id" value="${sysUserId}"/>
        <table>
            <tr>
                <td>原密码:</td>
                <td><input name="oldPwd" class="easyui-textbox" type="password" data-options="required:true" style="width: 250px"/></td>
            </tr>
            <tr>
                <td>新密码:</td>
                <td><input id="newPwd" name="newPwd" class="easyui-textbox" type="password" data-options="required:true,validType:'length[8,20]'" style="width: 250px"/></td>
            </tr>
            <tr>
                <td>确认密码:</td>
                <td><input id="confirmPwd" name="confirmPwd" class="easyui-textbox" type="password" required="required" validType="equals['#newPwd']" style="width: 250px"/></td>
            </tr>
        </table>
        <div style="text-align:center;padding:15px 0">
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
               onclick="resetPwd();">确定</a>
        </div>
    </form>
</div>
</html>
