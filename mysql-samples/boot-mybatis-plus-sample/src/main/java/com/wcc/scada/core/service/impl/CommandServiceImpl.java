package com.wcc.scada.core.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wcc.scada.core.entity.Command;
import com.wcc.scada.core.mapper.CommandMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcc.scada.core.service.CommandService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wcc
 * @since 2022-07-27
 */
@Service
public class CommandServiceImpl extends ServiceImpl<CommandMapper, Command> implements CommandService {

}
