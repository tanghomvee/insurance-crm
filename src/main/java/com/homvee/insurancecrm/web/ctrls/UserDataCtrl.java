package com.homvee.insurancecrm.web.ctrls;

import com.homvee.insurancecrm.dao.entities.Data;
import com.homvee.insurancecrm.dao.entities.UserData;
import com.homvee.insurancecrm.enums.MatchStateEnum;
import com.homvee.insurancecrm.enums.YNEnum;
import com.homvee.insurancecrm.service.AccountService;
import com.homvee.insurancecrm.service.DataService;
import com.homvee.insurancecrm.service.UserDataService;
import com.homvee.insurancecrm.utils.ExcelUtils;
import com.homvee.insurancecrm.vos.DataVO;
import com.homvee.insurancecrm.vos.Msg;
import com.homvee.insurancecrm.vos.PageVO;
import com.homvee.insurancecrm.vos.UserDataVO;
import com.homvee.insurancecrm.web.BaseCtrl;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.ToStringUtil;
import org.apache.commons.compress.utils.Lists;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = {"/userData"})
@Slf4j
public class UserDataCtrl extends BaseCtrl {

    @Resource
    private UserDataService userDataService;
    @Resource
    private DataService dataService;
    @Resource
    private AccountService accountService;

    @PostMapping(path = "/in")
    public Msg in(@RequestParam("file") MultipartFile file){

        if (!accountService.exist(getUser().getId())){
            return Msg.error("积分账户不存在，或者余额不足");
        }

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

            List<UserData> datas = buildData(excelData , fName);
           return userDataService.save(datas , getUser().getId());
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

        UserDataVO tmpVO = new UserDataVO();
        tmpVO.setCarNo(StringUtils.isEmpty(cardNo) ? null : cardNo);
        tmpVO.setFrameNo(StringUtils.isEmpty(frameNo) ? null : frameNo);
        tmpVO.setMatchState(MatchStateEnum.MATCHED.getVal());
        PageVO<UserDataVO> vo = userDataService.list(tmpVO , pageNum ,pageSize);
        return Msg.success(vo);
    }
    @GetMapping(path = "/download")
    public ResponseEntity<byte[]> download(Date beginDate , Date finishDate) throws IOException {

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) userDataService.download(beginDate , finishDate ,getUser().getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", new String("匹配数据列表.xls".getBytes("GBK"), "ISO-8859-1"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }



    private List<UserData> buildData(List<List<Object>> excelData, String fName){
        List<UserData> datas = Lists.newArrayList();
        for (int i = 0 , size = excelData.size() ; i < size; i++) {
            List<Object> rowData = excelData.get(i);
            if (CollectionUtils.isEmpty(rowData)){
                log.warn("上传文件第 {} 行无数据:{}", i + 1 ,fName );
                continue;
            }

            String frameNo = (String) rowData.get(0);
            if (StringUtils.isEmpty(frameNo)){
                continue;
            }
            UserData userData = new UserData();
            datas.add(userData);
            Data data =  dataService.findByFrameNo(frameNo);
            if (data == null){
                userData.setFrameNo(frameNo);
                userData.setMatchState(MatchStateEnum.MISMATCH.getVal());
                continue;
            }
            BeanUtils.copyProperties(data,userData);
            userData.setMatchState(MatchStateEnum.MATCHED.getVal());
            userData.setCreateTime(DateTime.now().toDate());
            userData.setCreator(getUser().getId().toString());
            userData.setYn(YNEnum.YES.getVal());
        }
        return datas;
    }


}
