package com.yuxuan66.modules.system.rest;

import com.yuxuan66.modules.system.service.AttachService;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Sir丶雨轩
 * @since 2021/12/8
 */
@RestController
@RequestMapping(path = "/attach")
@RequiredArgsConstructor
public class AttachController {

    private final AttachService attachService;

    @PostMapping(path = "/upload")
    public RespEntity upload(@RequestParam("attach") MultipartFile[] files) throws IOException {
        return attachService.upload(files);
    }
}
