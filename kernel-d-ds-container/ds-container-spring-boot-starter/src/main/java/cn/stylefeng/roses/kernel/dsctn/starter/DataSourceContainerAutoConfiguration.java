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
package cn.stylefeng.roses.kernel.dsctn.starter;

import cn.stylefeng.roses.kernel.dsctn.DynamicDataSource;
import cn.stylefeng.roses.kernel.dsctn.aop.MultiSourceExchangeAop;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据库连接和DAO框架的配置
 * <p>
 * 如果开启此连接池，注意排除 GunsDataSourceAutoConfiguration
 *
 * @author fengshuonan
 * @since 2020/11/30 22:24
 */
@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnMissingBean(DataSource.class)
public class DataSourceContainerAutoConfiguration {

    /**
     * 多数据源连接池，如果开启此连接池，注意排除 GunsDataSourceAutoConfiguration
     *
     * @author fengshuonan
     * @since 2020/11/30 22:49
     */
    @Bean
    public DynamicDataSource dataSource() {
        return new DynamicDataSource();
    }

    /**
     * 数据源切换的AOP
     *
     * @author fengshuonan
     * @since 2020/11/30 22:49
     */
    @Bean
    public MultiSourceExchangeAop multiSourceExchangeAop() {
        return new MultiSourceExchangeAop();
    }

}
