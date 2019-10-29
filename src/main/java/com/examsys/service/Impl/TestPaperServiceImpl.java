package com.examsys.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.examsys.dao.ExamMapper;
import com.examsys.dao.GroupUserMapper;
import com.examsys.dao.TestPaperDetailMapper;
import com.examsys.dao.TestPaperMapper;
import com.examsys.model.*;
import com.examsys.model.entity.GroupUserEntity;
import com.examsys.model.entity.ResponseEntity;
import com.examsys.model.entity.TestPaperAdminEntity;
import com.examsys.model.entity.TestPaperListEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by twinkleStar on 2019/9/22.
 */

@Service
@Repository
public class TestPaperServiceImpl {

    @Autowired
    TestPaperDetailMapper testPaperDetailMapper;

    @Autowired
    TestPaperMapper testPaperMapper;

    @Autowired
    GroupUserMapper groupUserMapper;

    @Autowired
    ExamMapper examMapper;


    /**
     * 处理添加试卷的数据
     * @param TestpaperMap
     * @return
     */
    public List<TestPaperDetail> handleNewPaper(Map<String,Object> TestpaperMap){

        TestPaper testPaper=new TestPaper();
        Date now = new Date();
        SimpleDateFormat dateFormatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormatCode = new SimpleDateFormat("yyyyMMddHHmmss");



        testPaper.setDescription(String.valueOf(TestpaperMap.get("description")));
        testPaper.setTitle(String.valueOf(TestpaperMap.get("title")));
        testPaper.setCreateUserId(Integer.valueOf(TestpaperMap.get("user_id").toString()));

        String paper_code;
        if (TestpaperMap.get("paper_code") == null || TestpaperMap.get("paper_code") == "") {
            testPaper.setCreateTime(dateFormatTime.format(now));
            paper_code=dateFormatCode.format(now);
            testPaper.setPaperCode(paper_code);
            int res=testPaperMapper.insert(testPaper);
            if(res<0){
                throw new RuntimeException("数据库错误");
            }
        }else {
            paper_code = String.valueOf(TestpaperMap.get("paper_code"));
            testPaper.setPaperCode(paper_code);
            testPaper.setLastModifiedTime(dateFormatTime.format(now));
            int res = testPaperMapper.updateByPaperCode(testPaper);
            if(res<0){
                throw new RuntimeException("数据库错误");
            }
        }



        List<TestPaperDetail> testPaperList=new ArrayList<>();
        List<Map<String,Object>> map=(List<Map<String,Object>>)TestpaperMap.get("question_list");
        for(int i=0;i<map.size();i++){
            TestPaperDetail testpaper2=new TestPaperDetail();
            Map<String,Object> map1=map.get(i);

            double score=Double.parseDouble(String.valueOf(map1.get("score")));
            testpaper2.setScore(score);
            testpaper2.setDefAnswer(JSON.toJSONString(map1.get("answer_list")));
            testpaper2.setPaperCode(paper_code);
            testpaper2.setMustOrNot(Integer.valueOf(map1.get("must_or_not").toString()));
            testpaper2.setCategoryContent(String.valueOf(map1.get("category_content")));

            testpaper2.setQuestionId(Integer.valueOf(map1.get("id").toString()));
            testPaperList.add(testpaper2);
        }



        return testPaperList;
    }


    /**
     * 添加试卷
     * @param testPaperList
     * @return
     */
    public ResponseEntity addNewPaper(List<TestPaperDetail> testPaperList){
        ResponseEntity responseEntity=new ResponseEntity();
        int length=testPaperList.size();

        for(int i=0;i<length;i++){
            TestPaperDetail testPaper=testPaperList.get(i);
            int res=testPaperDetailMapper.insert(testPaper);
            if(res<0){
                responseEntity.setStatus(-1);
                return responseEntity;
            }
        }
        responseEntity.setStatus(200);
        return responseEntity;
    }


    public void deletePaper(List<TestPaperDetail> testPaperList){
        String paperCode = testPaperList.get(0).getPaperCode();
        int res = testPaperDetailMapper.deleteByPaperCode(paperCode);
        if(res<0){
            throw new RuntimeException("数据库错误");
        }

    }


    /**
     * 获取单个试卷详情
     * @return
     */
    public ResponseEntity getTestPaperDetail(String paper_code){
        TestPaperListEntity testPaperList = testPaperDetailMapper.selectPapers(paper_code);
        ResponseEntity responseEntity=new ResponseEntity();
        if(testPaperList==null){
            responseEntity.setStatus(-1);
            responseEntity.setMsg("查询失败");
        } else {
            responseEntity.setStatus(200);
            responseEntity.setData(testPaperList);
        }
        return responseEntity;
    }


    /**
     * 获取试卷列表
     * @return
     */
    public ResponseEntity getAllPaperList(){
        List<TestPaperAdminEntity>  testPaperList = testPaperMapper.selectAllWithAdmin();
        ResponseEntity responseEntity=new ResponseEntity();
        if(testPaperList==null | testPaperList.size()==0){
            responseEntity.setStatus(-1);
            responseEntity.setMsg("不存在试卷");
        } else {
            responseEntity.setStatus(200);
            responseEntity.setData(testPaperList);
        }
        return responseEntity;
    }


    /**
     * 获取试卷列表,
     * @return
     */
    public ResponseEntity getPaperListByAdmin(Map<String,Object> mapRes){
        List<TestPaper> testPaperList = testPaperMapper.selectByAdminId(Integer.valueOf(mapRes.get("admin_id").toString()));
        ResponseEntity responseEntity=new ResponseEntity();
        if(testPaperList==null | testPaperList.size()==0){
            responseEntity.setStatus(-1);
            responseEntity.setMsg("该管理员名下没有试卷");
        } else {
            responseEntity.setStatus(200);
            responseEntity.setData(testPaperList);
        }
        return responseEntity;
    }

    /**
     * 获取考生列表
     * @return
     */
    public ResponseEntity getStudent(Integer exam_id){
        List<GroupUserEntity> StudentList = groupUserMapper.selectStudent(exam_id);
        ResponseEntity responseEntity=new ResponseEntity();
        if(StudentList==null | StudentList.size()==0){
            responseEntity.setStatus(-1);
            responseEntity.setMsg("没有成员信息");
        } else {
            responseEntity.setStatus(200);
            responseEntity.setMsg("获取成员信息成功");
            responseEntity.setData(StudentList);
        }
        return responseEntity;
    }


    public ResponseEntity delByPaperCode(Map<String,Object> map){
        ResponseEntity responseEntity = new ResponseEntity();
        List<String> paperCodes = (List<String>)map.get("paper_code");

        for(int i=0;i<paperCodes.size();i++){
            String paperCode = paperCodes.get(i);
            List<Exam> examList = examMapper.selectByPaperCode(paperCode);
            if(examList == null || examList.size()==0){
                int res = testPaperMapper.deleteByPaperCode(paperCode);
                if(res<0){
                    throw new RuntimeException("数据库错误");
                }
                int res2 = testPaperDetailMapper.deleteByPaperCode(paperCode);
                if(res2<0){
                    throw new RuntimeException("数据库错误");
                }
            }else{
                responseEntity.setStatus(-1);
                responseEntity.setMsg("该试卷已与考试关联，请更换考试关联的试卷再删除");
                return responseEntity;
            }
        }

        responseEntity.setMsg("删除成功");
        responseEntity.setStatus(200);
        return responseEntity;
    }


}
