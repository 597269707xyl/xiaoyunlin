<%--page是JSP的编译指令之一，三个编译指令为page、include、taglib--%>
<%--导入外部shiro框架的filter的jar包--%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%--设定生成网页的格式为html，编码字符集为UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--引入标签库中自定义的JSP标签，JSTL是SUN提供的一套标签库--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--设置一个变量ctx，赋值为该WEB项目的根路径，若不指定scope属性，则默认为page范围。--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
<c:set var="loginError" value="<%=error%>"/>
<%--这是工具输入账号密码登陆的页面--%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>自动化测试仿真平台</title>
    <link rel="stylesheet" href="${ctx}/asset/css/login.css">
    <script type="text/javascript" src="${ctx}/asset/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/asset/js/jquery.cookie.js"></script>
    <script>
        var overdue = '${overdue}';
        if(overdue != '' && overdue == 'false'){
            alert("软件已过期!");
        }
        $(document).ready(function() {
            if ($.cookie("rember") == "true") {
                $("#rember").attr("checked", true);
                $("#username").val($.cookie("username"));
                $("#password").val($.cookie("password"));
            }
        });
        function saveUserInfo() {
            if ($("#rember").is(':checked') == true) {
                <!--如果勾选了记住我，那么就启用cookie来保存账户密码信息-->
                var userName = $("#username").val();
                var passWord = $("#password").val();
                $.cookie("rember", "true", { expires: 365 }); // 存储一个带365天期限的 cookie
                $.cookie("username", userName, { expires: 365 }); // 存储一个带365天期限的 cookie
                $.cookie("password", passWord, { expires: 365 }); // 存储一个带365天期限的 cookie
            }
            else {
                <%--设置响应的expires属性为-1可使cookie失效，让用户离开页面后就立即过期--%>
                $.cookie("rember", "false", { expires: -1 });
                $.cookie("username", '', { expires: -1 });
                $.cookie("password", '', { expires: -1 });
            }
        }
        function login(){
            saveUserInfo();
        }

        window.onload = window.onresize = function () {
            document.body.style.height = document.documentElement.clientHeight + 'px';
        };

        var _topWin = window;
        while (_topWin != _topWin.parent.window) {
            _topWin = _topWin.parent.window;
        }
        if (window != _topWin)_topWin.document.location.href = '${ctx}/main';
    </script>
</head>
<body>
<form id="form1" name="form1" method="post" action="${ctx}/login">
    <div class="login">
        <div class="login_top">
            <img src="${ctx}/asset/images/dl_logo.png" alt="">
        </div>
        <div class="login_Inp">
            <div class="login_txt">
                <input name="username" id="username" type="text" class="username" placeholder="请输入用户名">
            </div>
            <div class="login_txt">
                <input name="password" id="password" type="password" class="userpwd" placeholder="请输入密码">
            </div>
            <c:if test="${fn:contains(loginError,'CredentialsException')}">
                <div style="margin-bottom:10px;height: 17px;color: red">
                    用户名或密码不正确
                </div>
            </c:if>
            <div class="for_rem">
                <div class="rember">
                    <input id="rember" name="rember" type="checkbox"/><span>记住我</span>
                </div>
            </div>
        </div>
        <div class="login_btn">
            <input type="submit" onclick="login();" value="登 录">
        </div>
    </div>
    <div class="copy">
        <p>版权所有@中电科技（北京）有限公司</p>
    </div>
</form>

</body>
</html>
