package cn.stylefeng.roses.kernel.sys.modular.org.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 组织机构审批人封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HrOrgApproverRequest extends BaseRequest {

    /**
     * 主键id
     */
    @ChineseDescription("主键id")
    private Long orgApproverId;

    /**
     * 组织审批类型：1-负责人，2-部长，3-体系负责人，4-部门助理，5-资产助理（专员），6-考勤专员，7-HRBP，8-门禁员，9-办公账号员，10-转岗须知员
     */
    @ChineseDescription("组织审批类型：1-负责人，2-部长，3-体系负责人，4-部门助理，5-资产助理（专员），6-考勤专员，7-HRBP，8-门禁员，9-办公账号员，10-转岗须知员")
    @NotNull(message = "组织审批类型不能为空", groups = {add.class, delete.class})
    private Integer orgApproverType;

    /**
     * 组织机构id
     */
    @ChineseDescription("组织机构id")
    @NotNull(message = "组织机构id不能为空", groups = {list.class, add.class, delete.class})
    private Long orgId;

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    @NotNull(message = "用户id不能为空", groups = {delete.class})
    private Long userId;

    /**
     * 用户id集合，一般用在绑定多个用户
     */
    @ChineseDescription("用户id集合")
    @NotEmpty(message = "用户id集合不能为空", groups = {add.class})
    private List<Long> userIdList;

}
