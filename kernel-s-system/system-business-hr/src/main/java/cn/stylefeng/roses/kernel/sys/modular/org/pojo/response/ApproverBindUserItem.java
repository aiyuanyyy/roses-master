package cn.stylefeng.roses.kernel.sys.modular.org.pojo.response;

import lombok.Data;

/**
 * 绑定用户信息详情（用在部门审批人绑定用户）
 *
 * @author fengshuonan
 * @since 2022/9/13 23:47
 */
@Data
public class ApproverBindUserItem {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 姓名
     */
    private String name;

    /**
     * 公司或部门名称
     */
    private String deptName;

}
