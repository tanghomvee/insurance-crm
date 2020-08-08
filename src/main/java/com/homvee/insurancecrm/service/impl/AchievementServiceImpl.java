package com.homvee.insurancecrm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homvee.insurancecrm.dao.entities.UserData;
import com.homvee.insurancecrm.service.AchievementService;
import com.homvee.insurancecrm.utils.ExcelUtils;
import com.homvee.insurancecrm.utils.HttpUtils;
import com.homvee.insurancecrm.vos.AchievementVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AchievementServiceImpl implements AchievementService {

    private String url = "http://157.122.153.67:9000/khyx/qth/proposalsearch/query.do?" +
            "uploadImageControl=1&imageTypeControl=0&useWXUploadControl=0&insuredTypePayControl=0&userCode=85110867&" +
            "prpallProposalRequestBody.lisenceColorCode=01&payVerifyControl=0&payLinkControl=0&usePlatformPayType=1&" +
            "totalFee=&comId=51000000&carClauseCode=F42,F41,F43,&prpallProposalRequestBody.licenseNo=%s&" +
            "prpallProposalRequestBody.policyNo=&prpallProposalRequestBody.insuredName=&" +
            "prpallProposalRequestBody.frameNo=&prpallProposalRequestBody.operateDateStart=%tF&" +
            "prpallProposalRequestBody.operateDate=%tF&" +
            "prpallProposalRequestBody.underWriteFlag=99&prpallProposalRequestBody.riskCode=1";

    @Override
    public ByteArrayOutputStream query(AchievementVO vo) throws IOException {

        CookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID",vo.getCookie());
        cookie.setDomain("157.122.153.67");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        List<List<Object>> data = vo.getData();
        List<PiccData.PiccRow> userData = Lists.newArrayList();
        for (List row : data){
            final String cardNum = (String) row.get(0);
            if (StringUtils.isEmpty(cardNum)){
                continue;
            }
            String urlTmp = String.format(url,cardNum,vo.getFromDate() , vo.getToDate());
            try {
               String rs = HttpUtils.postForm(urlTmp , cookieStore);
               if (StringUtils.isEmpty(rs)){
                   continue;
               }

                PiccData dataPicc = JSON.parseObject(rs , PiccData.class);
               if (dataPicc.getTotal() < 1){
                   continue;
               }
                PiccData.PiccRow piccRow = dataPicc.getRows().get(0);
                userData.add(piccRow);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        List<Map<String,String>> excelData = Lists.transform(userData, new Function<PiccData.PiccRow, Map<String, String>>() {
            @NullableDecl
            @Override
            public Map<String, String> apply(@NullableDecl PiccData.PiccRow data) {
                Map<String,String> dataMap = Maps.newHashMap();
                dataMap.put("车牌号",data.getLicenseNo());
                dataMap.put("日期","");
                dataMap.put("保险公司","人保");
                dataMap.put("保险公司分类","");
                dataMap.put("分部门","");
                dataMap.put("销售人员","");
                dataMap.put("保险单号",data.getPolicyNo());
                dataMap.put("发动机号",data.getEngineNo());
                dataMap.put("识别代码（车架号）",data.getFrameNo());
                dataMap.put("排量","");
                dataMap.put("厂牌型号","");
                dataMap.put("被保险人",data.getInsuredName());
                dataMap.put("被保险人身份证号（组织机构代码）",data.getIdentifyNumber());
                dataMap.put("被保险人地址","");
                dataMap.put("商业险",data.getBiPayTax());
                dataMap.put("交强险",data.getCiPayTax());
                dataMap.put("保费合计",data.getSumPremium());
                dataMap.put("车船税",data.getSumtax());
                dataMap.put("合计（加车船）",data.getSumPremium());
                dataMap.put("交强险手续费率（收）","");
                dataMap.put("应收交强险手续费收入","");
                dataMap.put("商业险手续费率（收）","");
                dataMap.put("应收商业险手续费收入","");
                dataMap.put("车船税手续费率（收）","");
                dataMap.put("应收车船税手续费收入","");
                dataMap.put("应收手续费收入合计","");

                return dataMap;
            }
        });



        return (ByteArrayOutputStream) ExcelUtils.download("匹配的数据" , excelData);
    }


    @Data
    private class PiccData implements Serializable{
        private Integer total;
        private List<PiccRow> rows;

        @Data
        private class PiccRow implements Serializable{
            private String actualPremium;
            private String agentName;
            private String bZFlag;
            private String biPayTax;
            private String bindId;
            private String brandName;
            private String carInsuredinfo;
            private String ciPayTax;
            private String clauseType;
            private String comCode;
            private String contractno;
            private String customerType;
            private String deductPremium;
            private String delayPayTax;
            private String discount;
            private String endDate;
            private String endHour;
            private String engineNo;
            private String exchangeNo;
            private String frameNo;
            private String giftPackageComCode;
            private String identifyNumber;
            private String identifyType;
            private String inputTime;
            private String insureId;
            private String insuredCode;
            private String insuredName;
            private String isDeliver;
            private String isJoinGift;
            private String isMsgCheck;
            private String isNetProp;
            private String licenseColorCode;
            private String licenseNo;
            private String memIdNo;
            private String memIdType;
            private String memName;
            private String modelCode;
            private String monopolycode;
            private String msgStatus;
            private String netSales;
            private String operateDate;
            private String othFlag;
            private String payDate;
            private String payType;
            private String poliNo;
            private String policyFlag;
            private String policyNo;
            private String prePayTax;
            private String proposalNo;
            private String relationPolicyInfo;
            private String relationProposalNo;
            private String relationUPolicyInfo;
            private String riskCName;
            private String riskCode;
            private String sendNoticeMsgFlag;
            private String signStatus;
            private String startDate;
            private String startHour;
            private String sumPremium;
            private String sumtax;
            private String thisPayTax;
            private String tid;
            private String typeCode;
            private String underWriteFlag;
            private String vinNo;
        }
    }
}
