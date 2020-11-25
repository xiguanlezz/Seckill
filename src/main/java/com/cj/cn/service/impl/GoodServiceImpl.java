package com.cj.cn.service.impl;

import com.cj.cn.dao.MiaoshaGoodMapper;
import com.cj.cn.pojo.MiaoshaGood;
import com.cj.cn.service.IGoodService;
import com.cj.cn.vo.GoodVO;
import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
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
    public int reduceStock(GoodVO goodVO) {
//        Example example = new Example(MiaoshaGood.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("goodsId", goodVO.getId());
//        criteria.andGreaterThan("stockCount", 0);
//        goodVO.setStockCount(goodVO.getStockCount() - 1);
//        miaoshaGoodMapper.updateByExampleSelective(assembleMiaoshaGood(goodVO), example);
        return miaoshaGoodMapper.reduceCount(goodVO.getId());
    }

    @Override
    public List<MiaoshaGood> getAllGoodsStock() {
        Example example = new Example(MiaoshaGood.class);
        example.selectProperties("goodsId", "stockCount");
        return miaoshaGoodMapper.selectByExample(example);
    }

    private MiaoshaGood assembleMiaoshaGood(GoodVO goodVO) {
        MiaoshaGood good = new MiaoshaGood();
        BeanUtils.copyProperties(goodVO, good);
        return good;
    }
}
