<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="fm" method="post">

    <div class="popup">
        <input type="hidden" id="id" name="id"/>
        <table class="single">
            <tr>
                <td>项目名称:</td>
                <td><input class="easyui-textbox" id="name1" name="name"
                           data-options="required:true"/></td>
            </tr>
            <tr>
                <td>项目标识:</td>
                <td><input class="easyui-textbox" id="abb" name="abb"
                           data-options="required:true"/></td>
            </tr>
            <tr>
                <td>项目类型:</td>
                <td><input id="type" name="type" data-options="required:true"/></td>
            </tr>
            <tr>
                <td>被测系统:</td>
                <td><input class="easyui-textbox" id="testsystem" name="testsystem"
                           data-options="required:true"/></td>
            </tr>
            <tr>
                <td>依赖实例:</td>
                <td><input id="insname" name="insname" data-options="required:true"/></td>
            </tr>
            <tr>
                <td>预计截止日期:</td>
                <td><input class="easyui-datetimebox" id="endtime" name="endtime" ></td>
            </tr>

            <tr>
                <td>说明:</td>
                <td><input class="easyui-textbox" id="descript" name="descript" /></td>
            </tr>
        </table>
    </div>
    <div class="buttonBar">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="submitForm();">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
           onclick="closeWindow();">取消</a>
    </div>

</form>

<script>
    $("#insname").combobox({
        url: '${ctx}/proj/proj/insname',
        valueField: 'id',
        textField: 'name',
        panelHeight: '200',
        editable:false,
        limitToList:true
    });

    $("#type").combobox({
        url: '${ctx}/generic/dict/PROJECT_TYPE',
        valueField: 'id',
        textField: 'text',
        panelHeight: '200',
        editable:false,
        limitToList:true
    });

    function getData() {
        var formData = {};
        formData.id = $('#id').val();
        formData.endtime = $('#endtime').datebox('getValue');
        formData.name = $('#name1').textbox('getValue');
        formData.testsystem = $('#testsystem').textbox('getValue');
        formData.descript = $('#descript').textbox('getValue');
        formData.abb = $('#abb').textbox('getValue');
        formData.instanceId = $('#insname').combobox('getValue');
        formData.type = $('#type').combobox('getValue');
        return formData;
    }
    function setData(param){
        if (param.action == 'update') {
            $.ajax({
                url: "${ctx}/proj/proj/get/" + param.id,
                cache: false,
                success: function (vo) {
                    $('#fm').form('load', vo);
                    $('#type').combobox("setValue", vo.type);
                    $('#insname').combobox("setValue", vo.instance.id);
                }
            });
        }
    }
    function submitForm() {
        commonSubmitForm('${ctx}/proj/proj/new/add');
    }
</script>
