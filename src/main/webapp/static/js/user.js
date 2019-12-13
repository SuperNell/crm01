function searchUser() {
    $("#dg").datagrid("load",{
        uname:$("#s_userName").val(),
        email:$("#s_email").val(),
        phone:$("#s_phone").val()
    })
}

$(function () {
    $("#dlg").dialog({
        onClose:function () {
            // 清空表单数据
            $("#userName").val("");
            $("#email").val("");
            $("#phone").val("");
            $("#trueName").val("");
            $("#id").val("");
            $("#cc").combobox("setValue","");
        }
    })
});

function openUserAddDialog() {
    $("#dlg").dialog("open").dialog("setTitle","用户添加");
}


function closeUserDialog() {
    $("#dlg").dialog("close");
}


function saveOrUpdateUser() {
    var url=ctx+"/user/save";
    if(!isEmpty($("#id").val())){
        url=ctx+"/user/update";
    }
    $("#fm").form("submit",{
        url:url,
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        success:function (data) {
            data=JSON.parse(data);
            if(data.code==200){
                closeUserDialog();
                //刷新表格
                searchUser();
                //清空表单数据  通过对话框关闭的监听事件
            }else{
                $.messager.alert("来自CRM",data.msg,"error");
            }
        }
    })
}


function openUserModifyDialog() {
    var rows=$("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自CRM","请选择待修改的数据！","warning");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自CRM","暂不支持批量修改！","warning");
        return;
    }


    if(!(isEmpty(rows[0].roleIdStr))){
        rows[0].roleIds=rows[0].roleIdStr.split(",");
    }
    $("#fm").form("load",rows[0]);

    $("#dlg").dialog("open").dialog("setTitle","用户更新");
}


function deleteUser() {
    var rows=$("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自CRM","请选择待删除的数据！","warning");
        return;
    }

    if(rows.length>1){
        $.messager.alert("来自CRM","暂不支持批量删除！","warning");
        return;
    }


    $.messager.confirm("来自CRM","确定删除选中的记录吗？",function (r) {
        if(r){
            $.ajax({
                type:"post",
                url:ctx+"/user/delete",
                data:{
                    userId:rows[0].id
                },
                dataType:"json",
                success:function (data) {
                    if(data.code==200){
                        searchUser();
                    }else{
                        $.messager.alert("来自CRM",data.msg,"error");
                    }
                }
            })
        }
    })
}
