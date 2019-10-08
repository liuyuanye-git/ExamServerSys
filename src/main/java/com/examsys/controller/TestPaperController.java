package com.examsys.controller;

import com.examsys.model.QuestionLibrary;
import com.examsys.model.TestPaperDetail;
import com.examsys.model.entity.ResponseEntity;
import com.examsys.service.Impl.TestPaperServiceImpl;
import com.examsys.service.Impl.QuestionLibraryServiceImpl;
import com.examsys.util.ExcelAnalysisUtil;
import com.examsys.util.ExcelTemplateUtil;
import com.examsys.util.OtherUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by twinkleStar on 2019/9/22.
 */

@RestController
@RequestMapping("/paper")
public class TestPaperController {

    @Autowired
    ExcelAnalysisUtil excelAnalysisUtil;
    @Autowired
    TestPaperServiceImpl testPaperService;
    @Autowired
    QuestionLibraryServiceImpl questionLibraryService;

    @Autowired
    ExcelTemplateUtil excelUtil;
    @Autowired
    OtherUtil otherUtil;

//    /**
//     * 通过上传模板excel的方式批量添加试题
//     * todo:暂不可用
//     * @param multipartFiles
//     * @return
//     */
//    @PostMapping(value = "/excel")
//    @Transactional
//    public ResponseEntity addNewPaperByExcel(
//            @RequestParam( value="files[]",required=false)MultipartFile[] multipartFiles)throws IllegalStateException, IOException {
//        MultipartFile file=multipartFiles[0];
//        Map<String,Object> map=excelAnalysisUtil.addNewPaper(file);
//        List<QuestionLibrary> questioninfoList=(List<QuestionLibrary>)map.get("questionInfo");
//        List<TestPaper> paperQuestionList=(List<TestPaper>)map.get("paperQuestion");
//        List<TestPaper> paperQuestion=questionLibraryService.addNewQuestions(questioninfoList,paperQuestionList);
//        ResponseEntity responseEntity=testPaperService.addNewPaper(paperQuestion);
//        return responseEntity;
//    }





//    /**
//     * 下载批量导入的excel模板
//     * @param response
//     * @throws Exception
//     */
//    @GetMapping("/template")
//    public void userTemplate(HttpServletResponse response) throws Exception {
//        String fileName = "添加试卷模板.xls";
//        HSSFWorkbook wb=excelUtil.addPaperTemplate(); //调用excelUtil生成excel
//        try {
//            otherUtil.setResponseHeader(response, fileName,wb);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 添加试卷
     * @param mapRes
     * @return
     */
    @PostMapping(value = "/new")
    @Transactional
    public ResponseEntity addNewPaperByFront(@RequestBody Map<String,Object> mapRes) {

        Map<String,Object> map=testPaperService.handleNewPaper(mapRes);
        List<QuestionLibrary> questionList=(List<QuestionLibrary>)map.get("questionList");
        List<TestPaperDetail> testPaperList=(List<TestPaperDetail>)map.get("testPaperList");
        List<TestPaperDetail> paperQuestion=questionLibraryService.addNewQuestions(questionList,testPaperList);
        ResponseEntity responseEntity=testPaperService.addNewPaper(paperQuestion);
        return responseEntity;

    }


    /**
     * 获取所有试卷
     * @return
     */
    @GetMapping(value = "/all")
    public ResponseEntity getAllPapers() {
        ResponseEntity responseEntity=testPaperService.getAllPapers();
        return responseEntity;
    }

}
