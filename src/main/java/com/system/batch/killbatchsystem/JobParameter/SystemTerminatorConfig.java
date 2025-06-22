package com.system.batch.killbatchsystem.JobParameter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.converter.JsonJobParametersConverter;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class SystemTerminatorConfig {
    @Bean
    public Job processTerminatorJob(JobRepository jobRepository, Step terminationStep){
        return new JobBuilder("terminatorJob", jobRepository)
                .start(terminationStep)
                .build();
    }

    @Bean
    public Step terminationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, Tasklet terminatorTasklet ){
        return new StepBuilder("terminationStep", jobRepository)
                .tasklet(terminatorTasklet, transactionManager)
                .build();
    }

//    @Bean
//    @StepScope
//    public Tasklet terminatorTasklet(@Value("#{jobParameters['terminatorId']}")String terminatorId,
//                                     @Value("#{jobParameters['targetCount']}") Integer targetCount){
//        return (contribution, chunkContext) -> {
//            log.info("시스템 종결자 정보:");
//            log.info("ID: {}", terminatorId);
//            log.info("제거 대상 수: {}", targetCount);
//            log.info("🎇 SYSTEM TERMINATOR {} 작전을 개시합니다", terminatorId);
//            log.info("👻 {}개의 프로세스를 종료합니다.", targetCount);
//
//            for(int i =1; i<=targetCount; i++){
//                log.info("👻 프로세스 {} 종료 완료!", i);
//            }
//
//            log.info("🎯 임무 완료: 모든 대상 프로세스가 종료되었습니다.");
//            return RepeatStatus.FINISHED;
//        };
//    }

    @Bean
    @StepScope
    public Tasklet terminatorTasklet(
            @Value("#{jobParameters['infiltrationTargets']}") String infiltrationTargets
    ) {
        return (contribution, chunkContext) -> {
            String[] targets = infiltrationTargets.split(",");

            log.info("⚡ 침투 작전 개시");
            log.info("첫 번째 타겟: {} 침투 시작", targets[0]);
            log.info("마지막 타겟: {} 에서 집결", targets[1]);
            log.info("🎯 임무 전달 완료");

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public JobParametersConverter jobParameterConverter() {
        return new JsonJobParametersConverter();
    }
}


