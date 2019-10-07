package com.examsys.controller;

import com.examsys.model.Group;
import com.examsys.model.entity.ResponseEntity;
import com.examsys.service.Impl.GroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by twinkleStar on 2019/9/4.
 */
@RestController
@RequestMapping("/upi/group")
public class GroupController {

    @Autowired
    GroupServiceImpl groupService;


    /**
     * 添加单个组
     * @param map
     * @return
     */
    @PostMapping("/single")
    public ResponseEntity addSingleGroup(@RequestBody Map<String,String> map) {
        Group group=new Group();
        group.setName(map.get("groupName"));
        ResponseEntity responseEntity=groupService.addSingleGroup(group);
        return responseEntity;
    }

    /**
     * 删除单个组
     * todo:暂不可用
     * @param map
     * @return
     */
    @DeleteMapping("/single")
    public ResponseEntity deleteUsers(@RequestBody Map<String,Integer> map) {
        ResponseEntity responseEntity = groupService.deleteSingleGroup(map);
        return responseEntity;
    }

    /**
     * 根据学生ID获取所在组
     * @param map
     * @return
     */
    @PostMapping("/uid")
    public ResponseEntity getGroupByUserId(@RequestBody Map<String,Integer> map) {
        ResponseEntity responseEntity=groupService.getGroupByUserId(map.get("id"));
        return responseEntity;
    }
}
