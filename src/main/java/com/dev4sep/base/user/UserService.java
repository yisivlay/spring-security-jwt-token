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
import org.springframework.http.ResponseEntity;

/**
 * @author YISivlay
 */
public interface UserService {

    PageResponse<UserResponse> findAllUsers(int page, int size);

    UserResponse findById(Long id);

    void deleteById(Long id);

    ResponseEntity<?> updateUser(Long id, UserRequest request);
}
