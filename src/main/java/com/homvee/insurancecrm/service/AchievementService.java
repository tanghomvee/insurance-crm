package com.homvee.insurancecrm.service;

import com.homvee.insurancecrm.vos.AchievementVO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface AchievementService {

    ByteArrayOutputStream query(AchievementVO vo) throws IOException;
}
