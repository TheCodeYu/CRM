package com.mychip.system.mapper;

import com.mychip.common.core.domain.entity.SysUser;

/**
 * 用户表 数据层
 *
 * @author zhouyu
 */
public interface UserMapper {
    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);
}
