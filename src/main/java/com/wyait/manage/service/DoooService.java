package com.wyait.manage.service;

import com.wyait.manage.pojo.Dooo;
import com.wyait.manage.utils.PageDataResult;

import java.util.List;

/**
 * @author h_baojian
 * @version 1.0
 * @date 2020/6/23 11:28
 */
public interface DoooService {
    /**
     * 分页查询事项列表
     * @param page
     * @param limit
     * @return
     */
    PageDataResult getDooos(Dooo dooo, Integer page, Integer limit);

    /**
     * 新增或修改事项
     * @param dooo
     * @return
     */
    String setDooo(Dooo dooo);

    String delDooo(Integer doooId);


    List<Dooo> getDoooList(Integer departmentId);

    Dooo selectById(Integer id);

    String addDoooAndData(Integer dataId, Integer doooId);

    /**
     * 根据多种条件查询
     * @param dooo
     * @return
     */
    List<Dooo> fetchByName(Dooo dooo);

    Dooo getDoooById(Integer doooId);
}
