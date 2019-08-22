package cn.app.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.app.pojo.AppCategory;
import cn.app.pojo.AppInfo;
import cn.app.pojo.DataDictionary;
import cn.app.service.AppCategoryService;
import cn.app.service.AppInfoService;
import cn.app.service.AppVersionService;
import cn.app.service.DataDictionaryService;
import cn.app.util.PageSupport;

@Controller
@RequestMapping("/backend")
public class BackendController {
	private Logger logger = Logger.getLogger(DevController.class);
	@Resource
	private DataDictionaryService dataDictionaryService;
	@Resource
	private AppCategoryService appCategoryService;
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private AppVersionService appVersionService;
	
	@RequestMapping("/main.html")
	public String main(){
		return "backend/BackendMain";
	}

	@RequestMapping("/applist.html")
	public String toAppList(@RequestParam(value="querySoftwareName",required = false) String querySoftwareName,
			@RequestParam(value="queryFlatformId",required = false) Integer queryFlatformId,
			@RequestParam(value="queryCategoryLevel1",required = false) Integer queryCategoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required = false) Integer queryCategoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required = false) Integer queryCategoryLevel3,
			@RequestParam(value="pageIndex",required=false) Integer pageIndex,
			HttpServletRequest request){
		//获得全部APP所属平台
		List<DataDictionary> appflatform = dataDictionaryService.findappflatform();
		//获得全部一级分类
		List<AppCategory> appCategorys = appCategoryService.findAppCategorys1();
		
		//查询app
		//app总数
		int appCount = appInfoService.findAppInfoCount(querySoftwareName, 1, queryFlatformId, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3);
		logger.info("app总数:"+appCount);
		//创建分页对象
		PageSupport page = new PageSupport();
		int currentPageNo = 0;
		if(pageIndex == null || pageIndex.equals("")){
			currentPageNo = 1;
		}else{
			currentPageNo = Integer.valueOf(pageIndex);
		}
		page.setCurrentPageNo(currentPageNo);
		page.setPageSize(5);
		page.setTotalCount(appCount);
		//查询app列表
		List<AppInfo> appInfos = appInfoService.findAppInfoList(querySoftwareName, 1, queryFlatformId,
				queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, 
				page.getCurrentPageNo(), page.getPageSize());
		List<AppCategory> categoryLevel2List = null;
		List<AppCategory> categoryLevel3List = null;
		//保存选择的分类(二级分类，三级分类)
		if(queryCategoryLevel1 != null && !"".equals(queryCategoryLevel1)){
			categoryLevel2List = appCategoryService.findAppCategorysByParentId(queryCategoryLevel1);
		}
		if(queryCategoryLevel2 != null && !"".equals(queryCategoryLevel2)){
			categoryLevel3List = appCategoryService.findAppCategorysByParentId(queryCategoryLevel2);
		}
		//用户选择的查询信息
		request.setAttribute("querySoftwareName", querySoftwareName);
		request.setAttribute("queryFlatformId", queryFlatformId);
		request.setAttribute("queryCategoryLevel1", queryCategoryLevel1);
		request.setAttribute("queryCategoryLevel2", queryCategoryLevel2);
		request.setAttribute("queryCategoryLevel3", queryCategoryLevel3);
		//查询条件列表
		request.setAttribute("flatFormList", appflatform);
		request.setAttribute("categoryLevel1List", appCategorys);
		request.setAttribute("categoryLevel2List", categoryLevel2List);
		request.setAttribute("categoryLevel3List", categoryLevel3List);
		//app列表
		request.setAttribute("appInfoList", appInfos);
		//分页信息
		request.setAttribute("pages", page);
		return "backend/applist";
	}
	
	@RequestMapping("/categorylevellist.json")
	@ResponseBody
	public List<AppCategory> getAppCategory(@RequestParam("pid")Integer pid){
		if(pid == null){
			return appCategoryService.findAppCategorys1();
		}
		return appCategoryService.findAppCategorysByParentId(pid);
	}
	
	@RequestMapping("/check.html")
	public String check(@RequestParam("aid")Integer aid,@RequestParam("vid")Integer vid,
			HttpServletRequest request){
		request.setAttribute("appInfo", appInfoService.findAppInfoById(aid));
		request.setAttribute("appVersion", appVersionService.findAppVersionById(vid));
		return "backend/appcheck";
	}
	
	@RequestMapping("/checksave.html")
	public String appCheck(@RequestParam("id")Integer id,@RequestParam("status")Integer status){
		AppInfo appInfo = new AppInfo();
		appInfo.setId(id);
		appInfo.setStatus(status);
		if(appInfoService.updateAppInfo(appInfo)){
			return "redirect:/backend/applist.html";
		}else{
			return "backend/applist";
		}
	}
}
