package com.practice.file_management_sys.service;

import com.practice.file_management_sys.domain.JsonData;

public interface UserService {
    JsonData checkLogin(String email, String password);

    JsonData addUser(String email, String gender, String password, String domain);
}
