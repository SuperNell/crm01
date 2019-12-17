package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.query.CustomerOrderQuery;
import com.shsxt.crm.service.CustomerOrderService;
import com.shsxt.crm.vo.CustomerOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("customer_order")
public class CustomerOrderController extends BaseController {

    @Autowired
    private CustomerOrderService customerOrderService;


    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerOrdersByParams(CustomerOrderQuery customerOrderQuery, @RequestParam(defaultValue = "1") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer rows){
        customerOrderQuery.setPageNum(page);
        customerOrderQuery.setPageSize(rows);
        return customerOrderService.queryByParamsForDataGrid(customerOrderQuery);
    }


}
