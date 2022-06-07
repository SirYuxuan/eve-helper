package com.yuxuan66.modules.quartz.rest;

import com.yuxuan66.modules.quartz.entity.JobEntity;
import com.yuxuan66.modules.quartz.service.QuartzService;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

/**
 * @author Sir丶雨轩
 * @since 2022/1/13
 */
@RestController
@RequestMapping(path = "/quartz")
@RequiredArgsConstructor
public class QuartzController {

    private final QuartzService quartzService;

    /**
     * 查询所有定时任务列表
     * @return 任务列表
     * @throws SchedulerException 调度器错误
     */
    @GetMapping
    public PageEntity list() throws SchedulerException {
        return quartzService.list();
    }

    /**
     * 添加一个定时任务
     * @param job 定时任务
     * @return 标准返回
     * @throws SchedulerException 调度器错误
     */
    @PostMapping
    public RespEntity add(@RequestBody JobEntity job) throws SchedulerException {
        return quartzService.add(job);
    }

    /**
     * 手动执行任务
     * @param groupName 任务分组
     * @param name 任务名称
     * @return 标准返回
     * @throws SchedulerException 调度器错误
     */
    @PostMapping(path = "/runJob")
    public RespEntity runJob(String groupName,String name) throws SchedulerException {
        return quartzService.runJob(groupName, name);
    }
    /**
     * 删除一个定时任务
     * @param job 定时任务
     * @return 标准返回
     * @throws SchedulerException 调度器错误
     */
    @DeleteMapping
    public RespEntity del(@RequestBody JobEntity job) throws SchedulerException {
        return quartzService.del(job);
    }
}
