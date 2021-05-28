package com.mychip.system.service;


import com.mychip.common.core.domain.entity.SysUser;

/**
 * 用户 业务层
 *
 * @author zhouyu
 */

public interface UserService {

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);
}
