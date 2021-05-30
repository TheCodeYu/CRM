package com.mychip.system.mapper;

import com.mychip.common.core.domain.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * Description:
 * User:   zhouyu
 * Date:   2021/5/30 0030
 */
public interface OtherUserMapper {
    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(@Param("userName") String userName, @Param("product")String product);
}
