//package com.xjrsoft.common.config;
//
//import org.quartz.Scheduler;
//import org.quartz.impl.JobDetailImpl;
//import org.quartz.spi.JobFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
//@Configuration
//public class QuartzConfig {
//
//    private JobFactory jobFactory;
//
//    @Bean(name = "schedulerFactoryBean")
//    public SchedulerFactoryBean createSchedulerFactoryBean(){
//        SchedulerFactoryBean schedulerFactoryBean=new SchedulerFactoryBean();
////        schedulerFactoryBean.setOverwriteExistingJobs(true);
//        schedulerFactoryBean.setJobFactory(jobFactory);
//        return schedulerFactoryBean;
//    }
//
//    @Bean
//    public JobDetailImpl createJobDetailsImpl() {
//        return new JobDetailImpl();
//    }
//
//    @Bean
//    public Scheduler scheduler() {
//        return createSchedulerFactoryBean().getScheduler();
//    }
//
//}
