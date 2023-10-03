package com.runwithme.runwithme.global.quartz;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class Quartz {
	public void run() {
		try {
			// Scheduler 사용을 위한 인스턴스화
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();

			// JOB Data 객체
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("jobSays", "Say Hello World!");
			jobDataMap.put("myFloatValue", 3.1415f);

			JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
				.withIdentity("myJob", "group1")
				.setJobData(jobDataMap)
				.build();

			@SuppressWarnings("deprecation")
			SimpleTrigger simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger()
				.withIdentity("simple_trigger", "simple_trigger_group")
				.startAt(new Date(2023 - 1900, 9, 3, 17, 07))
				.withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(5, 10))
				.forJob(jobDetail)
				.build();

			CronTrigger cronTrigger = (CronTrigger) TriggerBuilder.newTrigger()
				.withIdentity("trggerName", "cron_trigger_group")
				.withSchedule(CronScheduleBuilder.cronSchedule("* * 8 * * ?")) // 매 5초마다 실행
				// .withSchedule(CronScheduleBuilder.cronSchedule("0 0/2 8-17 * * ?")) // 매일 오전 8시에서 오후 5시 사이에 격분마다 실행
				.forJob(jobDetail)
				.build();

			Set<Trigger> triggerSet = new HashSet<>();
			triggerSet.add(simpleTrigger);
			triggerSet.add(cronTrigger);

			scheduler.scheduleJob(jobDetail, triggerSet, false);
			scheduler.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
