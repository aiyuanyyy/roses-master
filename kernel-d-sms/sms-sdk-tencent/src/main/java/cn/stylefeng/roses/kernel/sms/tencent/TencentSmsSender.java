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
package cn.stylefeng.roses.kernel.sms.tencent;

import cn.hutool.core.util.ArrayUtil;
import cn.stylefeng.roses.kernel.sms.api.SmsSenderApi;
import cn.stylefeng.roses.kernel.sms.api.exception.SmsException;
import cn.stylefeng.roses.kernel.sms.api.pojo.TencentSmsProperties;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import static cn.stylefeng.roses.kernel.sms.api.exception.enums.SmsExceptionEnum.TENCENT_SMS_PARAM_NULL;

/**
 * 腾讯云短信发送
 *
 * @author fengshuonan
 * @since 2020/5/24 17:58
 */
public class TencentSmsSender implements SmsSenderApi {

    private TencentSmsProperties tencentSmsProperties;

    public TencentSmsSender(TencentSmsProperties tencentSmsProperties) {
        this.tencentSmsProperties = tencentSmsProperties;
    }

    @Override
    public void sendSms(String phone, String templateCode, Map<String, Object> params) {
        try {

            // 实例化一个认证对象
            Credential cred = new Credential(
                    tencentSmsProperties.getSecretId(), tencentSmsProperties.getSecretKey());

            // 实例化一个 http 选项，可选，无特殊需求时可以跳过
            HttpProfile httpProfile = new HttpProfile();
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);

            // 实例化 SMS 的 client 对象
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);

            // 构建请求参数
            SendSmsRequest req = new SendSmsRequest();

            // 设置应用id
            req.setSmsSdkAppid(tencentSmsProperties.getSdkAppId());

            // 设置签名
            req.setSign(tencentSmsProperties.getSign());

            // 设置模板id
            req.setTemplateID(templateCode);

            // 默认发送一个手机短信
            String[] phoneNumbers = {"+86" + phone};
            req.setPhoneNumberSet(phoneNumbers);

            // 模板参数
            if (params != null && params.size() > 0) {
                LinkedList<String> strings = new LinkedList<>();
                Collection<Object> values = params.values();
                for (Object value : values) {
                    strings.add(value.toString());
                }
                req.setTemplateParamSet(ArrayUtil.toArray(strings, String.class));
            }

            SendSmsResponse res = client.SendSms(req);

            SendStatus[] sendStatusSet = res.getSendStatusSet();
            if (sendStatusSet != null && sendStatusSet.length > 0) {
                if (!sendStatusSet[0].getCode().equals("Ok")) {
                    throw new SmsException(TENCENT_SMS_PARAM_NULL, sendStatusSet[0].getCode(), sendStatusSet[0].getMessage());
                }
            }
        } catch (TencentCloudSDKException e) {
            throw new SmsException(TENCENT_SMS_PARAM_NULL, TENCENT_SMS_PARAM_NULL.getErrorCode(), e.getMessage());
        }
    }
}
