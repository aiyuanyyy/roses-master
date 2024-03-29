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
package cn.stylefeng.roses.kernel.file.api.pojo.response;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 附件列表返回类
 *
 * @author fengshuonan
 * @since 2020/12/30 21:24
 */
@Data
public class SysFileInfoListResponse implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 主键id
     */
    @ChineseDescription("主键id")
    private Long fileId;

    /**
     * 文件编码
     * <p>
     * 解决一个文件多个版本问题，多次上传文件编码不变
     * <p>
     * 版本号升级的依据，code相同id不同视为同一个文件的不同版本
     */
    @ChineseDescription("文件编码")
    private Long fileCode;


    /**
     * 文件仓库（文件夹）
     */
    @ChineseDescription("文件仓库(文件夹)")
    private String fileBucket;


    /**
     * 存储到bucket中的名称，主键id+.后缀
     */
    @ChineseDescription("存储到bucket中的名称，主键id+.后缀")
    private String fileObjectName;

    /**
     * 是否为机密文件
     */
    @ChineseDescription("是否为机密文件")
    private String secretFlag;

    /**
     * 文件应用Code名称
     */
    @ChineseDescription("文件应用Code名称")
    private String fileAppCodeName;

    /**
     * 文件名称（上传时候的文件名）
     */
    @ChineseDescription("文件名称（上传时候的文件名）")
    private String fileOriginName;

    /**
     * 文件存储位置：1-阿里云，2-腾讯云，3-minio，4-本地
     */
    @ChineseDescription("存储位置：1-阿里云，2-腾讯云，3-minio，4-本地")
    private Integer fileLocation;

    /**
     * 文件后缀
     */
    @ChineseDescription("文件后缀")
    private String fileSuffix;

    /**
     * 文件大小信息，计算后的
     */
    @ChineseDescription("文件大小信息，计算后")
    private String fileSizeInfo;

    /**
     * 文件版本
     */
    @ChineseDescription("文件版本")
    private Integer fileVersion;

    /**
     * 创建人
     */
    @ChineseDescription("创建人")
    private Long createAccountId;

    /**
     * 创建人部门id
     */
    @ChineseDescription("创建人部门id")
    private Long createDeptId;

    /**
     * 创建人姓名
     */
    @ChineseDescription("创建人姓名")
    private String createUserName;

    /**
     * 创建时间
     */
    @ChineseDescription("创建时间")
    private Date createTime;

    /**
     * 创建人姓名
     */
    @ChineseDescription("创建人姓名")
    private String realName;

    /**
     * 文件访问的url
     */
    @ChineseDescription("文件访问的url")
    private String fileUrl;

}
