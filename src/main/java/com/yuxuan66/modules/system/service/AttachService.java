package com.yuxuan66.modules.system.service;

import cn.hutool.core.date.DateUtil;
import com.yuxuan66.modules.user.entity.Attach;
import com.yuxuan66.support.aliyun.OSSHelper;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 2021/12/8
 */
@Service
@RequiredArgsConstructor
public class AttachService {

    private final OSSHelper ossHelper;


    /**
     * 文件上传
     * @param files 文件列表
     * @return 文件访问地址
     * @throws IOException io异常
     */
    public RespEntity upload(MultipartFile[] files) throws IOException {

        List<Attach> attachList = new ArrayList<>(files.length);

        // 多文件上传
        for (MultipartFile file : files) {

            Attach attach = new Attach();
            attach.setUrl(ossHelper.upload(file.getBytes(), file.getOriginalFilename()));
            attachList.add(attach);

        }

        return RespEntity.success(attachList);

    }
}
