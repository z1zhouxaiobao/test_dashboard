package com.liquicool.config;

import com.liquicool.entity.SysUser;
import com.liquicool.enums.UserRole;
import com.liquicool.repository.SysUserRepository;
import com.liquicool.util.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 空库启动时自动写入演示账号，避免公网部署后无法登录。
 */
@Component
public class DemoDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataInitializer.class);

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (sysUserRepository.count() > 0) {
            return;
        }
        saveUser("admin", "系统管理员", UserRole.ADMIN, "/uploads/avatar-admin.png");
        saveUser("user1", "张工程师", UserRole.USER, "/uploads/avatar-user1.png");
        log.info("空库已自动创建演示账号: admin/123456, user1/123456");
    }

    private void saveUser(String username, String nickname, UserRole role, String avatar) {
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(Md5Util.md5("123456"));
        user.setNickname(nickname);
        user.setRole(role);
        user.setStatus(1);
        user.setAvatar(avatar);
        user.setEmail(username + "@liquicool.com");
        sysUserRepository.save(user);
    }
}
