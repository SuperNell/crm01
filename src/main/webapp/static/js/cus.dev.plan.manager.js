$(function () {
    $("#dg").edatagrid({
        url:ctx+"/cus_dev_plan/list?sid="+$("#saleChanceId").val(),
        saveUrl:ctx+"/cus_dev_plan/save?saleChanceId="+$("#saleChanceId").val(),
        updateUrl:ctx+"/cus_dev_plan/update?saleChanceId="+$("#saleChanceId").val()
        //destroyUrl:ctx+"/cus_dev_plan/delete"
    });

    if($("#devResult").val()==2||$("#devResult").val()==3){
        $("#toolbar").hide();
        $("#dg").edatagrid("disableEditing");
    }

});

//保存或更新
function saveCusDevPlan() {
    $("#dg").edatagrid("saveRow");
    $("#dg").edatagrid("load");
}


function delCusDevPlan() {
    var rows=$("#dg").edatagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自CRM","请选择待删除的数据！","warning");
        return;
    }

    var ids="ids=";
    for (var i=0;i<rows.length;i++) {
        if(i<rows.length-1){
            ids=ids+rows[i].id+"&ids=";
        }else{
            ids=ids+rows[i].id;
        }
    }
    $.messager.confirm("来自CRM","确定删除选中的记录吗？",function (r) {
        if(r){
            $.ajax({
                type:"post",
                url:ctx+"/cus_dev_plan/delete",
                data:ids,
                dataType:"json",
                success:function (data) {
                    if(data.code==200){
                        $("#dg").edatagrid("load");
                    }else{
                        $.messager.alert("来自CRM",data.msg,"error");
                    }
                }
            })
        }
    })
}


function updateSaleChanceDevResult(result) {
    $.ajax({
        type:"post",
        url:ctx+"/sale_chance/updateSaleChanceDevResult",
        data:{
            devResult:result,
            sid:$("#saleChanceId").val()
        },
        dataType:"json",
        success:function (data) {
            if(data.code==200){
                $("#toolbar").hide();
                $("#dg").edatagrid("disableEditing");
            }else{
                $.messager.alert("来自CRM",data.msg,"error");
            }
        }
    })
}








