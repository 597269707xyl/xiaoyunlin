package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yjli on 2017/9/6.
 */
@Entity
@Table(name = "nxy_func_usecase_exec")
@EntityListeners(value = EntityCodeListener.class)
public class NxyFuncUsecaseExec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "usecase_id", referencedColumnName = "id")
    private NxyFuncUsecase nxyFuncUsecase;
    private int round;
    @FieldEnum(code = "UC_RESULT")
    private String result;
    @Transient
    private String resultDis;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @Column(name = "exec_time")
    private Date execTime = new Date();
    @Column(name = "batch_id")
    private Long batchId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NxyFuncUsecase getNxyFuncUsecase() {
        return nxyFuncUsecase;
    }

    public void setNxyFuncUsecase(NxyFuncUsecase nxyFuncUsecase) {
        this.nxyFuncUsecase = nxyFuncUsecase;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
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

    public Date getExecTime() {
        return execTime;
    }

    public void setExecTime(Date execTime) {
        this.execTime = execTime;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }
}
