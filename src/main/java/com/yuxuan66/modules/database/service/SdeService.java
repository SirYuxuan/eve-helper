package com.yuxuan66.modules.database.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.cache.modules.EveCache;
import com.yuxuan66.common.utils.Lang;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.common.utils.sde.SdeUtil;
import com.yuxuan66.config.SystemConfig;
import com.yuxuan66.modules.database.entity.*;
import com.yuxuan66.modules.database.mapper.*;
import com.yuxuan66.support.aliyun.OSSHelper;
import com.yuxuan66.support.basic.BasicQuery;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SDE相关操作
 *
 * @author Sir丶雨轩
 * @since 2021/12/14
 */
@Service
@RequiredArgsConstructor
public class SdeService {

    @Resource
    private SdeMapper sdeMapper;
    @Resource
    private TypeMapper typeMapper;
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private MetaGroupMapper metaGroupMapper;
    @Resource
    private MarketGroupMapper marketGroupMapper;
    @Resource
    private BluePrintMapper bluePrintMapper;
    @Resource
    private BluePrintSkillMapper bluePrintSkillMapper;
    @Resource
    private BluePrintProductsMapper bluePrintProductsMapper;
    @Resource
    private BluePrintMaterialsMapper bluePrintMaterialsMapper;
    @Resource
    private CategoryMapper categoryMapper;
    private final EveCache eveCache;
    private final OSSHelper ossHelper;
    private final SystemConfig config;


    /**
     * 批量删除一SDE数据
     * @param ids SDE id列表
     * @return 标准返回
     */
    public RespEntity del(Set<Long> ids){
        sdeMapper.deleteBatchIds(ids);
        return RespEntity.success();
    }



    /**
     * 分页查询sde导入列表
     *
     * @param basicQuery 用户查询
     * @return SDE导入列表
     */
    public PageEntity list(BasicQuery<Sde> basicQuery) {
        QueryWrapper<Sde> queryWrapper = basicQuery.getQueryWrapper();
        return PageEntity.success(sdeMapper.selectPage(basicQuery.getPage(), queryWrapper));
    }

    /**
     * 新增一个SDE文件
     *
     * @param sde sde
     * @return 标准返回
     */
    public RespEntity add(Sde sde) {
        sde.setCreateTime(Lang.getNow());
        sde.setCreateId(TokenUtil.getUserId());
        sde.setCreateBy(TokenUtil.getUser().getNickname());
        sde.setIsUse(false);
        sdeMapper.insert(sde);
        return RespEntity.success();
    }


    /**
     * SDE文件上传至本地，并同步至OSS
     *
     * @param file 文件
     * @return 文件地址
     * @throws IOException IO异常
     */
    public RespEntity upload(MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();

        String ext = FileUtil.getSuffix(fileName);
        String dateFileName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN);
        String newFileName = dateFileName + "." + ext;
        String basePath = config.getVal(CacheKey.Config.EVE.EVE_SDE_PATH);
        if (!basePath.endsWith("/")) {
            basePath = basePath + "/";
        }
        byte[] bytes = file.getBytes();
        File sdeFile = new File(basePath + newFileName);
        FileUtil.writeBytes(bytes, sdeFile);
        // 开始解压
        File unSde;
        try {
            unSde = ZipUtil.unzip(basePath + newFileName, basePath + "Data/" + dateFileName);
        } catch (Exception e) {
            try {
                unSde = ZipUtil.unzip(basePath + newFileName, basePath + "Data/" + dateFileName, Charset.forName("GBK"));
            } catch (Exception e1) {
                return RespEntity.error("您选择的文件有误,请在下载地址进行下载。如您编辑过文件,请使用UTF8格式进行压缩并保持原有格式上传");
            }
        }
        // 开始判断是否为SDE文件
        String checkFile = unSde.getAbsolutePath() + "/sde/fsd/landmarks/landmarks.staticdata";
        if (!new File(checkFile).exists()) {
            FileUtil.del(sdeFile);
            FileUtil.del(unSde);
            return RespEntity.error("您选择的文件有误,请在下载地址进行下载。无法在您提供的文件中找到合适的数据");
        }

        Map<String, Object> result = new HashMap<>(2);
        result.put("localPath", unSde);
        result.put("url", ossHelper.upload(bytes, file.getOriginalFilename()));

