package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 *
 * @Author itcast
 * @Create 2024/6/11
 **/
@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){

        //使用UUID随机产生一个新的文件名
        String uuid = UUID.randomUUID().toString();

        //加入原始文件的后缀名
        String originalFilename = file.getOriginalFilename();//1.2.png
        //获得最后一个.的索引位置
        int index = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(index);

        try {
            //调用阿里云OSS工具类，将文件上传到阿里云服务器，返回完整url地址
            String path = aliOssUtil.upload(file.getBytes(), uuid + extension);
            return Result.success(path);
        } catch (IOException e) {
            log.error("文件上传失败！");
        }
        return null;
    }

}
