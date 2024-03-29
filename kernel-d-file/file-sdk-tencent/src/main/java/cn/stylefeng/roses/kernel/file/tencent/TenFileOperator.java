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
package cn.stylefeng.roses.kernel.file.tencent;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.stylefeng.roses.kernel.file.api.FileOperatorApi;
import cn.stylefeng.roses.kernel.file.api.enums.BucketAuthEnum;
import cn.stylefeng.roses.kernel.file.api.enums.FileLocationEnum;
import cn.stylefeng.roses.kernel.file.api.exception.FileException;
import cn.stylefeng.roses.kernel.file.api.exception.enums.FileExceptionEnum;
import cn.stylefeng.roses.kernel.file.api.expander.FileConfigExpander;
import cn.stylefeng.roses.kernel.file.api.pojo.props.TenCosProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;

import javax.activation.MimetypesFileTypeMap;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * 腾讯云内网文件操作
 *
 * @author fengshuonan
 * @since 2020-05-22-6:51 下午
 */
public class TenFileOperator implements FileOperatorApi {

    private final TenCosProperties tenCosProperties;

    private COSClient cosClient;

    private TransferManager transferManager;

    public TenFileOperator(TenCosProperties tenCosProperties) {
        this.tenCosProperties = tenCosProperties;
        initClient();
    }

    @Override
    public void initClient() {

        // 1.初始化用户身份信息
        String secretId = tenCosProperties.getSecretId();
        String secretKey = tenCosProperties.getSecretKey();
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

        // 2.设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        Region region = new Region(tenCosProperties.getRegionId());
        ClientConfig clientConfig = new ClientConfig(region);

        // 3.生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);

        // 4.线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。

        //ExecutorService threadPool = Executors.newFixedThreadPool(32); 线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
        ExecutorService threadPool = ExecutorBuilder.create().build();

        // 5.传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        transferManager = new TransferManager(cosClient, threadPool);

        // 6.设置高级接口的分块上传阈值和分块大小为10MB
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(10 * 1024 * 1024);
        transferManagerConfiguration.setMinimumUploadPartSize(10 * 1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);
    }

    @Override
    public void destroyClient() {
        cosClient.shutdown();
    }

    @Override
    public Object getClient() {
        return cosClient;
    }

    @Override
    public boolean doesBucketExist(String bucketName) {
        try {
            return cosClient.doesBucketExist(bucketName);
        } catch (CosClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.TENCENT_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public void setBucketAcl(String bucketName, BucketAuthEnum bucketAuthEnum) {
        try {
            if (bucketAuthEnum.equals(BucketAuthEnum.PRIVATE)) {
                cosClient.setBucketAcl(bucketName, CannedAccessControlList.Private);
            } else if (bucketAuthEnum.equals(BucketAuthEnum.PUBLIC_READ)) {
                cosClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            } else if (bucketAuthEnum.equals(BucketAuthEnum.PUBLIC_READ_WRITE)) {
                cosClient.setBucketAcl(bucketName, CannedAccessControlList.PublicReadWrite);
            }
        } catch (CosClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.TENCENT_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean isExistingFile(String bucketName, String key) {
        try {
            cosClient.getObjectMetadata(bucketName, key);
            return true;
        } catch (CosServiceException e) {
            return false;
        }
    }

    @Override
    public void storageFile(String bucketName, String key, byte[] bytes) {
        // 根据文件名获取contentType
        String contentType = "application/octet-stream";
        if (key.contains(".")) {
            contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(key);
        }

        // 上传文件
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            cosClient.putObject(bucketName, key, new ByteArrayInputStream(bytes), objectMetadata);
        } catch (CosClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.TENCENT_FILE_ERROR, e.getMessage());
        } finally {
            IoUtil.close(byteArrayInputStream);
        }

    }

    @Override
    public void storageFile(String bucketName, String key, InputStream inputStream) {

        // 根据文件名获取contentType
        String contentType = "application/octet-stream";
        if (key.contains(".")) {
            contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(key);
        }

        // 上传文件
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            cosClient.putObject(bucketName, key, inputStream, objectMetadata);
        } catch (CosClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.TENCENT_FILE_ERROR, e.getMessage());
        } finally {
            IoUtil.close(inputStream);
        }
    }

    @Override
    public byte[] getFileBytes(String bucketName, String key) {
        COSObjectInputStream cosObjectInput = null;
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            cosObjectInput = cosObject.getObjectContent();
            return IoUtil.readBytes(cosObjectInput);
        } catch (CosClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.TENCENT_FILE_ERROR, e.getMessage());
        } finally {
            IoUtil.close(cosObjectInput);
        }
    }

    @Override
    public void setFileAcl(String bucketName, String key, BucketAuthEnum bucketAuthEnum) {
        if (bucketAuthEnum.equals(BucketAuthEnum.PRIVATE)) {
            cosClient.setObjectAcl(bucketName, key, CannedAccessControlList.Private);
        } else if (bucketAuthEnum.equals(BucketAuthEnum.PUBLIC_READ)) {
            cosClient.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);
        } else if (bucketAuthEnum.equals(BucketAuthEnum.PUBLIC_READ_WRITE)) {
            cosClient.setObjectAcl(bucketName, key, CannedAccessControlList.PublicReadWrite);
        }
    }

    @Override
    public void copyFile(String originBucketName, String originFileKey, String newBucketName, String newFileKey) {
        // 初始化拷贝参数
        Region srcBucketRegion = new Region(tenCosProperties.getRegionId());
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(
                srcBucketRegion, originBucketName, originFileKey, newBucketName, newFileKey);

        // 拷贝对象
        try {
            transferManager.copy(copyObjectRequest, cosClient, null);
        } catch (CosClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.TENCENT_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public String getFileAuthUrl(String bucketName, String key, Long timeoutMillis) {
        GeneratePresignedUrlRequest presignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        Date expirationDate = new Date(System.currentTimeMillis() + timeoutMillis);
        presignedUrlRequest.setExpiration(expirationDate);
        URL url = null;
        try {
            url = cosClient.generatePresignedUrl(presignedUrlRequest);
        } catch (CosClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.TENCENT_FILE_ERROR, e.getMessage());
        }
        return url.toString();
    }

    @Override
    public String getFileUnAuthUrl(String bucketName, String key) {
        return this.getFileAuthUrl(bucketName, key, FileConfigExpander.getDefaultFileTimeoutSeconds() * 1000);
    }

    @Override
    public void deleteFile(String bucketName, String key) {
        cosClient.deleteObject(bucketName, key);
    }

    @Override
    public FileLocationEnum getFileLocationEnum() {
        return FileLocationEnum.TENCENT;
    }

}