        return RespEntity.success(result);

    }


    /**
     * 获取一个文件的内容
     *
     * @param id        sde id
     * @param catalogue 文件文职
     * @return 文件内容
     */
    public RespEntity getFileContent(Long id, String catalogue) {
        String path = sdeMapper.selectById(id).getLocalPath();
        if (StrUtil.isNotBlank(catalogue)) {
            path += "/" + catalogue;
        }
        return RespEntity.success(FileUtil.readString(path, StandardCharsets.UTF_8));
    }

    /**
     * 便利指定sde的目录
     *
     * @param id sde记录id
     * @return 目录
     */
    public RespEntity listPath(Long id, String catalogue) {

        String path = sdeMapper.selectById(id).getLocalPath();
        if (StrUtil.isNotBlank(catalogue)) {
            path += "/" + catalogue;
        }
        List<File> fileList = FileUtil.loopFiles(new File(path), 1, null);
        List<FileTree> resultFileList = new ArrayList<>();
        List<FileTree> resultFolderList = new ArrayList<>();
        for (File file : fileList) {
            FileTree fileTree = new FileTree();
            fileTree.setTitle(file.getName());
            fileTree.setIsFile(!file.isDirectory());
            if (fileTree.getIsFile()) {
                resultFileList.add(fileTree);
            } else {
                resultFolderList.add(fileTree);
            }
        }
        resultFolderList.addAll(resultFileList);

        return RespEntity.success(resultFolderList);

    }

    /**
     * 启动一个SDE为当前版本
     *
     * @param id sde id
     * @return 标准返回
     */
    public RespEntity startSde(Long id) {
        // 所有不是当前ID的改成未启动
        UpdateWrapper<Sde> updateWrapper = new UpdateWrapper<>();
        updateWrapper.ne("id", id);
        updateWrapper.set("is_use", false);
        sdeMapper.update(new Sde(), updateWrapper);

        Sde sde = sdeMapper.selectById(id);
        // 开始读取各个文件清洗数据库并清洗缓存
        // 第一步 语言包
        String basePath = sde.getLocalPath();

        // 第二步 分类
        List<Category> categoryList = SdeUtil.getCategoryList(basePath);

        categoryMapper.delete(null);
        categoryMapper.batchInsert(categoryList);
        eveCache.resetCategory(categoryList);

        // 第三步 分组
        List<Group> groupList = SdeUtil.getGroupList(basePath);
        // 需要获取从分类中获取名字
        for (Group group : groupList) {
            Optional<Category> category = categoryList.stream().filter(item -> item.getId().equals(group.getCategoryId())).findFirst();
            if (category.isPresent()) {
                group.setCategoryName(category.get().getName());
                group.setCategoryNameEn(category.get().getNameEn());
            }
        }
        groupMapper.delete(null);
        groupMapper.batchInsert(groupList);
        eveCache.resetGroup(groupList);

        List<MetaGroup> metaGroupList = SdeUtil.getMetaGroupList(basePath);

        List<MarketGroup> marketGroupList = SdeUtil.getMarketGroupList(basePath);
        metaGroupMapper.delete(null);
        metaGroupMapper.batchInsert(metaGroupList);
        eveCache.resetMetaGroup(metaGroupList);

        marketGroupMapper.delete(null);
        marketGroupMapper.batchInsert(marketGroupList);
        eveCache.resetMarketGroup(marketGroupList);


        List<Type> typeList = SdeUtil.getTypeList(basePath);

        // 获取分组名称，元分组名称，市场分组名称
        for (Type type : typeList) {
            if (type.getGroupId() != null) {
                Optional<Group> group = groupList.stream().filter(item -> item.getId().equals(type.getGroupId())).findFirst();
                if (group.isPresent()) {
                    type.setGroupName(group.get().getName());
                    type.setGroupNameEn(group.get().getNameEn());
                    type.setCategoryId(group.get().getCategoryId());
                    type.setCategoryName(group.get().getCategoryName());
                    type.setCategoryNameEn(group.get().getCategoryNameEn());
                }
            }
            if (type.getMetaGroupId() != null) {
                Optional<MetaGroup> group = metaGroupList.stream().filter(item -> item.getId().equals(type.getMetaGroupId())).findFirst();
                if (group.isPresent()) {
                    type.setMetaGroupName(group.get().getName());
                    type.setMetaGroupNameEn(group.get().getNameEn());
                }
            }
            if (type.getMarketGroupId() != null) {
                Optional<MarketGroup> group = marketGroupList.stream().filter(item -> item.getId().equals(type.getMarketGroupId())).findFirst();
                if (group.isPresent()) {
                    type.setMarketGroupName(group.get().getName());
                    type.setMarketGroupNameEn(group.get().getNameEn());
                }
            }
        }


        typeMapper.delete(null);
        typeMapper.batchInsert(typeList);
        eveCache.resetType(typeList);

        List<BluePrint> bluePrintList = SdeUtil.getBluePrint(basePath, eveCache.getTypeMap());

        List<BluePrintProducts> bluePrintProductsList = new ArrayList<>();
        List<BluePrintMaterials> bluePrintMaterialsList = new ArrayList<>();
        List<BluePrintSkill> bluePrintSkillList = new ArrayList<>();
        for (BluePrint bluePrint : bluePrintList) {
            if (bluePrint.getPrintProductsList() != null) {
                bluePrintProductsList.addAll(bluePrint.getPrintProductsList());
            }

            if (bluePrint.getPrintMaterialsList() != null) {
                bluePrintMaterialsList.addAll(bluePrint.getPrintMaterialsList());
            }

            if (bluePrint.getPrintSkillList() != null) {
                bluePrintSkillList.addAll(bluePrint.getPrintSkillList());
            }
        }

        bluePrintMapper.delete(null);
        bluePrintProductsMapper.delete(null);
        bluePrintMaterialsMapper.delete(null);
        bluePrintSkillMapper.delete(null);

        bluePrintMapper.batchInsert(bluePrintList);
        eveCache.resetBluePrint(bluePrintList);

        bluePrintProductsMapper.batchInsert(bluePrintProductsList);
        bluePrintMaterialsMapper.batchInsert(bluePrintMaterialsList);
        bluePrintSkillMapper.batchInsert(bluePrintSkillList);

        sde.setIsUse(true);
        sdeMapper.updateById(sde);

        return RespEntity.success();
    }

    /**
     * 查询EVE市场分组
     * @param pid 父id
     * @return 标准数据
     */
    public RespEntity getMarketGroup(Integer pid){
        List<MarketGroup> marketGroupList = eveCache.getMarketGroup();
        List<MarketGroup> filterMarketGroup = marketGroupList.stream().filter(item-> (item.getPid() == null && pid == 0) || pid.equals(item.getPid())).collect(Collectors.toList());
        for (MarketGroup marketGroup : filterMarketGroup) {
            marketGroup.setHasChildren(marketGroupList.stream().anyMatch(item->marketGroup.getId().equals(item.getPid())));
        }
        return RespEntity.success(filterMarketGroup);
    }

    /**
     * 查询元分组
     * @return 标准返回
     */
    public RespEntity getMetaGroup(){
        return RespEntity.success(metaGroupMapper.selectList(null));
    }


}
