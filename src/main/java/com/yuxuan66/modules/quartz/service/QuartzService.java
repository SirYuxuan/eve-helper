package com.yuxuan66.modules.quartz.service;

import com.yuxuan66.modules.quartz.entity.JobEntity;
import com.yuxuan66.support.basic.model.PageEntity;
import com.yuxuan66.support.basic.model.RespEntity;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Sir丶雨轩
 * @since 2022/1/13
 */
@Service
@RequiredArgsConstructor
public class QuartzService {

    private final Scheduler scheduler;

    /**
     * 添加一个定时任务
     *
     * @param job 定时任务
     * @return 标准返回
     * @throws SchedulerException 调度器错误
     */
    public RespEntity add(JobEntity job) throws SchedulerException {

        CronScheduleBuilder cronScheduleBuilder;
        job.setClazz("com.yuxuan66.modules.quartz.task." + job.getClazz());

        try {
            cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());
        } catch (Exception e) {
            return RespEntity.error("CRON表达式不正确");
        }

        TriggerKey triggerKey = TriggerKey.triggerKey(job.getName(), job.getGroupName());

        CronTrigger triggerOld = (CronTrigger) scheduler.getTrigger(triggerKey);

        if (triggerOld == null) {
            //将job加入到jobDetail中
            Class<?> clazz;
            try {
                clazz = Class.forName(job.getClazz());
            } catch (Exception e) {
                return RespEntity.error("任务主类不存在");
            }

            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) clazz).withIdentity(job.getName(), job.getGroupName()).build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getName(), job.getGroupName()).withSchedule(cronScheduleBuilder).build();
            //执行任务
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            return RespEntity.error("当前定时任务已经存在");
        }

        return RespEntity.success();
    }

    /**
     * 删除一个定时任务
     *
     * @param job 定时任务
     * @return 标准返回
     * @throws SchedulerException 调度器错误
     */
    public RespEntity del(JobEntity job) throws SchedulerException {
        scheduler.deleteJob(new JobKey(job.getName(), job.getGroupName()));
        return RespEntity.success();
    }

    /**
     * 手动执行任务
     *
     * @param groupName 任务分组
     * @param name      任务名称
     * @return 标准返回
     * @throws SchedulerException 调度器错误
     */
    public RespEntity runJob(String groupName, String name) throws SchedulerException {
        scheduler.triggerJob(new JobKey(name, groupName));
        return RespEntity.success();
    }

    /**
     * 查询所有定时任务列表
     *
     * @return 任务列表
     * @throws SchedulerException 调度器错误
     */
    public PageEntity list() throws SchedulerException {
        List<String> triggerGroupNames = scheduler.getTriggerGroupNames();

        List<JobEntity> jobList = new ArrayList<>();

        for (String groupName : triggerGroupNames) {
            if ("DEFAULT".equals(groupName)) {
                continue;
            }
            //组装group的匹配，为了模糊获取所有的triggerKey或者jobKey
            GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.groupEquals(groupName);
            //获取所有的triggerKey
            Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);

            for (TriggerKey triggerKey : triggerKeySet) {
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                JobKey jobKey = trigger.getJobKey();
                JobDetailImpl jobDetail = (JobDetailImpl) scheduler.getJobDetail(jobKey);
                JobEntity jobDto = new JobEntity();
                jobDto.setGroupName(groupName);
                jobDto.setName(jobDetail.getName());
                jobDto.setCron(trigger.getCronExpression());
                jobDto.setClazz(jobDetail.getJobClass().getName());
                jobDto.setStatus(scheduler.getTriggerState(triggerKey).name());
                jobList.add(jobDto);
            }
        }
        return PageEntity.success(jobList);
    }
}
