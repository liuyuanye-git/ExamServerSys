package com.examsys.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.examsys.dao.GroupUserMapper;
import com.examsys.dao.TestPaperDetailMapper;
import com.examsys.dao.TestPaperMapper;
import com.examsys.model.GroupUser;
import com.examsys.model.QuestionLibrary;
import com.examsys.model.TestPaper;
import com.examsys.model.TestPaperDetail;
import com.examsys.model.entity.GroupUserEntity;
import com.examsys.model.entity.ResponseEntity;
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

    /**
     * 处理添加试卷的数据
     * @param TestpaperMap
     * @return
     */
    public List<TestPaperDetail> handleNewPaper(Map<String,Object> TestpaperMap){
        //Map<String,Object> mapRes=new HashMap<>();
        //List<QuestionLibrary> questionList=new ArrayList<>();
        List<TestPaperDetail> testPaperList=new ArrayList<>();
        TestPaper testPaper=new TestPaper();
        Date now = new Date();
        SimpleDateFormat dateFormatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        testPaper.setCreateTime(dateFormatTime.format(now));

        SimpleDateFormat dateFormatCode = new SimpleDateFormat("yyyyMMddHHmmss");

        String paper_code=dateFormatCode.format(now);
//        Calendar cal=Calendar.getInstance();
//        String paper_code=""+cal.get(Calendar.YEAR)+cal.get(Calendar.MONTH)+cal.get(Calendar.DATE)+cal.get(Calendar.HOUR_OF_DAY)
//                +cal.get(Calendar.MINUTE)+cal.get(Calendar.SECOND);

        List<Map<String,Object>> map=(List<Map<String,Object>>)TestpaperMap.get("question_list");
        testPaper.setPaperCode(paper_code);
        testPaper.setDescription(String.valueOf(TestpaperMap.get("description")));
        testPaper.setTitle(String.valueOf(TestpaperMap.get("title")));
        testPaper.setCreateUserId(Integer.valueOf(TestpaperMap.get("user_id").toString()));

        TestPaper testPaperAlready=testPaperMapper.selectByPaperCode(paper_code);
        if(testPaperAlready!=null){
            throw new RuntimeException("试卷已存在");
        }else{
            int res=testPaperMapper.insert(testPaper);
            if(res<0){
                throw new RuntimeException("数据库错误");
            }
        }

        for(int i=0;i<map.size();i++){
            //QuestionLibrary question=new QuestionLibrary();
            TestPaperDetail testpaper2=new TestPaperDetail();
            Map<String,Object> map1=map.get(i);

           // question.setId(Integer.valueOf(map1.get("id").toString()));
//            question.setType(String.valueOf(map1.get("type")));
//            question.setContent(String.valueOf(map1.get("content")));

//            question.setDescription(String.valueOf(map1.get("description")));
//
//            if( map1.get("option_list") == null || map1.get("option_list")== ""){
//                question.setOptions("");
//            }else {
//                question.setOptions(JSON.toJSONString(map1.get("option_list")));
//            }
//
//            if( map1.get("description") == null || map1.get("description")== ""){
//                question.setDescription("");
//            }else {
//                question.setDescription(String.valueOf(map1.get("description")));
//            }
//
            //question.setAnswer(JSON.toJSONString(map1.get("answer_list")));


            double score=Double.parseDouble(String.valueOf(map1.get("score")));
            testpaper2.setScore(score);
            testpaper2.setDefAnswer(map1.get("answer_list").toString());
            testpaper2.setPaperCode(paper_code);
            testpaper2.setMustOrNot(Integer.valueOf(map1.get("must_or_not").toString()));
            testpaper2.setCategoryContent(String.valueOf(map1.get("category_content")));

            testpaper2.setQuestionId(Integer.valueOf(map1.get("id").toString()));
            //questionList.add(question);
            testPaperList.add(testpaper2);
        }

        //mapRes.put("questionList",questionList);
        //mapRes.put("testPaperList",testPaperList);

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
        List<TestPaper> testPaperList = testPaperMapper.selectAll();
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


    public ResponseEntity editPaper(Map<String,Object> TestpaperMap){
        ResponseEntity responseEntity = new ResponseEntity();
        String paper_code = String.valueOf(TestpaperMap.get("paper_code"));
        List<Map<String,Object>> map=(List<Map<String,Object>>)TestpaperMap.get("question_list");
        List<Integer> quesIds = new ArrayList<>();
        List<TestPaperDetail> alreadyList=new ArrayList<>();
        for(int i=0;i<map.size();i++){

            TestPaperDetail testpaper2=new TestPaperDetail();
            Map<String,Object> map1=map.get(i);

            quesIds.add(Integer.valueOf(map1.get("id").toString()));

            double score=Double.parseDouble(String.valueOf(map1.get("score")));
            testpaper2.setScore(score);
            testpaper2.setDefAnswer(map1.get("answer_list").toString());
            testpaper2.setPaperCode(paper_code);
            testpaper2.setMustOrNot(Integer.valueOf(map1.get("must_or_not").toString()));
            testpaper2.setCategoryContent(String.valueOf(map1.get("category_content")));

            testpaper2.setQuestionId(Integer.valueOf(map1.get("id").toString()));
            //questionList.add(question);
            //testPaperList.add(testpaper2);
        }
        return responseEntity;


    }


}
