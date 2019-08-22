package cn.app.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.app.pojo.AppCategory;
import cn.app.pojo.AppInfo;
import cn.app.pojo.AppVersion;
import cn.app.pojo.DataDictionary;
import cn.app.pojo.DevUser;
import cn.app.service.AppCategoryService;
import cn.app.service.AppInfoService;
import cn.app.service.AppVersionService;
import cn.app.service.DataDictionaryService;
import cn.app.util.Constants;
import cn.app.util.PageSupport;

@Controller
@RequestMapping("/dev")
public class DevController {
	
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
		return "dev/DevMain";
	}
	
	@RequestMapping("/appList.html")
	public String appList(@RequestParam(value="querySoftwareName",required = false) String querySoftwareName,
			@RequestParam(value="queryStatus",required = false) Integer queryStatus,
			@RequestParam(value="queryFlatformId",required = false) Integer queryFlatformId,
			@RequestParam(value="queryCategoryLevel1",required = false) Integer queryCategoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required = false) Integer queryCategoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required = false) Integer queryCategoryLevel3,
			@RequestParam(value="pageIndex",required=false) Integer pageIndex,
			HttpServletRequest request){
		//获得全部APP状态
		List<DataDictionary> appStatusList = dataDictionaryService.findappStatus();
		//获得全部APP所属平台
		List<DataDictionary> appflatform = dataDictionaryService.findappflatform();
		//获得全部一级分类
		List<AppCategory> appCategorys = appCategoryService.findAppCategorys1();
		
		//查询app
		//app总数
		int appCount = appInfoService.findAppInfoCount(querySoftwareName, queryStatus, queryFlatformId, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3);
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
		List<AppInfo> appInfos = appInfoService.findAppInfoList(querySoftwareName, queryStatus, queryFlatformId,
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
		request.setAttribute("queryStatus", queryStatus);
		request.setAttribute("queryFlatformId", queryFlatformId);
		request.setAttribute("queryCategoryLevel1", queryCategoryLevel1);
		request.setAttribute("queryCategoryLevel2", queryCategoryLevel2);
		request.setAttribute("queryCategoryLevel3", queryCategoryLevel3);
		//查询条件列表
		request.setAttribute("statusList", appStatusList);
		request.setAttribute("flatFormList", appflatform);
		request.setAttribute("categoryLevel1List", appCategorys);
		request.setAttribute("categoryLevel2List", categoryLevel2List);
		request.setAttribute("categoryLevel3List", categoryLevel3List);
		//app列表
		request.setAttribute("appInfoList", appInfos);
		//分页信息
		request.setAttribute("pages", page);
		return "dev/appList";
	}
	
	@RequestMapping("/categorylevellist.json")
	@ResponseBody
	public List<AppCategory> getAppCategory(@RequestParam("pid")Integer pid){
		if(pid == null){
			return appCategoryService.findAppCategorys1();
		}
		return appCategoryService.findAppCategorysByParentId(pid);
	}
	
	@RequestMapping("/addAppInfo.html")
	public String toappInfoAdd(@ModelAttribute AppInfo appInfo){
		return "dev/appinfoadd";
	}
	
	@RequestMapping("/datadictionarylist.json")
	@ResponseBody
	public List<DataDictionary> getDatadictionaryList(){
		return dataDictionaryService.findappflatform();
	}
	
	@RequestMapping("/apkexist.json")
	@ResponseBody
	public Map<String,String> apkNameIsExist(@RequestParam("APKName")String APKName){
		Map<String,String> map = new HashMap<String, String>();
		if(APKName != null && APKName != ""){
			if(appInfoService.findAppInfoByAPKName(APKName)){
				map.put("APKName", "exist");
			}else{
				map.put("APKName", "noexist");
			}
		}else{
			map.put("APKName", "empty");
		}
		return map;
	}
	
	@RequestMapping("/appinfoaddsave.html")
	public String addAppInfo(AppInfo appInfo,HttpServletRequest request,HttpSession session,
			@RequestParam(value="a_logoPicPath",required=false)MultipartFile attach
			){
		String logoPicPath =  null;
		String logoLocPath =  null;
		String url = "dev/appinfoadd";
		
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+java.io.File.separator+"uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			int filesize = 500000;
			if(attach.getSize() > filesize){//上传大小不得超过 50k
				request.setAttribute("fileUploadError", "文件大小不能超过50k");
				return url;
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
			   ||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式
				 String fileName = appInfo.getAPKName() + ".jpg";//上传LOGO图片命名:apk名称.apk
				 File targetFile = new File(path,fileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					request.setAttribute("fileUploadError", "上传文件失败");
					return url;
				} 
				 logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				 logoLocPath = path+File.separator+fileName;
			}else{
				request.setAttribute("fileUploadError", "图片格式不正确");
				return url;
			}
		}
		
		DevUser devUser = (DevUser) session.getAttribute(Constants.DEV_USER);
		if(logoLocPath != null && logoPicPath != null){
			appInfo.setLogoLocPath(logoLocPath);
			appInfo.setLogoPicPath(logoPicPath);
		}
		
			appInfo.setCreatedBy(devUser.getId());
			appInfo.setCreationDate(new Date());
			
			appInfo.setDevId(devUser.getId());
			appInfo.setStatus(1);
			if(appInfoService.insertAppInfo(appInfo)){
				return "redirect:/dev/appList.html";
			}else{
				return url;
			}
	}
	
	@RequestMapping("/appinfomodify.html")
	public String toAppinfomodify(@RequestParam("id")Integer id,HttpServletRequest request){
		request.setAttribute("appInfo", appInfoService.findAppInfoById(id));
		return "dev/appinfomodify";
	}
	
	@RequestMapping("/delfile.json")
	@ResponseBody
	public Map<String,String> delFile(@RequestParam("id")Integer id,@RequestParam("flag")String flag){
		Map<String,String> map = new HashMap<String, String>();
		if(flag.equals("logo")){
			File file = new File(appInfoService.findAppInfoById(id).getLogoLocPath());
			if(file.delete()){
				if(appInfoService.updateApplogo(id)){
					map.put("result", "success");
				}else{
					map.put("result", "failed");
				}
			}
		}else if(flag.equals("apk")){
			File file = new File(appVersionService.findAppVersionById(id).getApkLocPath());
			if(file.delete()){
				if(appVersionService.updateApk(id)){
					map.put("result", "success");
				}else{
					map.put("result", "failed");
				}
			}
		}
		return map;
	}
	
	@RequestMapping("/appinfomodifysave.html")
	public String addAppModifyInfo(AppInfo appInfo,HttpServletRequest request,HttpSession session,
			@RequestParam(value="a_logoPicPath",required=false)MultipartFile attach,
			@RequestParam(value="status",required=false)Integer status){
		String logoPicPath =  null;
		String logoLocPath =  null;
		String url = "dev/appinfomodify";
		
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+java.io.File.separator+"uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			int filesize = 500000;
			if(attach.getSize() > filesize){//上传大小不得超过 50k
				request.setAttribute("fileUploadError", "文件大小不能超过50k");
				return url;
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
			   ||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式
				 String fileName = appInfo.getAPKName() + ".jpg";//上传LOGO图片命名:apk名称.apk
				 File targetFile = new File(path,fileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					request.setAttribute("fileUploadError", "上传文件失败");
					return url;
				} 
				 logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				 logoLocPath = path+File.separator+fileName;
			}else{
				request.setAttribute("fileUploadError", "图片格式不正确");
				return url;
			}
		}
		
		DevUser devUser = (DevUser) session.getAttribute(Constants.DEV_USER);
		if(logoLocPath != null && logoPicPath != null){
			appInfo.setLogoLocPath(logoLocPath);
			appInfo.setLogoPicPath(logoPicPath);
		}
		
			appInfo.setModifyBy(devUser.getId());
			appInfo.setModifyDate(new Date());
			if(status != null){
				appInfo.setStatus(status);
			}
			if(appInfoService.updateAppInfo(appInfo)){
				return "redirect:/dev/appList.html";
			}else{
				return url;
			}
	}
	
	@RequestMapping("appversionadd.html")
	public String toAppversionAdd(@RequestParam("id")Integer id,
			@ModelAttribute AppVersion appVersion,HttpServletRequest request){
		List<AppVersion> appVersions = appVersionService.findAppVersionsByAPPId(id);
		request.setAttribute("appVersionList", appVersions);
		appVersion.setAppId(id);
		return "dev/appversionadd";
	}
	
	@RequestMapping("addversionsave.html")
	public String AddversionSave(AppVersion appVersion,
			@RequestParam(value="a_downloadLink",required=false)MultipartFile multipartFile,
			HttpServletRequest request,HttpSession session){
		String downLoadPath =  null;
		String apkLocPath =  null;
		String fileName=null;
		String url = "dev/appversionadd";
		
		if(!multipartFile.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+java.io.File.separator+"uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = multipartFile.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			if(prefix.equalsIgnoreCase("apk")){//上传图片格式
				 fileName = appInfoService.findAppInfoById(appVersion.getAppId()).getSoftwareName()
						 +appVersion.getVersionNo() + ".apk";//上传LOGO图片命名:apk名称.apk
				 File targetFile = new File(path,fileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					 multipartFile.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					request.setAttribute("fileUploadError", "上传文件失败");
					return url;
				} 
				 downLoadPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				 apkLocPath = path+File.separator+fileName;
			}else{
				request.setAttribute("fileUploadError", "文件格式不正确");
				return url;
			}
		}
		DevUser devUser = (DevUser) session.getAttribute(Constants.DEV_USER);
		appVersion.setCreatedBy(devUser.getId());
		appVersion.setCreationDate(new Date());
		appVersion.setDownloadLink(downLoadPath);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(fileName);
		if(appVersionService.insertAppVersion(appVersion)){
			return "redirect:/dev/appList.html";
		}
		return url;
	}
	
	@RequestMapping("appversionmodify.html")
	public String toAppVersionModify(@RequestParam("vid")Integer vid,@RequestParam("aid")Integer aid,
			HttpServletRequest request){
		request.setAttribute("appVersion", appVersionService.findAppVersionById(vid));
		request.setAttribute("appVersionList", appVersionService.findAppVersionsByAPPId(aid));
		return "dev/appversionmodify";
	}
	
	@RequestMapping("appversionmodifysave.html")
	public String appVersionModifySava(AppVersion appVersion,
			@RequestParam(value="attach",required=false)MultipartFile multipartFile,
			HttpServletRequest request,HttpSession session){
		String downLoadPath =  null;
		String apkLocPath =  null;
		String fileName=null;
		String url = "dev/appversionmodify";
		
		if(!multipartFile.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+java.io.File.separator+"uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = multipartFile.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			if(prefix.equalsIgnoreCase("apk")){//上传图片格式
				 fileName = appInfoService.findAppInfoById(appVersion.getAppId()).getSoftwareName()
						 +appVersion.getVersionNo() + ".apk";//上传LOGO图片命名:apk名称.apk
				 File targetFile = new File(path,fileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					 multipartFile.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					request.setAttribute("fileUploadError", "上传文件失败");
					return url;
				} 
				 downLoadPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				 apkLocPath = path+File.separator+fileName;
			}else{
				request.setAttribute("fileUploadError", "文件格式不正确");
				return url;
			}
		}
		DevUser devUser = (DevUser) session.getAttribute(Constants.DEV_USER);
		appVersion.setModifyBy(devUser.getId());
		appVersion.setModifyDate(new Date());
		appVersion.setDownloadLink(downLoadPath);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(fileName);
		if(appVersionService.updateVersion(appVersion)){
			return "redirect:/dev/appList.html";
		}
		return url;
	}
	
	@RequestMapping("appview.html")
	public String appView(@RequestParam("id")Integer id,HttpServletRequest request){
		request.setAttribute("appInfo", appInfoService.findAppInfoById(id));
		request.setAttribute("appVersionList", appVersionService.findAppVersionsByAPPId(id));
		return "dev/appinfoview";
	}
	
	@RequestMapping("/sale.json")
	@ResponseBody
	public Map<String,String> sale(@RequestParam("appId")Integer appId,
			@RequestParam("method")String method){
		Map<String,String> map = new HashMap<String, String>();
		if(appInfoService.updateAppInfoStatus(appId, method)){
			map.put("resultMsg", "success");
		}else{
			map.put("resultMsg", "failed");
		}
		return map;
	}
	
	@RequestMapping("/delapp.json")
	@ResponseBody
	public Map<String,String> delApp(@RequestParam("id")Integer id){
		Map<String,String> map = new HashMap<String, String>();
		//查找APP的所有版本
		List<AppVersion> appVersions = appVersionService.findAppVersionsByAPPId(id);
		//循环版本集合删除apk文件
		for(AppVersion appVersion : appVersions){
			if(appVersion.getApkLocPath() != null){
				File file = new File(appVersion.getApkLocPath());
				if(file.exists()){
					file.delete(); //删除成功 删除数据库中数据
				}
			}
			if(!appVersionService.deleteVersion(appVersion.getId())){
				map.put("delResult", "Versionfalse");
				return map;
			}
		}
		
		//删除app
		AppInfo appInfo = appInfoService.findAppInfoById(id);
		if(appInfo != null){
			//删除图片
			if(appInfo.getLogoLocPath() != null){
				File file = new File(appInfo.getLogoLocPath());
				if(file.exists()){
					file.delete();
				}
			}
			if(appInfoService.deleteAppInfo(id)){
				map.put("delResult", "true");
			}else{
				map.put("delResult", "false");
			}
		}else{
			map.put("delResult", "notexist");
		}
		return map;
	}
}
