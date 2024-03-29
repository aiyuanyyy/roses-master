package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserGroup;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserGroupExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserGroupMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserGroupRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserGroupService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户组业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@Service
public class SysUserGroupServiceImpl extends ServiceImpl<SysUserGroupMapper, SysUserGroup> implements SysUserGroupService {

	@Override
    public void add(SysUserGroupRequest sysUserGroupRequest) {
        SysUserGroup sysUserGroup = new SysUserGroup();
        BeanUtil.copyProperties(sysUserGroupRequest, sysUserGroup);
        this.save(sysUserGroup);
    }

    @Override
    public void del(SysUserGroupRequest sysUserGroupRequest) {
        SysUserGroup sysUserGroup = this.querySysUserGroup(sysUserGroupRequest);
        this.removeById(sysUserGroup.getUserGroupId());
    }

    @Override
    public void edit(SysUserGroupRequest sysUserGroupRequest) {
        SysUserGroup sysUserGroup = this.querySysUserGroup(sysUserGroupRequest);
        BeanUtil.copyProperties(sysUserGroupRequest, sysUserGroup);
        this.updateById(sysUserGroup);
    }

    @Override
    public SysUserGroup detail(SysUserGroupRequest sysUserGroupRequest) {
        return this.querySysUserGroup(sysUserGroupRequest);
    }

    @Override
    public PageResult<SysUserGroup> findPage(SysUserGroupRequest sysUserGroupRequest) {
        LambdaQueryWrapper<SysUserGroup> wrapper = createWrapper(sysUserGroupRequest);
        Page<SysUserGroup> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysUserGroup> findList(SysUserGroupRequest sysUserGroupRequest) {
        LambdaQueryWrapper<SysUserGroup> wrapper = this.createWrapper(sysUserGroupRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private SysUserGroup querySysUserGroup(SysUserGroupRequest sysUserGroupRequest) {
        SysUserGroup sysUserGroup = this.getById(sysUserGroupRequest.getUserGroupId());
        if (ObjectUtil.isEmpty(sysUserGroup)) {
            throw new ServiceException(SysUserGroupExceptionEnum.SYS_USER_GROUP_NOT_EXISTED);
        }
        return sysUserGroup;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private LambdaQueryWrapper<SysUserGroup> createWrapper(SysUserGroupRequest sysUserGroupRequest) {
        LambdaQueryWrapper<SysUserGroup> queryWrapper = new LambdaQueryWrapper<>();

        Long userGroupId = sysUserGroupRequest.getUserGroupId();
        String userGroupTitle = sysUserGroupRequest.getUserGroupTitle();
        String userGroupDetailName = sysUserGroupRequest.getUserGroupDetailName();

        queryWrapper.eq(ObjectUtil.isNotNull(userGroupId), SysUserGroup::getUserGroupId, userGroupId);
        queryWrapper.like(ObjectUtil.isNotEmpty(userGroupTitle), SysUserGroup::getUserGroupTitle, userGroupTitle);
        queryWrapper.like(ObjectUtil.isNotEmpty(userGroupDetailName), SysUserGroup::getUserGroupDetailName, userGroupDetailName);

        return queryWrapper;
    }

}