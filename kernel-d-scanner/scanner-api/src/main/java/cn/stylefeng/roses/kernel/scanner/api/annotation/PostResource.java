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
package cn.stylefeng.roses.kernel.scanner.api.annotation;

import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.rule.enums.ResBizTypeEnum;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * 资源标识，此注解代替Spring Mvc的@PostMapping注解
 * <p>
 * 目的是为了在使用Spring Mvc的基础之上，增加对接口权限的控制功能
 *
 * @author fengshuonan
 * @since 2018-01-03-下午2:56
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = RequestMethod.POST)
public @interface PostResource {

    /**
     * <pre>
     * 资源编码唯一标识.
     *
     * 说明:
     *     1.可不填写此注解属性.
     *     2.若不填写,则默认生成的编码标识为: 控制器类名称 + 分隔符 + 方法名称.
     *     3.若编码存在重复则系统启动异常
     *
     * </pre>
     */
    String code() default "";

    /**
     * 资源名称(必填项)
     */
    String name() default "";

    /**
     * 当前接口是否需要登录(true-需要登录,false-不需要登录)
     */
    boolean requiredLogin() default true;

    /**
     * 当前接口是否需要鉴权(true-需要鉴权,false-不需要鉴权)
     * <p>
     * 【8.0.0】修改，默认改为false不需要权限校验
     */
    boolean requiredPermission() default false;

    /**
     * 当前接口需要的权限标识（菜单的编码或者菜单功能的编码，从sys_menu表或者sys_menu_options表查询）
     * <p>
     * 如果requiredPermission = true，则需要填写此编码
     */
    String requirePermissionCode() default StrUtil.EMPTY;

    /**
     * 是否需要请求解密，响应加密 (true-需要,false-不需要)
     */
    boolean requiredEncryption() default false;

    /**
     * 是否是视图类型：true-是，false-否
     * 如果是视图类型，url需要以 '/view' 开头，
     * 视图类型的接口会渲染出html界面，而不是json数据，
     * 视图层一般会在前后端不分离项目出现
     */
    boolean viewFlag() default false;

    /**
     * 资源的类型，系统类还是业务类资源
     */
    ResBizTypeEnum resBizType() default ResBizTypeEnum.DEFAULT;

    /**
     * 请求路径(同RequestMapping)
     */
    @AliasFor(annotation = RequestMapping.class)
    String[] path() default {};

    /**
     * 请求的http方法(同RequestMapping)
     */
    @AliasFor(annotation = RequestMapping.class)
    RequestMethod[] method() default RequestMethod.POST;

    /**
     * 同RequestMapping
     */
    @AliasFor(annotation = RequestMapping.class)
    String[] produces() default {};

}
