package com.highershine.portal.controller;

import com.highershine.portal.common.entity.dto.PersonQueryDTO;
import com.highershine.portal.common.entity.vo.DnaPersonResultVo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.utils.HttpUtils;
import com.highershine.portal.common.utils.JSONUtil;
import com.highershine.portal.common.utils.ResultUtil;
import com.highershine.portal.config.InterfaceConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 户籍提取相关接口
 * @Author zxk
 * @Date 2020/4/8 14:23
 **/
@RequestMapping("personQuery")
@RestController
@Slf4j
@Api("户籍提取相关接口")
public class PersonQueryController {

    @Autowired
    private InterfaceConfig interfaceConfig;

    @PostMapping("query/idCardNo")
    @ApiOperation(value = "根据身份证号查询用户信息(朱向坤)")
    public Result queryPersonByCardNo(@RequestBody PersonQueryDTO personQueryDTO, BindingResult bindingResult) {
        //校验参数
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), message);
        }
        try {
            //调用person_query接口
            //组织参数
            String url = interfaceConfig.getAddr();
            Map paramMap = new HashMap<String, Object>();
            paramMap.put("cardNo", personQueryDTO.getIdCardNo());
            String signCode = DigestUtils.md5DigestAsHex((personQueryDTO.getIdCardNo() + "&&" + interfaceConfig.getSystemKey()
                    + "&&" + interfaceConfig.getSalt()).getBytes());
            paramMap.put("signCode", signCode);
            paramMap.put("systemKey", interfaceConfig.getSystemKey());
            //调用接口
            String result = HttpUtils.doPost(url, paramMap);
            if (StringUtils.isBlank(result)) {
                return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
            }
            Map<String, Object> resultMap = JSONUtil.parseJsonToMap(result);
            long code = (long) resultMap.get("code");
            String msg = (String) resultMap.get("msg");
            if (1 != code) {
                return ResultUtil.errorResult((int) code, msg);
            } else {
                List<DnaPersonResultVo> personData = new ArrayList<>();
                List<HashMap<String, String>> resultData = (List<HashMap<String, String>>) resultMap.get("data");
                if (resultData != null && resultData.size() > 0) {
                    for (int i = 0; i < resultData.size(); i++) {
                        DnaPersonResultVo vo = new DnaPersonResultVo();
                        HashMap<String, String> stringStringHashMap = resultData.get(i);
                        vo.setId(StringUtils.isBlank(stringStringHashMap.get("id")) ? null : stringStringHashMap.get("id"));
                        vo.setXp(StringUtils.isBlank(stringStringHashMap.get("xp")) ? null : stringStringHashMap.get("xp"));
                        boolean csrq = StringUtils.isBlank(stringStringHashMap.get("csrq"));
                        if (!csrq) {
                            String csrqRes = stringStringHashMap.get("csrq");
                            if (csrqRes.length() == 8) {
                                vo.setCsrq(csrqRes.substring(0, 4) + "-" + csrqRes.substring(4, 6) + "-" + csrqRes.substring(6, 8));
                            } else {
                                vo.setCsrq(null);
                            }
                        } else {
                            vo.setCsrq(null);
                        }
                        vo.setHkszd(StringUtils.isBlank(stringStringHashMap.get("hkszd")) ? null : stringStringHashMap.get("hkszd"));
                        vo.setHkszdValue(StringUtils.isBlank(stringStringHashMap.get("hkszdValue"))
                                ? null : stringStringHashMap.get("hkszdValue"));
                        vo.setMz(StringUtils.isBlank(stringStringHashMap.get("mz")) ? null : stringStringHashMap.get("mz"));
                        vo.setMzValue(StringUtils.isBlank(stringStringHashMap.get("mzValue")) ? null : stringStringHashMap.get("mzValue"));
                        vo.setSfzh(StringUtils.isBlank(stringStringHashMap.get("sfzh")) ? null : stringStringHashMap.get("sfzh"));
                        vo.setXb(StringUtils.isBlank(stringStringHashMap.get("xb")) ? null : stringStringHashMap.get("xb"));
                        vo.setXbValue(StringUtils.isBlank(stringStringHashMap.get("xbValue")) ? null : stringStringHashMap.get("xbValue"));
                        vo.setXm(StringUtils.isBlank(stringStringHashMap.get("xm")) ? null : stringStringHashMap.get("xm"));
                        personData.add(vo);
                    }
                }
                return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, personData);
            }
        } catch (Exception e) {
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }
}
