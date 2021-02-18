package com.ry.sskt;

import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.model.UserInfo;
import com.ry.sskt.model.common.entity.cache.SyncUserLockStatusCache;
import com.ry.sskt.service.IDledcService;
import com.ry.sskt.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;
import java.util.Arrays;

@EnableScheduling
@ServletComponentScan
@SpringBootApplication
@EnableSwagger2 // 启动swagger注解
@EnableCaching // 启动缓存
@EnableAsync // 开启异步
@MapperScan(basePackages = {"com.ry.sskt.mapper"})
@EnableTransactionManagement
@EnableDiscoveryClient
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    IDledcService dledcService;
    @Autowired
    IUserService userService;

    @Scheduled(cron = "0 0 0/1 * * ?")// 0 0/1 * * * ?
    public void minuteExport() {
        SyncUserLockStatusCache lockStatusCache = new SyncUserLockStatusCache();
        boolean lockResult = RedisUtil.updateCas(lockStatusCache);
        if (lockResult) {
            var userList = userService.getListByStudentCodes(Arrays.asList("等待同步"));
            if (userList.size() == 0) {
                return;
            }
            log.info(String.format("用户同步开始，共%s条用户数据待同步", userList.size()));
            int successCount = 0;
            int failCount = 0;
            for (var user : userList) {
                try {
                    dledcService.sychroUser(user.getSourceId());
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    log.error(String.format("%s更新失败", user.getSourceId()), e);
                }
            }
            log.info(String.format("本次用户同步完成，成功更新%s条用户数据,%s条数据更新失败", successCount, failCount));
        }
    }


}