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
package cn.stylefeng.roses.kernel.timer.modular.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.annotation.SimpleFieldFormat;
import cn.stylefeng.roses.kernel.sys.api.format.UserNameFormatProcess;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时任务
 *
 * @author stylefeng
 * @since 2020/6/30 18:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_timers")
public class SysTimers extends BaseEntity {

    /**
     * 定时器id
     */
    @TableId(value = "timer_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("定时器id")
    private Long timerId;

    /**
     * 任务名称
     */
    @TableField("timer_name")
    @ChineseDescription("任务名称")
    private String timerName;

    /**
     * 执行任务的class的类名（实现了TimerAction接口的类的全称）
     */
    @TableField("action_class")
    @ChineseDescription("执行任务的class的类名")
    private String actionClass;

    /**
     * 定时任务表达式
     */
    @TableField("cron")
    @ChineseDescription("定时任务表达式")
    private String cron;

    /**
     * 参数
     */
    @TableField("params")
    @ChineseDescription("参数")
    private String params;

    /**
     * 状态：1-运行，2-停止
     */
    @TableField("job_status")
    @ChineseDescription("状态：1-运行，2-停止")
    private Integer jobStatus;

    /**
     * 备注信息
     */
    @TableField("remark")
    @ChineseDescription("备注信息")
    private String remark;

    /**
     * 是否删除：Y-被删除，N-未删除
     */
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @ChineseDescription("是否删除")
    @TableLogic
    private String delFlag;

    @Override
    @SimpleFieldFormat(processClass = UserNameFormatProcess.class)
    public Long getCreateUser() {
        return super.getCreateUser();
    }

}
