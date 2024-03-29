package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserGroupDetail;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserGroupDetailExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserGroupDetailMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserGroupDetailRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserGroupDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户组详情业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@Service
public class SysUserGroupDetailServiceImpl extends ServiceImpl<SysUserGroupDetailMapper, SysUserGroupDetail> implements SysUserGroupDetailService {

	@Override
    public void add(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        SysUserGroupDetail sysUserGroupDetail = new SysUserGroupDetail();
        BeanUtil.copyProperties(sysUserGroupDetailRequest, sysUserGroupDetail);
        this.save(sysUserGroupDetail);
    }

    @Override
    public void del(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        SysUserGroupDetail sysUserGroupDetail = this.querySysUserGroupDetail(sysUserGroupDetailRequest);
        this.removeById(sysUserGroupDetail.getDetailId());
    }

    @Override
    public void edit(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        SysUserGroupDetail sysUserGroupDetail = this.querySysUserGroupDetail(sysUserGroupDetailRequest);
        BeanUtil.copyProperties(sysUserGroupDetailRequest, sysUserGroupDetail);
        this.updateById(sysUserGroupDetail);
    }

    @Override
    public SysUserGroupDetail detail(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        return this.querySysUserGroupDetail(sysUserGroupDetailRequest);
    }

    @Override
    public PageResult<SysUserGroupDetail> findPage(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        LambdaQueryWrapper<SysUserGroupDetail> wrapper = createWrapper(sysUserGroupDetailRequest);
        Page<SysUserGroupDetail> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysUserGroupDetail> findList(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        LambdaQueryWrapper<SysUserGroupDetail> wrapper = this.createWrapper(sysUserGroupDetailRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private SysUserGroupDetail querySysUserGroupDetail(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        SysUserGroupDetail sysUserGroupDetail = this.getById(sysUserGroupDetailRequest.getDetailId());
        if (ObjectUtil.isEmpty(sysUserGroupDetail)) {
            throw new ServiceException(SysUserGroupDetailExceptionEnum.SYS_USER_GROUP_DETAIL_NOT_EXISTED);
        }
        return sysUserGroupDetail;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private LambdaQueryWrapper<SysUserGroupDetail> createWrapper(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        LambdaQueryWrapper<SysUserGroupDetail> queryWrapper = new LambdaQueryWrapper<>();

        Long detailId = sysUserGroupDetailRequest.getDetailId();
        Long userGroupId = sysUserGroupDetailRequest.getUserGroupId();
        Integer selectType = sysUserGroupDetailRequest.getSelectType();
        Long selectValue = sysUserGroupDetailRequest.getSelectValue();
        String selectValueName = sysUserGroupDetailRequest.getSelectValueName();

        queryWrapper.eq(ObjectUtil.isNotNull(detailId), SysUserGroupDetail::getDetailId, detailId);
        queryWrapper.eq(ObjectUtil.isNotNull(userGroupId), SysUserGroupDetail::getUserGroupId, userGroupId);
        queryWrapper.eq(ObjectUtil.isNotNull(selectType), SysUserGroupDetail::getSelectType, selectType);
        queryWrapper.eq(ObjectUtil.isNotNull(selectValue), SysUserGroupDetail::getSelectValue, selectValue);
        queryWrapper.like(ObjectUtil.isNotEmpty(selectValueName), SysUserGroupDetail::getSelectValueName, selectValueName);

        return queryWrapper;
    }

}