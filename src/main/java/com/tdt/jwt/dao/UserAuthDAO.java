package com.tdt.jwt.dao;

import com.tdt.jwt.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthDAO extends JpaRepository<UserAuth, String> {
}
