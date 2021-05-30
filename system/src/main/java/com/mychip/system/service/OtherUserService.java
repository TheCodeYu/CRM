package com.mychip.system.service;

import com.mychip.common.core.domain.entity.SysUser;

/**
 * Description:
 * User:   zhouyu
 * Date:   2021/5/30 0030
 */
public interface OtherUserService {
    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @param product APP
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName,String product);
}
