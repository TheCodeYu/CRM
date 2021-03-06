package com.mychip.framework.server;

import com.alibaba.fastjson.JSONObject;
import com.mychip.common.enums.Product;
import com.mychip.common.core.domain.LoginUser;
import com.mychip.common.core.domain.entity.SysUser;
import com.mychip.common.enums.UserStatus;
import com.mychip.common.exception.BaseException;
import com.mychip.common.utils.StringUtils;
import com.mychip.system.service.OtherUserService;
import com.mychip.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * 用户验证处理
 *
 * @author zhouyu
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OtherUserService otherUserService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JSONObject jsonObject = JSONObject.parseObject(username);
        SysUser user;
        if(Product.crm.getCode().equals(jsonObject.get("product").toString())) {
            user = userService.selectUserByUserName(jsonObject.get("username").toString());
        }else{
            user = otherUserService.selectUserByUserName(jsonObject.get("username").toString(),jsonObject.get("product").toString());
        }
        if (StringUtils.isNull(user))
        {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已停用");
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user, null);
    }
}
