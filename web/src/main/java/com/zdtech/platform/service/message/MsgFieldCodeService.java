package com.zdtech.platform.service.message;

import com.zdtech.platform.framework.repository.MsgFieldCodeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huangbo on 2017/3/8.
 */
@Service
public class MsgFieldCodeService {
    @Autowired
    private MsgFieldCodeDao msgFieldCodeDao;

    public void delCodes(Long[] ids){
        for (Long id:ids){
            delCode(id);
        }
    }

    private void delCode(Long id){
        msgFieldCodeDao.delete(id);
    }
}
