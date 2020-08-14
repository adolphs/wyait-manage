package com.wyait.manage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyait.manage.dao.DepartmentMapper;
import com.wyait.manage.dao.DoooMapper;
import com.wyait.manage.dao.SituationDetailsMapper;
import com.wyait.manage.dao.SituationMapper;
import com.wyait.manage.entity.UserRoleDTO;
import com.wyait.manage.pojo.DataDooo;
import com.wyait.manage.pojo.Department;
import com.wyait.manage.pojo.Dooo;
import com.wyait.manage.pojo.Situation;
import com.wyait.manage.utils.PageDataResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author h_baojian
 * @version 1.0
 * @date 2020/6/23 11:28
 */
@Service
public class DoooServiceImpl implements DoooService{

    private static final Logger logger = LoggerFactory
            .getLogger(DoooServiceImpl.class);

    @Autowired
    private DoooMapper doooMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private SituationMapper situationMapper;
    @Autowired
    private SituationDetailsMapper situationDetailsMapper;
    @Override
    public PageDataResult getDooos(Dooo dooo, Integer page, Integer limit) {
        PageDataResult pdr = new PageDataResult();
        PageHelper.startPage(page, limit);
        List<Dooo> doooList = doooMapper.getDooos(dooo);
        // 获取分页查询后的数据
        PageInfo<Dooo> pageInfo = new PageInfo<>(doooList);
        // 设置获取到的总记录数total：
        pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());
        for (int i=0;i<doooList.size();i++){
            Department department = departmentMapper.getDepartmentById(doooList.get(i).getDepartmentId());
            doooList.get(i).setDepartmentName(department.getDepartmentName());
        }
        pdr.setList(doooList);
        return pdr;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 30000, rollbackFor = {
            RuntimeException.class, Exception.class })
    public String setDooo(Dooo dooo) {
        if (dooo.getId() != null){
            //判断是否存在相同名称
            Dooo dooo1 = doooMapper.getDoooByNameAndId(dooo.getDoooName(),dooo.getDoooId());
            if (null != dooo1){
                return "该事项名称已经存在";
            }
            //修改
            dooo.setUpdateTime(new Date());
            doooMapper.updateDooo(dooo);
        }else{
            //判断是否存在相同名称
            Dooo dooo1 = doooMapper.getDoooByName(dooo.getDoooName());
            if (null != dooo1){
                return "该事项名称已经存在";
            }
            dooo.setNewTime(new Date());
            dooo.setUpdateTime(new Date());
            dooo.setType(0);
            doooMapper.insertDooo(dooo);
        }
        return "ok";
    }

    @Override
    public String delDooo(Integer doooId) {

        List<Situation> situation = situationMapper.getSituationById(doooId);
        for (int i=0;i<situation.size();i++){
            situationDetailsMapper.delSituationDetail(situation.get(i).getSituationId());
            situationMapper.delSituationByGoooId(doooId);
        }

        int i = doooMapper.delDooo(doooId);
        //删除其他
        if (i == 1){
            return "ok";
        }else {
            return "删除失败";
        }

    }

    @Override
    public List<Dooo> getDoooList(Integer departmentId) {
        return doooMapper.getDoooList(departmentId);
    }

    @Override
    public Dooo selectById(Integer id) {
        return doooMapper.getDoooById(id);
    }

    @Override
    public String addDoooAndData(Integer dataId, Integer doooId) {
        DataDooo dataDooo = doooMapper.getDataDooo(dataId,doooId);
        if (dataDooo != null){
            return "您选择关联的材料已和本事项关联了，请勿重复关联";
        }
        doooMapper.addDoooAndData(dataId,doooId);
        return "ok";
    }

    @Override
    public List<Dooo> fetchByName(Dooo dooo) {
        return doooMapper.fetchByName(dooo);
    }

    @Override
    public Dooo getDoooById(Integer doooId) {
        return doooMapper.getDoooById(doooId);
    }


}
