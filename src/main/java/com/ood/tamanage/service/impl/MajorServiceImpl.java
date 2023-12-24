package com.ood.tamanage.service.impl;

import com.ood.tamanage.mapper.MajorMapper;
import com.ood.tamanage.pojo.Course;
import com.ood.tamanage.pojo.Major;
import com.ood.tamanage.service.MajorService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {
    @Resource
    private MajorMapper majorMapper;

    @Override
    public List<Major> getMajorsList() {
        return majorMapper.getMajorsList();
    }

    @Override
    public Major getMajorById(int mid) {
        return majorMapper.getMajorById(mid);
    }

    @Override
    public Major getMajorByCode(String majorCode) {
        return majorMapper.getMajorByCode(majorCode);
    }

    @Override
    public void addMajor(Major major) {
        majorMapper.addMajor(major);
    }

    @Override
    public void updateMajor(Major major) {
        majorMapper.updateMajor(major);
    }

    @Override
    public void deleteMajor(int mid) {
        majorMapper.deleteMajor(mid);
    }

    @Override
    public List<Course> getCourses(Major major) {
        return majorMapper.getCourses(major);
    }

    @Override
    public List<Course> getCoursesByStage(Major major, int stage) {
        return majorMapper.getCoursesByStage(major, stage);
    }

    @Override
    public void addCourse(Major major, Course course1) {
        majorMapper.addCourse(major, course1);
    }

    @Override
    public int getClassesCount(Major major) {
        return majorMapper.getModulesCount(major) + majorMapper.getCoursesCount(major);
    }

    @Override
    public int getCoursesCountByStage(Major major, int stage) {
        return majorMapper.getCoursesCountByStage(major, stage);
    }
}
