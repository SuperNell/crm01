package com.shsxt.crm.query;

import com.shsxt.base.BaseQuery;
import org.apache.ibatis.annotations.Param;

public class SaleChanceQuery extends BaseQuery {
    private String customerName;//客户名称
    private String createMan;//创建人
    private Integer state;//分配状态 0-未分配 1
    private Integer assignMan;

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String creatMan) {
        this.createMan = creatMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
