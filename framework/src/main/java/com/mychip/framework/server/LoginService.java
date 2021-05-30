package com.mychip.framework.server;

import com.mychip.common.constant.Constant;
import com.mychip.common.constant.Product;
import com.mychip.common.core.domain.LoginUser;
import com.mychip.common.core.domain.entity.SysUser;
import com.mychip.common.core.redis.RedisCache;
import com.mychip.common.exception.CustomException;
import com.mychip.common.exception.user.UserPasswordNotMatchException;
import com.mychip.common.utils.DateUtils;
import com.mychip.common.utils.MessageUtils;
import com.mychip.common.utils.ServletUtils;
import com.mychip.common.utils.ip.IpUtils;
import com.mychip.framework.manager.AsyncFactory;
import com.mychip.framework.manager.AsyncManager;
import com.mychip.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录校验方法
 *
 * @author zhouyu
 */
@Component
public class LoginService {

    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid,String product)
    {

//        String verifyKey = Constant.CAPTCHA_CODE_KEY + uuid;
//        String captcha = redisCache.getCacheObject(verifyKey);
//        redisCache.deleteObject(verifyKey);
//        if (captcha == null)
//        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
//            throw new CaptchaExpireException();
//        }
//        if (!code.equalsIgnoreCase(captcha))
//        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
//            throw new CaptchaException();
//        }
        // 用户验证
        Authentication authentication = null;
        try
        {
            if(product.startsWith(Constant.PACKAGE) && (product.length() > Constant.PACKAGE.length())){
                product = Product.valueOf(product.substring(Constant.PACKAGE.length())).getCode();
            }else{
                throw new CustomException("非法访问");
            }
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            String data = "{\"username\":\""+username+"\",\"product\":\""+product+"\"}";
            authentication =authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(data,
                    password));

        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constant.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constant.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constant.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUser());
        // 生成token
        return tokenService.createToken(loginUser);
    }
    /**
     * 记录登录信息
     */
    public void recordLoginInfo(SysUser user)
    {
        user.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        user.setLoginDate(DateUtils.getNowDate());
        //userService.updateUserProfile(user);
    }
}
