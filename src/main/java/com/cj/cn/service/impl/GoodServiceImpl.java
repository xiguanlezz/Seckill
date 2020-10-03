package com.cj.cn.service.impl;

import com.cj.cn.mapper.MiaoshaGoodMapper;
import com.cj.cn.service.IGoodService;
import com.cj.cn.vo.GoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iGoodService")
public class GoodServiceImpl implements IGoodService {
    @Autowired
    private MiaoshaGoodMapper miaoshaGoodMapper;

    @Override
    public List<GoodVO> getList() {
        return miaoshaGoodMapper.getList();
    }

    @Override
    public GoodVO getDetailById(long goodsId) {
        return miaoshaGoodMapper.getDetailById(goodsId);
    }
}
