package com.atguigu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.bean.OBJECT_T_MALL_SKU_CLASS;
import com.atguigu.bean.OBJECT_T_MALL_SKU_DETAIL;
import com.atguigu.bean.T_MALL_SKU;
import com.atguigu.bean.T_MALL_SKU_ATTR_VALUE;
import com.atguigu.bean.T_MALL_SKU_CLASS;
import com.atguigu.mapper.SkuMapper;
import com.atguigu.service.SkuServiceInfo;

@Service
public class SkuServiceImpl implements SkuServiceInfo {
	@Autowired
	SkuMapper skuMapper;

	@Override
	public List<T_MALL_SKU_CLASS> get_sku_by_class_2(int class_2_id) {
		List<T_MALL_SKU_CLASS> list_sku = skuMapper.select_sku_by_class_2(class_2_id);
		return list_sku;
	}

	@Override
	public List<OBJECT_T_MALL_SKU_CLASS> get_attr_search(int class_2_id, List<T_MALL_SKU_ATTR_VALUE> list_av) {
		
		List<OBJECT_T_MALL_SKU_CLASS> list_sku =new ArrayList<OBJECT_T_MALL_SKU_CLASS>();
		
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		
		map.put("class_2_id", class_2_id);

		if (list_av!=null && list_av.size() > 0) {
			StringBuffer sql = new StringBuffer();
			sql.append(" and sku.id in ");
			sql.append(" ( select sku0.sku_id from ");
			for (int i = 0; i < list_av.size(); i++) {
				T_MALL_SKU_ATTR_VALUE list_i = list_av.get(i);
//				where id in list_shxm_id?
				
//				java的数组和数据库的数组

				sql.append(" (select sku_id from t_mall_sku_attr_value where shxm_id = " + list_i.getShxm_id()
						+ " and shxzh_id = " + list_i.getShxzh_id() + ") sku" + i + "");
				if (list_av.size() > 1 && i < (list_av.size() - 1)) {
					sql.append(" , ");
				} 
			}

			if (list_av.size() > 1) {
				sql.append(" where ");
			}

			for (int i = 0; i < list_av.size(); i++) {
				if (list_av.size() > 1 && i < (list_av.size() - 1)) {
					sql.append("sku" + i + ".sku_id= sku" + (i + 1) + ".sku_id ");
					if (list_av.size()>2&&i < (list_av.size() - 2)) {
						sql.append(" and ");
						// 此处拼接and为后添加，倒数第二个and使用在倒数第一个元素前边的位置；
					}

				}
			}
			sql.append(" ) ");
			map.put("sql", sql.toString());
		}
		list_sku = skuMapper.select_attr_search(map);
		return list_sku;
	}

	@Override
	public OBJECT_T_MALL_SKU_DETAIL get_sku_search(int spu_id, int sku_id) {
		
		OBJECT_T_MALL_SKU_DETAIL obj_sku= skuMapper.select_sku_search(spu_id,sku_id);
		return  obj_sku;
	}

	@Override
	public List<T_MALL_SKU> get_sku_list_by_spu_id(int spu_id) {
		List<T_MALL_SKU> list_sku=skuMapper.select_sku_list_by_spu_id(spu_id);
		return list_sku;
	}

}
