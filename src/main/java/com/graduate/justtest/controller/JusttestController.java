package com.graduate.justtest.controller;


import com.graduate.justtest.entity.Justtest;
import com.graduate.justtest.mapper.JusttestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping ("/insert")
    public int insert(@RequestBody Justtest test){
        return justtestMapper.insert(test);
    }

    @GetMapping("/getall")
    public List<Justtest> getall(){
        return justtestMapper.selectBatchIds(Arrays.asList(1, 2));
    }

}
