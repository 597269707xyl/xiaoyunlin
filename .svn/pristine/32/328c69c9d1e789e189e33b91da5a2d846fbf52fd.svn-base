function topWindowJump(url) {
    var _topWin = window;
    while (_topWin != _topWin.parent.window) {
        _topWin = _topWin.parent.window;
    }
    if (window != _topWin)_topWin.document.location.href = url;
}
//绑定全局的ajax 设置。ajax请求时，如果session过期，则整个页面跳转到首页
(function () {
    if (window.$) {
        $(document).ajaxComplete(function (event, xhr, settings) {
            var sessionStatus = xhr.getResponseHeader("sessionStatus");
            if (sessionStatus === "timeout") {
                var curWwwPath=window.document.location.href;
                //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
                var pathName=window.document.location.pathname;
                var pos=curWwwPath.indexOf(pathName);
                //获取主机地址，如： http://localhost:8083
                var localhostPaht=curWwwPath.substring(0,pos);
                //获取带"/"的项目名，如：/uimcardprj
                var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
                var path = localhostPaht+projectName;
                console.log('session超时，重定向url：'+path);
                topWindowJump(path+"/main");
                //topWindowJump("/main");
            }
        });
    }
})();
$(function () {
    $('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
});
function showWindow(title, href, width, height, modal, minimizable, maximizable) {
    $('#myWindow').window({
        title: title,
        width: width === undefined ? 600 : width,
        height: height === undefined ? 400 : height,
        //content: '<iframe scrolling="yes" frameborder="0"  src="' + href + '" style="width:100%;height:98%;"></iframe>',
        href: href === undefined ? null : href,
        modal: modal === undefined ? true : modal,
        minimizable: minimizable === undefined ? false : minimizable,
        maximizable: maximizable === undefined ? false : maximizable,
        shadow: false,
        cache: false,
        closed: false,
        collapsible: false,
        resizable: true,
        loadingMessage: '正在加载数据，请稍等......'
    });
    //$('#myWindow').window('open');
}
function commonShowWindow(param,title, href, width, height, modal, minimizable, maximizable) {
    $('#myWindow').window({
        title: title,
        width: width === undefined ? 600 : width,
        height: height === undefined ? 400 : height,
        href: href === undefined ? null : href,
        modal: modal === undefined ? true : modal,
        minimizable: minimizable === undefined ? false : minimizable,
        maximizable: maximizable === undefined ? false : maximizable,
        shadow: false,
        cache: false,
        closed: false,
        collapsible: false,
        resizable: true,
        loadingMessage: '正在加载数据，请稍等......',
        onLoad:function(){
            if (param != null && param.action == 'update'){
                setData(param);
            }
        }
    });
}
function closeWindow() {
    $('#myWindow').window('close');
}
function showMsg(title, msg, isAlert) {
    if (isAlert !== undefined && isAlert) {
        $.messager.alert(title, msg);
    } else {
        $.messager.show({
            title: title,
            msg: msg,
            showType: 'show'
        });
    }
}
function showConfirm(title, msg, callback) {
    $.messager.confirm(title, msg, function (r) {
        if (r) {
            if (jQuery.isFunction(callback))
                callback.call();
        }
    });
}
/*function commonSubmitForm(url) {
    $('#fm').form('submit', {
        url: url === undefined ? "/sys/common" : url,
        onSubmit: function () {
            var flag = $(this).form('validate');
            return flag
        },
        success: function (data) {
            data = $.parseJSON(data);
            if (data.success) {
                if (window !== undefined) {
                    if ($.isFunction(window.reloadParent)) {
                        reloadParent.call();
                    } else {
                        $("#dg").datagrid('reload');
                    }
                }
                closeWindow();
            } else {
                showMsg('温馨提示', data.msg,true);
            }
        }
    });
}*/
function commonSubmitForm(url){
    var form = $('#fm');
    if ($.isFunction(window.validateForm)){
        var flag = validateForm.call();
        if (!flag) return;
    }else {
        var flag = form.form('validate');
        if (!flag) return;
    }
    var formData;
    if ($.isFunction(window.getData)){
        formData = getData.call();
    }else {
        formData = form.serialize();
    }
    $.messager.progress();
    $.ajax({
        url: url,
        type: 'post',
        data: formData,
        cache: false,
        success: function (data) {
            $.messager.progress('close');
            if (data.success) {
                if (window !== undefined) {
                    if ($.isFunction(window.reloadParent)) {
                        reloadParent.call();
                    } else {
                        $("#dg").datagrid('reload');
                    }
                }
                closeWindow();
            } else {
                showMsg('温馨提示', data.msg,true);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.responseText);
            closeWindow();
        }
    });
}