package com.homvee.insurancecrm.web.ctrls;

import com.homvee.insurancecrm.dao.entities.Data;
import com.homvee.insurancecrm.enums.YNEnum;
import com.homvee.insurancecrm.service.DataService;
import com.homvee.insurancecrm.utils.ExcelUtils;
import com.homvee.insurancecrm.vos.DataVO;
import com.homvee.insurancecrm.vos.Msg;
import com.homvee.insurancecrm.vos.PageVO;
import com.homvee.insurancecrm.web.BaseCtrl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.joda.time.DateTime;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = {"/data"})
@Slf4j
public class DataCtrl extends BaseCtrl {

    @Resource
    private DataService dataService;


    @PostMapping(path = "/in")
    public Msg in(@RequestParam("file") MultipartFile file){
        if (file == null || file.isEmpty()) {
            return Msg.error("请选择上传的文件");
        }
        String fName = file.getOriginalFilename();
        InputStream ins = null;
        try {
            ins = file.getInputStream();
            List<List<Object>> excelData =  ExcelUtils.getData(ins , fName);
            if (CollectionUtils.isEmpty(excelData)){
                log.warn("上传文件无数据:{}" ,fName );
                return Msg.success();
            }

            List<Data> datas = buildData(excelData , fName);
            dataService.save(datas);
            return Msg.success();
        } catch (Exception e) {
            log.error("上传文件异常", e);
        }finally {
            if (ins != null){
                try {
                    ins.close();
                } catch (IOException e) {
                    log.error("关闭上传文件异常", e);
                }
            }
        }
        return Msg.error();
    }
    @GetMapping(path = "/list")
    public Msg list(String cardNo ,String frameNo , Integer pageNum,Integer pageSize){

        DataVO tmpVO = new DataVO();
        tmpVO.setCarNo(StringUtils.isEmpty(cardNo) ? null : cardNo);
        tmpVO.setFrameNo(StringUtils.isEmpty(frameNo) ? null : frameNo);
        PageVO<DataVO> vo = dataService.list(tmpVO , pageNum ,pageSize);
        return Msg.success(vo);
    }
    @PostMapping(path = "/match")
    public Msg match(){
        return Msg.success();
    }


    private List<Data> buildData(List<List<Object>> excelData,String fName){
        List<Data> datas = Lists.newArrayList();
        for (int i = 0 , size = excelData.size() ; i < size; i++) {
            List<Object> rowData = excelData.get(i);
            if (CollectionUtils.isEmpty(rowData)){
                log.warn("上传文件第 {} 行无数据:{}", i + 1 ,fName );
                continue;
            }
            Data data = new Data();
            datas.add(data);
            int column = 0;
            /**自带车牌*/
            data.setCarNo(this.getColumnData(rowData ,column++));
            /**车辆品牌*/
            data.setBrand(this.getColumnData(rowData ,column++));
            /**识别代码*/
            data.setFrameNo(this.getColumnData(rowData ,column++));
            /**发动机号*/
            data.setEngineNo(this.getColumnData(rowData ,column++));
            /**身份证号*/
            data.setIdNo(this.getColumnData(rowData ,column++));
            /**所有人*/
            data.setOwnerName(this.getColumnData(rowData ,column++));
            /**发牌日期*/
            data.setLicenseDate(this.getColumnData(rowData ,column++));
            /**住所*/
            data.setAddr(this.getColumnData(rowData ,column++));
            /**手机号码*/
            data.setPhoneNum(this.getColumnData(rowData ,column++));
            /**保险公司*/
            data.setCompany(this.getColumnData(rowData ,column++));
            /**商业险保单号*/
            data.setViPolicyNo(this.getColumnData(rowData ,column++));
            /**终保日期*/
            data.setFinalInsDate((String) rowData.get(column++));
            /**车辆使用价值*/
            data.setUseVal(this.getColumnData(rowData ,column++));
            /**商业险原价*/
            data.setViAmount(this.getColumnData(rowData ,column++));
            /**商业险折扣*/
            data.setViDiscount(this.getColumnData(rowData ,column++));
            /**商业险优惠后*/
            data.setPayViAmt(this.getColumnData(rowData ,column++));
            /**交强险*/
            data.setCiAmount(this.getColumnData(rowData ,column++));
            /**车船税*/
            data.setVvt(this.getColumnData(rowData ,column++));
            /**内容*/
            data.setContent(this.getColumnData(rowData ,column++));

            data.setCreateTime(DateTime.now().toDate());
            data.setCreator(getUser().getId().toString());
            data.setYn(YNEnum.YES.getVal());
        }
        return datas;
    }

    private String getColumnData(List<Object> rowData , int column){
        if (column >= rowData.size()){
            return "";
        }
        Object data = rowData.get(column);
        if (data == null){
            return "";
        }
        if (data instanceof String){
            return (String) data;
        }
        return rowData.get(column) + "";
    }
}
