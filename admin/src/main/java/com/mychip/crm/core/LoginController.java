package com.mychip.crm.core;

import com.mychip.common.constant.Constant;
import com.mychip.common.core.domain.AjaxResult;
import com.mychip.common.core.domain.LoginBody;
import com.mychip.framework.server.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录验证
 *
 * @author zhouyu
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;
    /**
     *  登录方法
     * @param loginBody 登录信息
     * @return  操作结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody){
        AjaxResult ajaxResult = AjaxResult.success();

        //生成令牌并返回
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajaxResult.put(Constant.TOKEN, token);
        return ajaxResult;
    }
}
