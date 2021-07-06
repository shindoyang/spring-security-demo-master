package com.spring.security.service.impl;

import com.alibaba.excel.EasyExcel;
import com.spring.security.entity.NmsSmsTmplExcelVo;
import org.junit.Test;

import java.util.List;

public class ExcelDemo {

    @Test
    public void reloadExcel() throws Exception {
        String fileName = "D:/cellphoneSampleSMS.xlsx";
        List<NmsSmsTmplExcelVo> list = EasyExcel.read(fileName).head(NmsSmsTmplExcelVo.class).sheet(0).headRowNumber(2).doReadSync();
        System.out.println(list.size());


//        OutputStream out = new FileOutputStream("D:/test2.xlsx");
//        ExcelWriter writer = EasyExcelFactory.getWriterWithTemp(inputStream, out, ExcelTypeEnum.XLSX, true);

        EasyExcel.write("D:/test1.xlsx", NmsSmsTmplExcelVo.class).sheet("Student").doWrite(list);
    }

    public void redisTest() {
        /*List<String> mobiles = new ArrayList<>();
        mobiles.add("13717778470");
        mobiles.add("13717778471");
        mobiles.add("13717778472");
        mobiles.add("13717778473");

        stringRedisTemplate.opsForValue().set("uidCache", JSONObject.toJSONString(mobiles), uidExpireMinutes, TimeUnit.MINUTES);
        String mobilecaches = stringRedisTemplate.opsForValue().get("uidCache");
        JSONArray objects = JSONObject.parseArray(mobilecaches);
        if (objects.contains("13717778473")) {
            System.out.println("包含 13717778473");
        }

        objects.add("17777344234");
        stringRedisTemplate.opsForValue().set("uidCache", JSONObject.toJSONString(objects), uidExpireMinutes, TimeUnit.MINUTES);
        String mobilecaches2 = stringRedisTemplate.opsForValue().get("uidCache");
        JSONArray objects2 = JSONObject.parseArray(mobilecaches2);
        if (objects2.contains("17777344234")) {
            System.out.println("包含 17777344234");
        }



        Map<String, String> uidCache = new HashMap<>();
        uidCache.put("13717778470", "88880");
        uidCache.put("13717778471", "88881");
        uidCache.put("13717778472", "88882");
        uidCache.put("13717778473", "88883");


        stringRedisTemplate.opsForValue().set("uidCache", JSONObject.toJSONString(uidCache), uidExpireMinutes, TimeUnit.MINUTES);

        String uids = stringRedisTemplate.opsForValue().get("uidCache");
        Map uidCaches = (Map) JSON.parse(uids);
        System.out.println(uidCaches);
        System.out.println(uidCaches.get("13717778473"));

        uidCaches.put("1567778901", "999999999");
        stringRedisTemplate.opsForValue().set("uidCache", JSONObject.toJSONString(uidCaches), uidExpireMinutes, TimeUnit.MINUTES);
        String uids2 = stringRedisTemplate.opsForValue().get("uidCache");
        Map uidCaches2 = (Map) JSON.parse(uids2);
        System.out.println(uidCaches2);
        System.out.println(uidCaches2.get("1567778901"));*/
    }
}
