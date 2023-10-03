package com.runwithme.runwithme.global.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job {
	private static final SimpleDateFormat TIMESTAMP_FMT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSS");

	/**
	 * Job interface 구현체
	 * Job의 trigger 실행 시 execute() Method는 scheduler의 스레드 중 하나에 의해 호출
	 *
	 */
	@Override
	public void execute(JobExecutionContext ctx) {
		// 특정 시간에 끝난 챌린지에 등록된 유저들에게 포인트 재분배
		// db에 완전히 챌린지 끝난지 판단하는 여부?

		// 1. 끝나는 날짜로 등록된 챌린지 전부 가져오고
		// 2. 해당 챌린지에 등록된 유저들을 가지고 와서
		// 3. 포인트 분배 알고리즘 돌리기
		// 4. 해당 유저의 포인트 증가

		System.out.println("Job Executed [" + new Date(System.currentTimeMillis()) + "]");

		JobDataMap dataMap = ctx.getJobDetail().getJobDataMap();

		String currentDate = TIMESTAMP_FMT.format(new Date());
		String triggerKey = ctx.getTrigger().getKey().toString(); // group1.trggerName

		String jobSays = dataMap.getString("jobSays"); // Hello World!
		float myFloatValue = dataMap.getFloat("myFloatValue"); // 3.141

		System.out.printf("[%s][%s] %s %s%n", currentDate, triggerKey, jobSays, myFloatValue);
	}
}
