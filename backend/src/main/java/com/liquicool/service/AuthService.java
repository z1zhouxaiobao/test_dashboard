package com.liquicool.service;

import com.liquicool.dto.LoginRequest;
import com.liquicool.dto.RegisterRequest;
import com.liquicool.dto.UserInfoResponse;
import com.liquicool.entity.SysUser;
import com.liquicool.enums.UserRole;
import com.liquicool.exception.BusinessException;
import com.liquicool.repository.SysUserRepository;
import com.liquicool.util.JwtUtil;
import com.liquicool.util.Md5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public UserInfoResponse login(LoginRequest request) {
        SysUser user = sysUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        String encrypted = Md5Util.md5(request.getPassword());
        if (!encrypted.equals(user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        return buildUserInfoResponse(user, true);
    }

    @Transactional
    public UserInfoResponse register(RegisterRequest request) {
        if (sysUserRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(Md5Util.md5(request.getPassword()));
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setRole(UserRole.USER);
        user.setStatus(1);
        sysUserRepository.save(user);
        return buildUserInfoResponse(user, true);
    }

    public UserInfoResponse getCurrentUser(Long userId) {
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        return buildUserInfoResponse(user, false);
    }

    private UserInfoResponse buildUserInfoResponse(SysUser user, boolean withToken) {
        UserInfoResponse response = new UserInfoResponse();
        BeanUtils.copyProperties(user, response);
        if (withToken) {
            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());
            response.setToken(token);
        }
        return response;
    }
}
