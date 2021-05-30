package com.mychip.system.service.impl;

import com.mychip.common.core.domain.entity.SysUser;
import com.mychip.system.mapper.OtherUserMapper;
import com.mychip.system.service.OtherUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * User:   zhouyu
 * Date:   2021/5/30 0030
 */
@Service
public class OtherUserServiceImpl implements OtherUserService {

    private static final Logger log = LoggerFactory.getLogger(OtherUserServiceImpl.class);

    @Autowired
    private OtherUserMapper otherUserMapper;
    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName,String product) {
        return otherUserMapper.selectUserByUserName(userName,product);
    }
}
