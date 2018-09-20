/**
 * Created by huangbo on 2018/7/30.
 */
var selectType;
$(function () {
    $("#testItem").combotree({
        url: '${ctx}/func/exec/tree?mark=send&caseType=t&id=-1&type=',
        valueField: 'id',
        textField: 'text',
        onSelect: function (node) {
            var type = node.type;
            selectType = type;

            $(this).tree('options').url = '${ctx}/func/exec/tree?mark=send&caseType=t&type=' + type;
            if (node.state == 'closed') {
                $(this).tree('expand', node.target);
            } else {
                $(this).tree('collapse', node.target);
            }

        },
        onExpand: function (node) {
        },
        onBeforeExpand: function (node) {
            var type = node.type;
            $(this).tree('options').url = '${ctx}/func/exec/tree?mark=send&caseType=t&type=' + type;
        },
        onLoadError: function (Error) {

        },
        onLoadSuccess: function (success) {
        }
    });
    $('#dg').datagrid({
        //fit: true,
        height: $(window).height() - 80,
        fitColumns: true,
        singleSelect: true,
        pagination: true,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
        url: '${ctx}/proj/proj/getStatistics',
        columns: [[
            {field: 'id', title: 'id', hidden: true},
            {field: 'projectName', title: '项目名称', width: 70},
            {field: 'itemName', title: '测试项名称', width: 170},
            {field: 'total', title: '总案例数', width: 70},
            {field: 'suc', title: '通过案例数', width: 70},
            {field: 'fail', title: '未通过案例数', width: 70},
            {field: 'other', title: '未执行案例数', width: 70}
        ]]
    });
});
function simSearch() {
    var testItem = $('#testItem').textbox('getValue');
    var mark = $('#mark').combobox("getValue");
    var p = {id: testItem, mark: mark, type: selectType};
    $("#dg").datagrid('load', p);

}