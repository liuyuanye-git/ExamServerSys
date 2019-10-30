package com.examsys.controller;

import com.examsys.model.QuestionLibrary;
import com.examsys.model.entity.ResponseEntity;
import com.examsys.service.Impl.QuestionLibraryServiceImpl;
import com.examsys.util.ExcelAnalysisUtil;
import com.examsys.util.ExcelTemplateUtil;
import com.examsys.util.OtherUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by twinkleStar on 2019/9/22.
 */

@RestController
@RequestMapping("/question")
public class QuestionLibraryController {

    @Autowired
    ExcelAnalysisUtil excelAnalysisUtil;

    @Autowired
    QuestionLibraryServiceImpl questionLibraryService;

    @Autowired
    ExcelTemplateUtil excelUtil;
    @Autowired
    OtherUtil otherUtil;

    /**
     * 批量添加题目
     * @param mapRes
     * @return
     */
    @PostMapping(value = "/multi")
    @Transactional
    public ResponseEntity addNewQusetionsByFront(@RequestBody List<Map<String,Object>> mapRes) {
        List<QuestionLibrary> questionList=questionLibraryService.handleNewQuestions(mapRes);
        ResponseEntity responseEntity=questionLibraryService.addNewQuestions(questionList);
        return responseEntity;
    }


    @PostMapping(value = "/single")
    @Transactional
    public ResponseEntity addSingleQusetionsByFront(@RequestBody Map<String,Object> mapRes) {
        ResponseEntity responseEntity = questionLibraryService.addSingleQuestion(mapRes);
        return responseEntity;
    }


    /**
     * 获取所有题目
     * @return
     */
    @GetMapping(value = "/all")
    public ResponseEntity getAllQuestion() {
        ResponseEntity responseEntity=questionLibraryService.getAllQuestion();
        return responseEntity;
    }

    /**
     * 获取所有题目
     * @return
     */
    @PostMapping(value = "/filter")
    public ResponseEntity getQuestionsByFlitEr(@RequestBody Map<String,Object> map) {
        ResponseEntity responseEntity=questionLibraryService.getQuestionsByFilter(map);
        return responseEntity;
    }



    @DeleteMapping(value = "/del")
    public ResponseEntity delQues(@RequestBody Map<String,Object> map) {
        ResponseEntity responseEntity=questionLibraryService.delQues(map);
        return responseEntity;
    }


//    @PostMapping(value = "/excel")
//    @Transactional
//    public ResponseEntity addNewQuestionByExcel(
//            @RequestParam( value="files[]",required=false)MultipartFile[] multipartFiles)throws IllegalStateException, IOException {
//        MultipartFile file=multipartFiles[0];
//        List<Questioninfo> questioninfoList=excelAnalysisUtil.addNewQuestion(file);
//        ResponseEntity responseEntity=questioninfoService.addNewQuestions(questioninfoList);
//        return responseEntity;
//    }




//    @GetMapping("/template")
//    public void userTemplate(HttpServletResponse response) throws Exception {
//        String fileName = "添加题库模板.xls";
//        HSSFWorkbook wb=excelUtil.addQuestionsTemplate(); //调用excelUtil生成excel
//        try {
//            otherUtil.setResponseHeader(response, fileName);
//            OutputStream os = response.getOutputStream();
//            wb.write(os);
//            os.flush();
//            os.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
