package com.zdtech.platform.framework.utils.converter;


import com.zdtech.platform.framework.repository.SeqDao;
import com.zdtech.platform.framework.utils.Bundle;
import com.zdtech.platform.framework.utils.PatternLayout;
import com.zdtech.platform.framework.utils.SeqIds;

/**
 * 输出指定长度的序列号
 * 
 * @author qfxu
 * 
 */
public class SerialNoPatternConverter extends PatternConverter {

    //流水号标识，可以是常量字符串或者表达式
	private String sid;

    //流水数据访问接口
    private SeqDao seqDao;

	public SerialNoPatternConverter(FormattingInfo formattingInfo, String sid, SeqDao seqDao) {
		super(formattingInfo);
		this.sid = sid;
        this.seqDao = seqDao;
	}

	@Override
	protected String convert(Bundle bundle) {
        if(sid.startsWith("{")){
            sid = sid.substring(1,sid.lastIndexOf("}"));
            PatternLayout layout = new PatternLayout(sid, seqDao);
            sid = layout.format(bundle);
        }
//        Seq seq = seqDao.findOne(sid);
//        if(seq == null){
//            seq = new Seq();
//            seq.setSid(sid);
//            seq.setNextval(1);
//            seqDao.save(seq);
//        } else{
//            long nextval = seq.getNextval();
//            nextval ++;
//            seq.setNextval(nextval);
//            seqDao.save(seq);
//        }

//		return String.valueOf(seq.getNextval());
        return SeqIds.getSeqIdNoPreFetch(sid).toString();
	}

}
