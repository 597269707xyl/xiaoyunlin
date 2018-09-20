package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yjli on 2017/9/6.
 */
@Entity
@Table(name = "nxy_func_usecase")
@QueryDef(queryTag = "nxyFuncUsecase", genericQueryFields = {}, sortFields = {"seqNo","id"}, direction = Sort.Direction.DESC,
        daoName = "nxyFuncUsecaseDao")
@EntityListeners(value = EntityCodeListener.class)
public class NxyFuncUsecase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private NxyFuncItem nxyFuncItem;
    private String no;
    @Column(name = "case_number")
    private String caseNumber;
    private String purpose;
    private String step;
    private String expected;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime = new Date();
    @Column(name = "create_user")
    private String createUser;
    @FieldEnum(code = "UC_RESULT")
    private String result;
    @Transient
    private String resultDis;
    @Column(name = "seq_no")
    private Integer seqNo;
    @Column(name = "usecase_no")
    private String usecaseNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NxyFuncItem getNxyFuncItem() {
        return nxyFuncItem;
    }

    public void setNxyFuncItem(NxyFuncItem nxyFuncItem) {
        this.nxyFuncItem = nxyFuncItem;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultDis() {
        return resultDis;
    }

    public void setResultDis(String resultDis) {
        this.resultDis = resultDis;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getUsecaseNo() {
        return usecaseNo;
    }

    public void setUsecaseNo(String usecaseNo) {
        this.usecaseNo = usecaseNo;
    }
}
