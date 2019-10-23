package com.examsys.service.Impl;

import com.examsys.dao.StudentPointDetailMapper;
import com.examsys.dao.StudentPointMapper;
import com.examsys.dao.TestPaperDetailMapper;
import com.examsys.model.StudentPoint;
import com.examsys.model.StudentPointDetail;
import com.examsys.model.entity.ResponseEntity;
import com.examsys.model.entity.StudentAnswersDetail;
import com.examsys.model.entity.TestPaperListEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Repository
public class StudentPointDetailServiceImpl {

    @Autowired
    StudentPointDetailMapper studentPointDetailMapper;
    @Autowired
    StudentPointMapper studentPointMapper;
    @Autowired
    TestPaperDetailMapper testPaperDetailMapper;

    public ResponseEntity getAllStudentAnswers(int examId){
        ResponseEntity responseEntity = new ResponseEntity();
        List<StudentAnswersDetail> studentAnswersDetails = studentPointDetailMapper.selectStudentAnswers(examId);
        if(studentAnswersDetails != null || studentAnswersDetails.size()!=0){
            responseEntity.setData(studentAnswersDetails);
            responseEntity.setMsg("查询成功");
            responseEntity.setStatus(200);
        }else {
            responseEntity.setStatus(-1);
            responseEntity.setMsg("不存在值");
        }
        return responseEntity;
    }

    /**
     * todo:需要设置回滚，别忘了
     * 老师判完题，总分要设置下
     * @param map
     * @return
     */
    public ResponseEntity addTeacherPoint(Map<String,Object> map){
        ResponseEntity responseEntity = new ResponseEntity();

        String paper_code = String.valueOf(map.get("paper_code"));
        int exam_id = Integer.valueOf(map.get("exam_id").toString());
        List<Map<String,Object>> student_answers_detail = (List<Map<String,Object>>)map.get("student_answers_detail");

        for(int i=0;i<student_answers_detail.size();i++){

            Map<String,Object> stuMap = student_answers_detail.get(i);
            int student_id = Integer.valueOf(stuMap.get("student_id").toString());
            Map<String,Object> paper_status =(Map<String,Object>)stuMap.get("paper_status");

            List<Map<String,Object>> subMap = (List<Map<String,Object>>)paper_status.get("subjective_answers");
            int flag = 1;
            double totalPoint = 0.0;

            for(int j=0;j<subMap.size();j++){
                Map<String,Object> quesMap = subMap.get(j);
                int id = Integer.valueOf(quesMap.get("id").toString());
                double point = Double.valueOf(quesMap.get("point").toString());

                StudentPointDetail studentPointDetail = new StudentPointDetail();
                studentPointDetail.setExamId(exam_id);
                studentPointDetail.setPaperCode(paper_code);
                studentPointDetail.setStudentId(student_id);
                studentPointDetail.setQuestionId(id);

                if(point == -1.0){
                    flag = 0;
                    studentPointDetail.setQuestionStatus(0);
                }else{
                    totalPoint = totalPoint + point;
                    studentPointDetail.setQuestionStatus(1);
                    studentPointDetail.setStudentPoint(point);
                }

                int res = studentPointDetailMapper.updateByIds(studentPointDetail);
                if(res<0){
                    throw new RuntimeException("数据库更新失败");
                }

            }

            //todo:总分没统计
            if(flag == 1){
                StudentPoint studentPointTmp = new StudentPoint();
                studentPointTmp.setExamId(exam_id);
                studentPointTmp.setStudentId(student_id);
                StudentPoint studentPoint = studentPointMapper.selectByIds(studentPointTmp);

//                studentPoint.setStudentId(student_id);
//                studentPoint.setExamId(exam_id);
                studentPoint.setSubjectiveStatus(1);
                studentPoint.setSubjectiveGrade(totalPoint);
                if(studentPoint.getObjectiveGrade() == null){
                    studentPoint.setStudentTotalPoint(totalPoint+0.0);
                }else{
                    studentPoint.setStudentTotalPoint(totalPoint+studentPoint.getObjectiveGrade());
                }

                int res2 = studentPointMapper.updateSubStatus(studentPoint);
                if(res2<0){
                    throw new RuntimeException("数据库出错");
                }
            }

        }
        responseEntity.setStatus(200);
        return responseEntity;
    }



    public ResponseEntity getStuExamPaper(String paper_code){
        TestPaperListEntity testPaperList = testPaperDetailMapper.selectStuPaper(paper_code);
        ResponseEntity responseEntity=new ResponseEntity();
        if(testPaperList==null){
            responseEntity.setStatus(-1);
            responseEntity.setMsg("查询失败,请检查");
        } else {
            responseEntity.setStatus(200);
            responseEntity.setData(testPaperList);
        }
        return responseEntity;
    }

}
