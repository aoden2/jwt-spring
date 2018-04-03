package com.tdt.jwt.service;

import com.tdt.jwt.model.UserAuth;

/**
 * @author
 **/
public interface UserAuthService {

    UserAuth findById(String id);
}
