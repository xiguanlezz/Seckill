package com.cj.cn.mapper;

import com.cj.cn.pojo.MiaoshaGood;
import com.cj.cn.vo.GoodVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MiaoshaGoodMapper extends Mapper<MiaoshaGood> {
    List<GoodVO> getList();

    GoodVO getDetailById(Long goodsId);
}