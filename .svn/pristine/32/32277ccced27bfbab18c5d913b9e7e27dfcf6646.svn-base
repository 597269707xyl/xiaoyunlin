package com.zdtech.platform.framework.service;


import com.zdtech.platform.framework.repository.SeqDao;
import com.zdtech.platform.framework.utils.Bundle;
import com.zdtech.platform.framework.utils.PatternLayout;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 统一编码服务
 */
@Service
public class UcodeService {

    @Resource
    private SeqDao seqDao;

    public String format(String pattern, Bundle bundle) {
        PatternLayout layout = new PatternLayout(pattern, seqDao);
        return layout.format(bundle);
    }

}