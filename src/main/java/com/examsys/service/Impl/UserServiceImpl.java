package com.examsys.service.Impl;

import com.examsys.dao.GroupUserMapper;
import com.examsys.dao.UserMapper;
import com.examsys.model.GroupUser;
import com.examsys.model.User;
import com.examsys.model.entity.ResponseEntity;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by twinkleStar on 2019/8/26.
 */
@Service
@Repository
public class UserServiceImpl{

    @Autowired
    UserMapper userMapper;
    @Autowired
    GroupUserMapper groupUserMapper;

    /**
     * 用户登陆
     * @param username
     * @param password
     * @return
     */
    public ResponseEntity userLogin(String username,String password){
        User user=userMapper.selectByUsername(username);
        ResponseEntity responseEntity=new ResponseEntity();
        if(user==null){
            responseEntity.setStatus(-1);
            responseEntity.setMsg("该用户不存在");
        }else if(!password.equals(user.getPassword())){
            responseEntity.setStatus(-1);
            responseEntity.setMsg("用户名或密码错误");
        }else{
            responseEntity.setStatus(200);
            responseEntity.setMsg("查询成功");
            responseEntity.setData(user);
        }
        return responseEntity;
    }


    /**
     * 通过模板批量导入用户
     * todo:重复用户的判定
     * @param multipartFile
     * @return
     */
    public ResponseEntity addNewUsers(MultipartFile multipartFile)throws IllegalStateException, IOException {

        ResponseEntity responseEntity=new ResponseEntity();
        InputStream inputStream = multipartFile.getInputStream();
        int flag=0;
        List<User> userList=new ArrayList<>();
        try{
            Workbook wb = WorkbookFactory.create(inputStream);
            Sheet sheet = wb.getSheetAt(0);
            //获取最大行
            int maxRowNum = sheet.getPhysicalNumberOfRows();

            String[] name=new String[maxRowNum-3];
            int[] password=new int[maxRowNum-3];

            //从第四行开始解析，前三行为提示信息
            for(int i=3;i<maxRowNum;i++){
                Row row=sheet.getRow(i);
                Cell nameCell=row.getCell(0);
                Cell passwordCell= row.getCell(1);

                if(nameCell.getCellType()==Cell.CELL_TYPE_STRING){
                    name[i-3]=nameCell.getStringCellValue();
                }else{
                    responseEntity.setStatus(-1);
                    responseEntity.setMsg("姓名列不得为数字");
                    return responseEntity;
                }

                if(passwordCell.getCellType()==Cell.CELL_TYPE_NUMERIC){
                    password[i-3]=(int)passwordCell.getNumericCellValue();
                }else{
                    responseEntity.setStatus(-1);
                    responseEntity.setMsg("身份证号列必须为数字");
                    return responseEntity;
                }
            }

            for(int i=0;i<name.length;i++){
                User user=new User();
                user.setRole("student");
                user.setName(name[i]);
                user.setPassword(password[i]+"");
                User userinfo=userMapper.selectByUsername(user.getName());
                if(userinfo!=null){
                    userList.add(user);
                    flag=-1;
                }else{
                    int res=userMapper.insert(user);
                    if(res<0){
                        userList.add(user);
                        flag=-1;
                    }
                }
            }

        }catch (Exception e) {

        }

        responseEntity.setData(userList);
        if(flag!=0){
            responseEntity.setStatus(-1);
            responseEntity.setMsg("用户已存在或数据库错误，添加失败,失败数据见data");
        }else{
            responseEntity.setStatus(200);
            responseEntity.setMsg("添加成功");
        }
        return responseEntity;
    }

    /**
     * 删除若干考生
     * todo：暂不可用
     * @param map
     * @return
     */
    public ResponseEntity deleteUsers(Map<String,Object> map){
        ArrayList<Integer> userIds=(ArrayList<Integer>)map.get("id");

        for(int i=0;i<userIds.size();i++){
            List<GroupUser> groupUsers=groupUserMapper.selectByUserId(userIds.get(i));
            if (groupUsers==null ||groupUsers.size()==0){

                //todo:加上试卷后需要改动
            }else {
                int delRelation=groupUserMapper.deleteByUserId(userIds.get(i));
            }

            int delRes=userMapper.deleteByPrimaryKey(userIds.get(i));
        }

        ResponseEntity responseEntity=new ResponseEntity();
        responseEntity.setStatus(200);
        responseEntity.setMsg("删除成功");
        return responseEntity;
    }






}
