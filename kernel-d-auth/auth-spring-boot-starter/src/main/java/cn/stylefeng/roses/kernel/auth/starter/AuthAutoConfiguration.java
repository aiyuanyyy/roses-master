/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.auth.starter;

import cn.stylefeng.roses.kernel.auth.api.SessionManagerApi;
import cn.stylefeng.roses.kernel.auth.api.TenantCodeGetApi;
import cn.stylefeng.roses.kernel.auth.api.expander.AuthConfigExpander;
import cn.stylefeng.roses.kernel.auth.api.password.PasswordStoredEncryptApi;
import cn.stylefeng.roses.kernel.auth.api.password.PasswordTransferEncryptApi;
import cn.stylefeng.roses.kernel.auth.api.pojo.auth.PwdRsaSecretProperties;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.auth.auth.DefaultTenantCodeProvider;
import cn.stylefeng.roses.kernel.auth.password.BcryptPasswordStoredEncrypt;
import cn.stylefeng.roses.kernel.auth.password.RsaPasswordTransferEncrypt;
import cn.stylefeng.roses.kernel.auth.session.DefaultSessionManager;
import cn.stylefeng.roses.kernel.auth.session.timer.ClearInvalidLoginUserCacheTimer;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.jwt.JwtTokenOperator;
import cn.stylefeng.roses.kernel.jwt.api.JwtApi;
import cn.stylefeng.roses.kernel.jwt.api.pojo.config.JwtConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Set;


/**
 * 认证和鉴权模块的自动配置
 *
 * @author fengshuonan
 * @since 2020/11/30 22:16
 */
@Configuration
public class AuthAutoConfiguration {

    @Resource
    private PwdRsaSecretProperties pwdRsaSecretProperties;

    /**
     * jwt操作工具类的配置
     *
     * @author fengshuonan
     * @since 2020/12/1 14:40
     */
    @Bean
    @ConditionalOnMissingBean(JwtApi.class)
    public JwtApi jwtApi() {

        JwtConfig jwtConfig = new JwtConfig();

        // 从系统配置表中读取配置
        jwtConfig.setJwtSecret(AuthConfigExpander.getAuthJwtSecret());
        jwtConfig.setExpiredSeconds(AuthConfigExpander.getAuthJwtTimeoutSeconds());

        return new JwtTokenOperator(jwtConfig);
    }

    /**
     * Bcrypt方式的密码加密
     *
     * @author fengshuonan
     * @since 2020/12/21 17:45
     */
    @Bean
    @ConditionalOnMissingBean(PasswordStoredEncryptApi.class)
    public PasswordStoredEncryptApi passwordStoredEncryptApi() {
        return new BcryptPasswordStoredEncrypt();
    }

    /**
     * RSA方式密码加密传输
     *
     * @author fengshuonan
     * @since 2020/12/21 17:45
     */
    @Bean
    @ConditionalOnMissingBean(PasswordTransferEncryptApi.class)
    public PasswordTransferEncryptApi passwordTransferEncryptApi() {
        return new RsaPasswordTransferEncrypt(pwdRsaSecretProperties.getPublicKey(), pwdRsaSecretProperties.getPrivateKey());
    }

    /**
     * 默认的session缓存为内存缓存，方便启动
     * <p>
     * 如需替换请在项目中增加一个SessionManagerApi Bean即可
     *
     * @author fengshuonan
     * @since 2020/11/30 22:17
     */
    @Bean
    @ConditionalOnMissingBean(SessionManagerApi.class)
    public SessionManagerApi sessionManagerApi(CacheOperatorApi<LoginUser> loginUserCache,
                                               CacheOperatorApi<Set<String>> allPlaceLoginTokenCache) {
        Long sessionExpiredSeconds = AuthConfigExpander.getSessionExpiredSeconds();
        return new DefaultSessionManager(loginUserCache, allPlaceLoginTokenCache, sessionExpiredSeconds);
    }

    /**
     * 清空无用登录用户缓存的定时任务
     *
     * @author fengshuonan
     * @since 2021/3/30 11:32
     */
    @Bean
    @ConditionalOnMissingBean(ClearInvalidLoginUserCacheTimer.class)
    public ClearInvalidLoginUserCacheTimer clearInvalidLoginUserCacheTimer(CacheOperatorApi<LoginUser> loginUserCache,
                                                                           CacheOperatorApi<Set<String>> allPlaceLoginTokenCache) {
        return new ClearInvalidLoginUserCacheTimer(loginUserCache, allPlaceLoginTokenCache);
    }

    /**
     * 通过租户编码获取租户id的查询接口
     *
     * @author fengshuonan
     * @since 2023/9/1 0:52
     */
    @Bean
    @ConditionalOnMissingBean(TenantCodeGetApi.class)
    public TenantCodeGetApi tenantCodeGetApi() {
        return new DefaultTenantCodeProvider();
    }

}
