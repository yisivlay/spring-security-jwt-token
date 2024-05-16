/**
 * Copyright 2024 DEV4Sep
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dev4sep.base.user;

import com.dev4sep.base.common.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author YISivlay
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResponse<UserResponse> findAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> users = this.userRepository.findAllUsers(pageable);
        List<UserResponse> userResponses = users.stream()
                .map(userMapper::toUserResponse)
                .toList();
        return new PageResponse<>(
                userResponses,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isFirst(),
                users.isLast()
        );
    }

    @Override
    public UserResponse findById(Long id) {
        return this.userRepository.findById(id)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

    @Override
    public void deleteById(Long id) {
        var token = this.tokenRepository.findByUserId(id);
        token.ifPresent(value -> this.tokenRepository.deleteById(value.getId()));
        var user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        this.userRepository.delete(user);

    }

    @Override
    public ResponseEntity<?> updateUser(Long id, UserRequest request) {
        var user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        if (request.getFirstname() != null)
            user.setFirstname(request.getFirstname());
        if (request.getLastname() != null)
            user.setLastname(request.getLastname());
        if (request.getEmail() != null)
            user.setEmail(request.getEmail());
        if (request.getDateOfBirth() != null)
            user.setDateOfBirth(request.getDateOfBirth());
        if (request.getPassword() != null)
            user.setPassword(passwordEncoder.encode(request.getPassword()));

        this.userRepository.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
