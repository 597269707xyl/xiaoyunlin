package com.zdtech.platform.service.simulator;

import com.zdtech.platform.framework.repository.SimMsgFieldCodeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huangbo on 2017/3/13.
 */
@Service
public class SimMessageFieldCodeService {
    @Autowired
    private SimMsgFieldCodeDao simMsgFieldCodeDao;

    public void delCodes(Long[] ids){
        for (Long id:ids){
            simMsgFieldCodeDao.delete(id);
        }
    }
}
