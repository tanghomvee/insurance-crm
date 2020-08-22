package com.homvee.insurancecrm.web.ctrls;

import com.google.common.collect.Maps;
import com.homvee.insurancecrm.dao.entities.Data;
import com.homvee.insurancecrm.service.AchievementService;
import com.homvee.insurancecrm.utils.ExcelUtils;
import com.homvee.insurancecrm.vos.AchievementVO;
import com.homvee.insurancecrm.vos.Msg;
import com.homvee.insurancecrm.web.BaseCtrl;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping(path = {"/achievement"})
@Slf4j
public class AchievementCtrl extends BaseCtrl {

    @Resource
    private AchievementService achievementService;
    private static Map<String , AchievementVO> CACHE = Maps.newConcurrentMap();
    private ThreadPoolExecutor  poolExecutor = new ThreadPoolExecutor(5,
            20,
            30L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(150),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
            );


//    @PostMapping(path = "/query/{cookie}/{fromDate}/{toDate}")
    @PostMapping(path = "/query")
    public Msg query(
//            @PathVariable("cookie") String cookie,
//            @PathVariable("fromDate") String fromDate,
//            @PathVariable("toDate") String toDate,
            @RequestParam("file") MultipartFile file,
            @RequestParam("cookie") String cookie,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestParam("uid") String uid
            ){
        if (file == null || file.isEmpty()) {
            return Msg.error("请选择上传的文件");
        }
        if (StringUtils.isEmpty(cookie)){
            return Msg.error("填写cookie");
        }
        if (StringUtils.isEmpty(uid)){
            return Msg.error("非法参数");
        }
        Date to = DateTime.now().toLocalDate().toDate();
        if (!StringUtils.isEmpty(toDate)){
            to = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(toDate).toDate();
        }
        Date from = DateTime.now().minusMonths(1).toLocalDate().toDate();
        if (!StringUtils.isEmpty(fromDate)){
            from = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(fromDate).toDate();
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
            CACHE.put(uid , new AchievementVO(uid ,cookie ,excelData,from ,to));
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
    @GetMapping("/download")
    public ResponseEntity<byte[]> download(String uid) throws Exception {
        AchievementVO vo = CACHE.get(uid);
        if (vo == null){
            throw new Exception("数据不存在");
        }

        ByteArrayOutputStream outputStream =  achievementService.query(vo);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", new String("匹配数据列表.xls".getBytes("GBK"), "ISO-8859-1"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
}
