package com.examsys.controller;

import com.examsys.model.Knowledge;
import com.examsys.model.entity.QuesKnowEntity;
import com.examsys.model.entity.ResponseEntity;
import com.examsys.service.Impl.KnowledgeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/keypoint")
public class KnowledgeController {

    @Autowired
    KnowledgeServiceImpl knowledgeService;

    /**
     * 批量添加知识点
     * @param mapRes
     * @return
     */
    @PostMapping(value = "/multi")
    public ResponseEntity addNewKnowledgeByFront(@RequestBody List<Map<String,Object>> mapRes) {
        List<Knowledge> konwledgeList=knowledgeService.handleNewKnowledge(mapRes);
        ResponseEntity responseEntity=knowledgeService.addNewKnowledge(konwledgeList);
        return responseEntity;
    }


    /**
     * 添加/编辑知识点
     * @param mapRes
     * @return
     */
    @PostMapping(value = "/single")
    public ResponseEntity addOrUpdateKnowledge(@RequestBody Map<String,Object> mapRes) {
        ResponseEntity responseEntity = knowledgeService.addOrUpdateKnowledge(mapRes);
        return responseEntity;
    }


    /**
     * 获取所有知识点
     * @return
     */
    @GetMapping(value = "/all")
    public ResponseEntity getAllKnowledge() {
        ResponseEntity responseEntity=knowledgeService.getAllKnowledge();
        return responseEntity;
    }


    /**
     * 给多个题目添加知识点
     * @param mapRes
     * @return
     */
    @PostMapping(value = "/questions")
    public ResponseEntity addQuesAndKnow(@RequestBody List<Map<String,Object>> mapRes) {
        List<QuesKnowEntity> QuesKnowList=knowledgeService.handleQuesAndKnow(mapRes);
        ResponseEntity responseEntity=knowledgeService.addQuesAndKnow(QuesKnowList);
        return responseEntity;
    }


    /**
     * 根据知识点获取题目
     * @param map
     * @return
     */
    @PostMapping(value = "/questionlist")
    public ResponseEntity getQuesByKnow(@RequestBody Map<String,Object> map) {
        ResponseEntity responseEntity = knowledgeService.getQuesByKonw(map);
        return responseEntity;
    }


    /**
     * 批量删除知识点
     * @param map
     * @return
     */
    @DeleteMapping(value = "/del")
    public ResponseEntity delKnows(@RequestBody Map<String,Object> map) {
        ResponseEntity responseEntity=knowledgeService.delKnows(map);
        return responseEntity;
    }

}
