package cn.stylefeng.roses.kernel.sys.modular.org.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.annotation.DictCodeFieldFormat;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.response.ApproverBindUserItem;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 组织机构审批人实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@TableName("sys_hr_org_approver")
@Data
@EqualsAndHashCode(callSuper = true)
public class HrOrgApprover extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "org_approver_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long orgApproverId;

    /**
     * 组织审批类型：1-负责人，2-部长，3-体系负责人，4-部门助理，5-资产助理（专员），6-考勤专员，7-HRBP，8-门禁员，9-办公账号员，10-转岗须知员
     */
    @TableField("org_approver_type")
    @ChineseDescription(
            "组织审批类型：1-负责人，2-部长，3-体系负责人，4-部门助理，5-资产助理（专员），6-考勤专员，7-HRBP，8-门禁员，9-办公账号员，10-转岗须知员")
    @DictCodeFieldFormat(dictTypeCode = "org_approver_type")
    private Integer orgApproverType;

    /**
     * 组织机构id
     */
    @TableField("org_id")
    @ChineseDescription("组织机构id")
    private Long orgId;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ChineseDescription("用户id")
    private Long userId;

    //-------------------------------非实体字段-------------------------------
    //-------------------------------非实体字段-------------------------------
    //-------------------------------非实体字段-------------------------------

    /**
     * 绑定人员信息
     */
    @TableField(exist = false)
    @ChineseDescription("绑定人员信息")
    private List<ApproverBindUserItem> bindUserItemList;

}
