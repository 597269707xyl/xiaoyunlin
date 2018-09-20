
function Socket(url,callback) {
    var socket = new WebSocket(url);
    socket.onopen = function(event){
        console.log('连接建立');
    }

    socket.onmessage = function(event) {
        callback(event);
    }

    socket.onclose = function(event) {
        console.log('连接关闭');
    }

    socket.onerror = function(event) {
        console.log('推送传输错误');
        socket.close();
        alert('WebSocket连接已断开，这将影响手动应答，请重新刷新页面。');
    }
    return socket;
}

function messageHandle(event) {
    var jsonStr = event.data;
    console.log('收到推送信息：'+jsonStr);
    var data = JSON.parse(jsonStr);
    if (data.messageType == 'self'||data.messageType == 'jh'){
        var id = data.testTaskMsgDataId;
        var msgType = data.messageType;
        //creatDialog('手动回复','http://'+window.location.host+'/pub/reply/reply',380,260);
        var iframeId = randomString(10);
        index = index+1;
        iframeId = iframeId + index;
        var w = $('<div/>').dialog({
            title:'手动应答',
            modal:false,
            closable:true,
            collapsible: false,
            resizable:true,
            width: 580,
            height: 430,
            //href: 'http://'+window.location.host+'/pub/reply/reply'
            //content:'<iframe scrolling="auto" id="myIframe" frameborder="0"  src="'+getRootPath()+'/pub/reply/reply?messageType='+msgType+'&id='+id+'" style="width:100%;height:100%;">'
            //content:'<iframe scrolling="auto" id="'+iframeId+'" frameborder="0"  src="'+'http://localhost:8099/pub/reply/reply?messageType='+msgType+'&id='+id+'&iframeId='+iframeId+'" style="width:100%;height:100%;">'
            content:'<iframe scrolling="auto" id="'+iframeId+'" frameborder="0"  src="'+getRootPath()+'/pub/reply/reply?messageType='+msgType+'&id='+id+'&iframeId='+iframeId+'" style="width:100%;height:99%;">'
        });
        map.put(iframeId,w);
    }
    if (data.messageType == 'xml'){
        var id = data.testTaskMsgDataId;
        var msgType = data.messageType;
        var isTrans = data.isTrans;
        var iframeId = randomString(10);
        index = index+1;
        iframeId = iframeId + index;
        var w = $('<div/>').dialog({
            title:'手动应答',
            modal:false,
            closable:true,
            collapsible: false,
            resizable:true,
            width: 700,
            height: 450,
            content:'<iframe scrolling="auto" id="'+iframeId+'" frameborder="0"  src="'+getRootPath()+'/pub/reply/reply?messageType='+msgType+'&id='+id+'&isTrans='+isTrans+'&iframeId='+iframeId+'" style="width:100%;height:99%;">'
        });
        map.put(iframeId,w);
    }
}

Array.prototype.remove = function(s) {
    for (var i = 0; i < this.length; i++) {
        if (s == this[i])
            this.splice(i, 1);
    }
}

function Map() {
    /** 存放键的数组(遍历用到) */
    this.keys = new Array();
    /** 存放数据 */
    this.data = new Object();

    /**
     * 放入一个键值对
     * @param {String} key
     * @param {Object} value
     */
    this.put = function(key, value) {
        if(this.data[key] == null){
            this.keys.push(key);
        }
        this.data[key] = value;
    };

    /**
     * 获取某键对应的值
     * @param {String} key
     * @return {Object} value
     */
    this.get = function(key) {
        return this.data[key];
    };

    /**
     * 删除一个键值对
     * @param {String} key
     */
    this.remove = function(key) {
        this.keys.remove(key);
        this.data[key] = null;
    };

    /**
     * 遍历Map,执行处理函数
     *
     * @param {Function} 回调函数 function(key,value,index){..}
     */
    this.each = function(fn){
        if(typeof fn != 'function'){
            return;
        }
        var len = this.keys.length;
        for(var i=0;i<len;i++){
            var k = this.keys[i];
            fn(k,this.data[k],i);
        }
    };

    /**
     * 获取键值数组(类似Java的entrySet())
     * @return 键值对象{key,value}的数组
     */
    this.entrys = function() {
        var len = this.keys.length;
        var entrys = new Array(len);
        for (var i = 0; i < len; i++) {
            entrys[i] = {
                key : this.keys[i],
                value : this.data[i]
            };
        }
        return entrys;
    };

    /**
     * 判断Map是否为空
     */
    this.isEmpty = function() {
        return this.keys.length == 0;
    };

    /**
     * 获取键值对数量
     */
    this.size = function(){
        return this.keys.length;
    };

    /**
     * 重写toString
     */
    this.toString = function(){
        var s = "{";
        for(var i=0;i<this.keys.length;i++,s+=','){
            var k = this.keys[i];
            s += k+"="+this.data[k];
        }
        s+="}";
        return s;
    };
}

function coloseWindow(iframeId,msg){
    if (msg !== undefined ){
        $.messager.alert('提示', msg);
    }
    map.get(iframeId).dialog("close");
    map.remove(iframeId);

}

function randomString(len) {
    len = len || 32;
    var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
    var maxPos = $chars.length;
    var pwd = '';
    for (i = 0; i < len; i++) {
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
    }
    return pwd;
}

function creatDialog(title,url,Dialog_w,Dialog_h){

    var div_id=randomString(5);
    var iframe_id=randomString(6);
    if (Dialog_w==null){
        Dialog_w=500;
    }else{
        if (Dialog_w>=$(window).width()){
            Dialog_w=$(window).width()-50;
        }
    };
    if (Dialog_h==null){
        Dialog_h=300;
    }else{
        if (Dialog_h>=$(window).height()){
            Dialog_h=$(window).height()-50;
        }
    };


    var htmlcontent='<div id=\"'+div_id+'\" class=\"easyui-dialog\" closed=\"true\" style=\"width:'+Dialog_w+'px;height:'+Dialog_h+'px;padding:10px;\">';
    htmlcontent=htmlcontent+'<iframe scrolling=\"no\" id=\"'+iframe_id+'\" frameborder=\"0\"  src=\"'+url+'\" style=\"width:100%;height:99%;\"></iframe>';
    htmlcontent=htmlcontent+'</div>';
    $(document.body).append(htmlcontent);
    div_id='#'+div_id;
    //$.parser.parse($(div_id).parent()); 这一句是直接生成。
    $(div_id).dialog({
        closable : true,
        title : title,
        shadow : true,
        modal : false,
        cache : false
    });

}

function getRootPath(){
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath=window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPaht=curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return(localhostPaht+projectName);
}


var map = new Map();
var index = 1;
