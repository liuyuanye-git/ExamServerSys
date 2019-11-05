package com.examsys.dao;

import com.examsys.model.StudentPointDetail;
import com.examsys.model.entity.StuObjJudgeEntity;
import com.examsys.model.entity.StudentAnswersDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StudentPointDetailMapper {

    List<StudentAnswersDetail> selectStudentAnswers(Integer examId);

    int deleteByPrimaryKey(Integer id);

    int insert(StudentPointDetail record);

    StudentPointDetail selectByPrimaryKey(Integer id);

    StudentPointDetail selectByIds(StudentPointDetail studentPointDetail);

    List<StudentPointDetail> selectAll();

    List<StudentPointDetail> selectAllByExamId(int examId);

    int updateByPrimaryKey(StudentPointDetail record);

    int updateByIds(StudentPointDetail studentPointDetail);

    List<StuObjJudgeEntity> selectStuObjQues(Integer examId);

    int updateByPK(StudentPointDetail studentPointDetail);

    int updateStuAnswer(StudentPointDetail studentPointDetail);

    int deleteByStuId(int stuId);

}