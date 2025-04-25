package com.example.collegeems.service;

import com.example.collegeems.dao.CompanyDao;
import com.example.collegeems.entity.Company;
import com.example.collegeems.entity.Params;
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

    // 按id查询
    public Company findByUserID(Long id)
    {
        return companyDao.findByUserID(id);
    }


}
