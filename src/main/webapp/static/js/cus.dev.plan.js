
//营销机会数据条件搜索
function searchSaleChance() {
    $("#dg").datagrid("load",{
        customerName:$("#s_customerName").val(),
        createMan:$("#s_createMan").val()
    })
}


function formatterDevResult(value) {
    if(value==0){
        return "为开发";
    }else if(value==1){
        return "开发中";
    }else if(value==2){
        return "开发成功";
    }else if(value==3){
        return "开发失败";
    }else{
        return "未知";
    }
}

function stylerDevResult(value) {
    if(value==0){
        return "background-color: orange;";
    }else if(value==1){
        return "background-color: #31b0d5";
    }else if(value==2){
        return "background-color: #00ee00;";
    }else if(value==3){
        return "background-color: red";
    }else{
        return "background-color:  #92a2ab";
    }
}


function formatterOp(value,rowData) {
    var href='javascript:openCusDevPlanTab("客户开发计划项_",'+rowData.id+')';
    if(rowData.devResult==1){
        return "<a href='"+href+"'>开发</a>";
    }else {
        return "<a href='"+href+"'>查看详情</a>";
    }
}


function openCusDevPlanTab(title,id) {
    window.parent.openTab(title+id,ctx+"/cus_dev_plan/toCusDevPlanManager?sid="+id);
}






