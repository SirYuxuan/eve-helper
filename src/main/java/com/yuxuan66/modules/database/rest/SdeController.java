package com.yuxuan66.modules.database.rest;

import com.yuxuan66.modules.database.entity.Sde;
import com.yuxuan66.modules.database.service.SdeService;
import com.yuxuan66.support.basic.BasicQuery;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

/**
 * @author Sir丶雨轩
 * @since 2021/12/14
 */
@RestController
@RequestMapping(path = "/sde")
@RequiredArgsConstructor
public class SdeController {

    private final SdeService sdeService;

    /**
     * 分页查询sde导入列表
     *
     * @param basicQuery 用户查询
     * @return SDE导入列表
     */
    @GetMapping
    public PageEntity list(BasicQuery<Sde> basicQuery) {
        return sdeService.list(basicQuery);
    }

    /**
     * 新增一个SDE文件
     * @param sde sde
     * @return 标准返回
     */
    @PostMapping
    public RespEntity add(@RequestBody Sde sde){
        return sdeService.add(sde);
    }

    /**
     * SDE文件上传至本地，并同步至OSS
     *
     * @param attach 文件
     * @return 文件地址
     * @throws IOException IO异常
     */
    @PostMapping(path = "upload")
    public RespEntity upload(@RequestParam("attach") MultipartFile attach) throws IOException {
        return sdeService.upload(attach);
    }

    /**
     * 便利指定sde的目录
     * @param id sde记录id
     * @return 目录
     */
    @GetMapping(path = "/listPath")
    public RespEntity listPath(Long id,String path){
        return sdeService.listPath(id,path);
    }

    @GetMapping(path = "/startSde")
    public RespEntity startSde(Long id){
        return sdeService.startSde(id);
    }


    /**
     * 获取一个文件的内容
     * @param id sde id
     * @param catalogue 文件文职
     * @return 文件内容
     */
    @GetMapping(path = "/getFileContent")
    public RespEntity getFileContent(Long id, String catalogue){
        return sdeService.getFileContent(id,catalogue);
    }

    /**
     * 批量删除一SDE数据
     * @param ids SDE id列表
     * @return 标准返回
     */
    @DeleteMapping
    public RespEntity del(@RequestBody Set<Long> ids){
        return sdeService.del(ids);
    }
}
