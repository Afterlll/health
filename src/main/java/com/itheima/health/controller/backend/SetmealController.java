package com.itheima.health.controller.backend;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiniuUtils;
import com.itheima.health.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 上传文件
     * @param multipartFile
     * @return
     */
    @RequestMapping("/upload.do")
    public Result upload(MultipartFile multipartFile) {
        // 获取文件的原始名字
        String originalFilename = multipartFile.getOriginalFilename();
        // 为了防止文件名重复,随机生成文件名: 8d1f624e0314405690ff87b09183a56c-a.jpg
        String fileName = UUIDUtils.uuid() + "-" + originalFilename;
        // 把文件上传到七牛云
        try {
            QiniuUtils.upload2Qiniu(multipartFile.getBytes(), fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 返回结果，附上文件名字
        return new Result(true, MessageConstant.UPLOAD_SUCCESS, fileName);
    }

    /**
     * 新增套餐
     * @param setmeal 套餐数据
     * @param checkgroupIds 套餐包含的检查组
     * @return 统一响应结果
     */
    @PostMapping("/add.do")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        // 1.调用业务层新增套餐
        setmealService.add(setmeal, checkgroupIds);

        // 2.响应统一结果Result
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }
}
