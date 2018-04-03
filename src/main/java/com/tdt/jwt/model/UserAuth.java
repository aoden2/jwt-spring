package com.tdt.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth {

    @Id
    String username;
    String tenantId;
    String password;
}
