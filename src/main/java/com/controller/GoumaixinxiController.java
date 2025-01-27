package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.GoumaixinxiEntity;
import com.entity.view.GoumaixinxiView;

import com.service.GoumaixinxiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 购买信息
 * 后端接口
 * @author 
 * @email 
 * @date 2021-01-12 22:31:57
 */
@RestController
@RequestMapping("/goumaixinxi")
public class GoumaixinxiController {
    @Autowired
    private GoumaixinxiService goumaixinxiService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,GoumaixinxiEntity goumaixinxi, HttpServletRequest request){

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("shangjia")) {
			goumaixinxi.setShangjiazhanghao((String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("yonghu")) {
			goumaixinxi.setYonghuming((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<GoumaixinxiEntity> ew = new EntityWrapper<GoumaixinxiEntity>();
		PageUtils page = goumaixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, goumaixinxi), params), params));
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,GoumaixinxiEntity goumaixinxi, HttpServletRequest request){
        EntityWrapper<GoumaixinxiEntity> ew = new EntityWrapper<GoumaixinxiEntity>();
		PageUtils page = goumaixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, goumaixinxi), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( GoumaixinxiEntity goumaixinxi){
       	EntityWrapper<GoumaixinxiEntity> ew = new EntityWrapper<GoumaixinxiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( goumaixinxi, "goumaixinxi")); 
        return R.ok().put("data", goumaixinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(GoumaixinxiEntity goumaixinxi){
        EntityWrapper< GoumaixinxiEntity> ew = new EntityWrapper< GoumaixinxiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( goumaixinxi, "goumaixinxi")); 
		GoumaixinxiView goumaixinxiView =  goumaixinxiService.selectView(ew);
		return R.ok("查询购买信息成功").put("data", goumaixinxiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        GoumaixinxiEntity goumaixinxi = goumaixinxiService.selectById(id);
        return R.ok().put("data", goumaixinxi);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") String id){
        GoumaixinxiEntity goumaixinxi = goumaixinxiService.selectById(id);
        return R.ok().put("data", goumaixinxi);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody GoumaixinxiEntity goumaixinxi, HttpServletRequest request){
    	goumaixinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(goumaixinxi);

        goumaixinxiService.insert(goumaixinxi);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody GoumaixinxiEntity goumaixinxi, HttpServletRequest request){
    	goumaixinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(goumaixinxi);

        goumaixinxiService.insert(goumaixinxi);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody GoumaixinxiEntity goumaixinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(goumaixinxi);
        goumaixinxiService.updateById(goumaixinxi);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        goumaixinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<GoumaixinxiEntity> wrapper = new EntityWrapper<GoumaixinxiEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("shangjia")) {
			wrapper.eq("shangjiazhanghao", (String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("yonghu")) {
			wrapper.eq("yonghuming", (String)request.getSession().getAttribute("username"));
		}

		int count = goumaixinxiService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	


}
