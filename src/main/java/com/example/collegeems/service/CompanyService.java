package com.example.collegeems.service;

import com.example.collegeems.dao.CompanyDao;
import com.example.collegeems.entity.Company;
import com.example.collegeems.entity.Params;
import com.example.collegeems.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Resource
    private CompanyDao companyDao;
    // 保存
    public void save(Company company)
    {
        if (company.getUserId()==null) {
            companyDao.save(company);
        }   //进行重复性判断
        Company userId = companyDao.findByUserID(company.getUserId());
        if(userId!=null)
        {
            throw new CustomException("您以创建公司,如需修改请点击编辑!");

        }
        companyDao.save(company);
    }

    // 删除
    public void delete(Long id)
    {
        companyDao.delete(id);
    }

    // 更新
    public void update(Company company)
    {
        companyDao.update(company);
    }



    //按条件查询
    public  PageInfo<Company> findBySearch(Params params) {
        //开启分页查询
        //查询会自动查询分页数据
        PageHelper.startPage(params.getPageNum(),params.getPageSize());

        List<Company> list = companyDao.findBySearch(params);

        return PageInfo.of(list);
    }

    //查询所有
    public List<Company> findAll()
    {
        return companyDao.findAll();
    }

    // 按id查询
    public Company findByUserID(Long id)
    {
        return companyDao.findByUserID(id);
    }


}
