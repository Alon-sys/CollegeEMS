package com.example.collegeems.service;

import cn.hutool.core.date.DateUtil;
import com.example.collegeems.dao.NoticeDao;
import com.example.collegeems.entity.Job;
import com.example.collegeems.entity.Notice;
import com.example.collegeems.entity.Params;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Resource
    private NoticeDao noticeDao;


    //按条件查询
    public PageInfo<Notice> findBySearch(Params params) {
        //开启分页查询
        //查询会自动查询分页数据
        PageHelper.startPage(params.getPageNum(),params.getPageSize());

        List<Notice> list = noticeDao.findBySearch(params);

        return PageInfo.of(list);
    }

    public List<Notice> findTop() {
        return noticeDao.findTop5();
    }

    public void save(Notice notice) {
        notice.setTime(DateUtil.now());
        noticeDao.save(notice);
    }


    //删除
    public void delete(Long id) {
        noticeDao.delete(id);
    }

    public void update(Notice notice) {
        noticeDao.update(notice);
    }


}
