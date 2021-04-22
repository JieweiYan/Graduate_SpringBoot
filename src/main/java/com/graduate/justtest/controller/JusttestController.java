package com.graduate.justtest.controller;


import com.graduate.justtest.entity.Justtest;
import com.graduate.justtest.mapper.JusttestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YanJiewei
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/test")
public class JusttestController {

    @Autowired
    JusttestMapper justtestMapper;

    @RequestMapping(value = "/upload",method=RequestMethod.POST)
    @ResponseBody
    public String upload(
            @RequestParam("myFile") MultipartFile multipartFile,
            HttpServletRequest request) {
        System.out.println(multipartFile);
//        try {
//            // 获取项目路径
//            String realPath = request.getSession().getServletContext()
//                    .getRealPath("");
//            InputStream inputStream = multipartFile.getInputStream();
//            String contextPath = request.getContextPath();
//            // 服务器根目录的路径
//            String path = realPath.replace(contextPath.substring(1), "");
//            // 根目录下新建文件夹upload，存放上传图片
//            String uploadPath = path + "upload";
//            // 获取文件名称
//            String filename = multipartFile.getOriginalFilename();
//            // 将文件上传的服务器根目录下的upload文件夹
//            File file = new File(uploadPath, filename);
//            FileUtils.copyInputStreamToFile(inputStream, file);
//            // 返回图片访问路径
//            String url = request.getScheme() + "://" + request.getServerName()
//                    + ":" + request.getServerPort() + request.getContextPath()+"/upload/" + filename;
//            //WangEditor类 是定义成功或者失败的返回格式在第9点
//            WangEditor we = new WangEditor(url);
//            return we;
//        } catch (IOException e) {
//        }
        return null;
    }

    @PutMapping ("/insert")
    public int insert(@RequestBody Justtest test){
        return justtestMapper.insert(test);
    }

    @GetMapping("/getall")
    public List<Justtest> getall(){
        return justtestMapper.selectBatchIds(Arrays.asList(1, 2));
    }

}
