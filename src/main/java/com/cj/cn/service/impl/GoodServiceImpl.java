package com.cj.cn.service.impl;

import com.cj.cn.mapper.MiaoshaGoodMapper;
import com.cj.cn.pojo.MiaoshaGood;
import com.cj.cn.service.IGoodService;
import com.cj.cn.vo.GoodVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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

    @Override
    public void reduceStock(GoodVO goodVO) {
        Example example = new Example(MiaoshaGood.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("goodsId", goodVO.getId());
        goodVO.setStockCount(goodVO.getStockCount() - 1);
        miaoshaGoodMapper.updateByExampleSelective(assembleMiaoshaGood(goodVO), example);
    }

    private MiaoshaGood assembleMiaoshaGood(GoodVO goodVO) {
        MiaoshaGood good = new MiaoshaGood();
        BeanUtils.copyProperties(goodVO, good);
        return good;
    }
}
