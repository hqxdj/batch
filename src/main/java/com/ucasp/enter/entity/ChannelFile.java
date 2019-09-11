package com.ucasp.enter.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: ucasp-enter-parse
 * @Description: 渠道文件实体
 * @Date 2019/9/11 15:50
 * Copyright (c)   xdj
 */
@Data
@Builder
public class ChannelFile {
    private String channelNum;
    private String customerNum;
    private String cardNum;
    private BigDecimal dealMoney;
    private String dealType;
    private String userName;
    private String commissionType;
    private String unionPayFlowNum;
    private String bankFlowNum;
    private BigDecimal charge1;
    private BigDecimal charge2;
    private BigDecimal charge3;
    private String additionalCharge;
    private String remark;

}
