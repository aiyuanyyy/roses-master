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
package cn.stylefeng.roses.kernel.email.api.pojo.aliyun;

import lombok.Data;

/**
 * 发送邮件的请求参数
 *
 * @author fengshuonan
 * @since 2018-07-05 21:19
 */
@Data
public class AliyunSendMailParam {

    /**
     * 发送给某人的邮箱
     * <p>
     * 发送给单个用户的邮箱时候用，只能写一个邮箱
     */
    private String to;

    /**
     * 邮件标题
     */
    private String title;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 发信人昵称
     */
    private String fromAlias;

    /**
     * 阿里云控制台，创建的标签名称
     */
    private String tagName;

    /**
     * 预先创建且上传了收件人的收件人列表名称，在阿里云控制台创建
     */
    private String receiversName;

    /**
     * 预先创建且通过审核的模板名称，在阿里云控制台创建
     */
    private String templateName;

}
