package cn.appsys.controller.backend;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.backend.AppCheckService;
import cn.appsys.service.developer.AppCategoryService;
import cn.appsys.service.developer.AppVersionService;
import cn.appsys.service.developer.DataDictionaryService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping("/manager/backend/app/")
public class AppCheckController {
	private Logger logger = LogManager.getLogger(AppCheckController.class);
	@Autowired
	private AppCheckService appCheckService;
	@Autowired
	private AppCategoryService appCategoryService;
	@Autowired
	private DataDictionaryService dataDictionaryService;
	@Autowired
	private AppVersionService appVersionService;
	/**
	 * 获取 待审核的app信息
	 * @param session
	 * @param model
	 * @param querySoftwareName
	 * @param _queryFlatformId
	 * @param _queryCategoryLevel1
	 * @param _queryCategoryLevel2
	 * @param _queryCategoryLevel3
	 * @param pageIndex
	 * @return
	 */
	@RequestMapping("list")
	public String getAppInfoList(HttpSession session,Model model,
				@RequestParam(value="querySoftwareName",required=false)String querySoftwareName,
				@RequestParam(value="queryFlatformId",required=false)String _queryFlatformId,
				@RequestParam(value="queryCategoryLevel1",required=false)String _queryCategoryLevel1,
				@RequestParam(value="queryCategoryLevel2",required=false)String _queryCategoryLevel2,
				@RequestParam(value="queryCategoryLevel3",required=false)String _queryCategoryLevel3,
				@RequestParam(value="pageIndex",required=false)String pageIndex){
		logger.info("getAppInfoList==querySoftwareName:"+querySoftwareName);
		logger.info("getAppInfoList==_queryFlatformId:"+_queryFlatformId);
		logger.info("getAppInfoList==queryCategoryLevel1:"+_queryCategoryLevel1);
		logger.info("getAppInfoList==queryCategoryLevel2:"+_queryCategoryLevel2);
		logger.info("getAppInfoList==queryCategoryLevel3:"+_queryCategoryLevel3);
		logger.info("getAppInfoList==pageIndex:"+pageIndex);
		//appinfo列表
		List<AppInfo> appInfoList = null;
		//数据字典列表
		List<DataDictionary> flatFormList = null;
		//一级分类
		List<AppCategory> categoryLevel1List = null;
		//二级分类
		List<AppCategory> categoryLevel2List = null;
		//三级分类
		List<AppCategory> categoryLevel3List = null;
		//分页显示的记录数
		int pageSize = Constants.pageSize;
		//当前页码
		int currentPageNo = 1;
		if(null != pageIndex && !"".equals(pageIndex)){
			currentPageNo = Integer.parseInt(pageIndex);
		}
		Integer queryCategoryLevel1 = null;//queryCategoryLevel2
		if(null != _queryCategoryLevel1 && !"".equals(_queryCategoryLevel1)){
			queryCategoryLevel1 = Integer.parseInt(_queryCategoryLevel1);
		}
		Integer queryCategoryLevel2 = null;//queryCategoryLevel2
		if(null != _queryCategoryLevel2 && !"".equals(_queryCategoryLevel2)){
			queryCategoryLevel2 = Integer.parseInt(_queryCategoryLevel2);
		}
		Integer queryCategoryLevel3 = null;//queryCategoryLevel2
		if(null != _queryCategoryLevel3 && !"".equals(_queryCategoryLevel3)){
			queryCategoryLevel3 = Integer.parseInt(_queryCategoryLevel3);
		}
		Integer queryFlatformId = null;
		if(null != _queryFlatformId && !"".equals(_queryFlatformId)){
			queryFlatformId = Integer.parseInt(_queryFlatformId);
		}
		//获取总记录数
		int totalCount = 0;
		try {
			totalCount = appCheckService.getAppInfoCount(querySoftwareName,queryCategoryLevel1,
						queryCategoryLevel2,queryCategoryLevel3,queryFlatformId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		//控制首尾页
		if(currentPageNo < 1){
			currentPageNo = 1;
		}else if(currentPageNo > totalPageCount){
			currentPageNo = totalPageCount;
		}
		try {
			appInfoList = appCheckService.getAppInfoList(querySoftwareName,queryCategoryLevel1,
						queryCategoryLevel2,queryCategoryLevel3,queryFlatformId,currentPageNo,pageSize);
			flatFormList = dataDictionaryService.getDataDictionaryList("APP_FLATFORM");
			categoryLevel1List = appCategoryService.getCatagoryListByParentId(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("pages",pages);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		model.addAttribute("queryFlatformId",queryFlatformId);
		model.addAttribute("flatFormList",flatFormList);
		model.addAttribute("categoryLevel1List",categoryLevel1List);
		model.addAttribute("appInfoList",appInfoList);
		//控制二级分类和三级分类列表的回显
		if(null != queryCategoryLevel2){
			categoryLevel2List = getCategoryList(queryCategoryLevel1);
			model.addAttribute("categoryLevel2List",categoryLevel2List);
		}
		if(null != queryCategoryLevel3){
			categoryLevel3List = getCategoryList(queryCategoryLevel2);
			model.addAttribute("categoryLevel3List",categoryLevel3List);
		}
		return "backend/applist";
	}
	/**
	 * 根据父类节点查找分类信息
	 * @param id
	 * @return
	 */
	private List<AppCategory> getCategoryList(Integer pid) {
		List<AppCategory> appCategoryList = null;
		try {
			appCategoryList = appCategoryService.getCatagoryListByParentId(pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appCategoryList;
	}
	/**
	 * 根据分类节点异步动态加载分类信息
	 * @param pid
	 * @return
	 */
	@RequestMapping(value="categorylevellist.json",method=RequestMethod.GET)
	@ResponseBody
	public List<AppCategory> getAppCategoryList(@RequestParam("pid")Integer pid){
		logger.info("pid"+pid);
		return getCategoryList(pid);
	}
	/**
	 * 点击审核按钮时 跳转到app审核页面
	 * @param appId
	 * @param versionId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="check",method=RequestMethod.GET)
	public String checkAppInfo(@RequestParam("aid")Integer appId,
				@RequestParam("vid")Integer versionId,Model model){
		logger.info("appId:"+appId);
		logger.info("versionId:"+versionId);
		AppInfo appInfo = null;
		AppVersion appVersion = null;
		try {
			appInfo = appCheckService.getAppInfoById(appId);
			appVersion = appVersionService.getAppVersionById(versionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("appInfo", appInfo);
		model.addAttribute("appVersion",appVersion);
		return "backend/appcheck";
	}
	/**
	 * 点击审核通过时 修改appInfo的审核状态
	 * @param appInfo
	 * @return
	 */
	@RequestMapping(value="checksave",method=RequestMethod.POST)
	public String checkAppInfoAndSave(AppInfo appInfo){
		logger.info("appInfo:status:"+appInfo.getStatus());
		try {
			if(appCheckService.updateStatus(appInfo.getStatus(),appInfo.getId())){
				return "redirect:/manager/backend/app/list";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "backend/appcheck";
	}
}
