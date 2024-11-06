package com.ssafy.tripgg.domain.user.service;

import com.ssafy.tripgg.domain.user.dto.UserResponse;
import com.ssafy.tripgg.domain.user.entity.User;
import com.ssafy.tripgg.domain.user.repository.UserRepository;
import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.from(user);
    }
    }
}
