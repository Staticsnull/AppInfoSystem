package cn.appsys.controller.developer;

import cn.appsys.pojo.*;
import cn.appsys.service.developer.AppCategoryService;
import cn.appsys.service.developer.AppInfoService;
import cn.appsys.service.developer.AppVersionService;
import cn.appsys.service.developer.DataDictionaryService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dev/flatform/app")
public class AppInfoController {
    private Logger logger = LogManager.getLogger();
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private AppCategoryService appCategoryService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private AppVersionService appVersionService;
    /**
     * 根据软件名称,APP状态,平台id,一级分类,二级分类,三级分类,
     * 当前页码查询APP所有记录数
     * @param session
     * @param model
     * @param querySoftwareName
     * @param _queryStatus
     * @param _queryFlatformId
     * @param _queryCategoryLevel1
     * @param _queryCategoryLevel2
     * @param _queryCategoryLevel3
     * @param pageIndex
     * @return
     */
    @RequestMapping(value="/list")
    public String getAppInfoList(HttpSession session, Model model,
                                 @RequestParam(value="querySoftwareName",required=false)String querySoftwareName,
                                 @RequestParam(value="queryStatus",required=false)String _queryStatus,
                                 @RequestParam(value="queryFlatformId",required=false)String _queryFlatformId,
                                 @RequestParam(value="queryCategoryLevel1",required=false)String _queryCategoryLevel1,
                                 @RequestParam(value="queryCategoryLevel2",required=false)String _queryCategoryLevel2,
                                 @RequestParam(value="queryCategoryLevel3",required=false)String _queryCategoryLevel3,
                                 @RequestParam(value="pageIndex",required=false)String pageIndex){
        logger.info("getAppInfoList-->querySoftwareName:"+querySoftwareName);
        logger.info("getAppInfoList-->queryStatus:"+_queryStatus);
        logger.info("getAppInfoList-->queryFlatformId:"+_queryFlatformId);
        logger.info("getAppInfoList-->queryCategoryLevel1:"+_queryCategoryLevel1);
        logger.info("getAppInfoList-->queryCategoryLevel2:"+_queryCategoryLevel2);
        logger.info("getAppInfoList-->queryCategoryLevel3:"+_queryCategoryLevel3);
        logger.info("getAppInfoList-->pageIndex:"+pageIndex);
        //app信息列表
        List<AppInfo> appInfoList = null;
        //数据字典中的状态列表
        List<DataDictionary> statusList = null;
        //数据字典中的平台列表
        List<DataDictionary> flatFormList = null;
        //一级分类列表
        List<AppCategory> categoryLevel1List = null;
        //二级分类列表
        List<AppCategory> categoryLevel2List = null;
        //三级分类列表
        List<AppCategory> categoryLevel3List = null;
        //页面显示的记录数
        Integer pageSize = Constants.pageSize;
        //当前页码
        Integer currentPageNo = 1;
        if(null != pageIndex){
            currentPageNo = Integer.parseInt(pageIndex);
        }
        Integer queryStatus = null;
        if(null != _queryStatus && !"".equals(_queryStatus)){
            queryStatus = Integer.parseInt(_queryStatus);
        }
        Integer queryCategoryLevel1 = null;
        if(null != _queryCategoryLevel1 && !"".equals(_queryCategoryLevel1)){
            queryCategoryLevel1 = Integer.parseInt(_queryCategoryLevel1);
        }
        Integer queryCategoryLevel2 = null;
        if(null != _queryCategoryLevel2 && !"".equals(_queryCategoryLevel2)){
            queryCategoryLevel2 = Integer.parseInt(_queryCategoryLevel2);
        }
        Integer queryCategoryLevel3 = null;
        if(null != _queryCategoryLevel3 && !"".equals(_queryCategoryLevel3)){
            queryCategoryLevel3 = Integer.parseInt(_queryCategoryLevel3);
        }
        Integer queryFlatformId = null;
        if(null != _queryFlatformId && !"".equals(_queryFlatformId)){
            queryFlatformId = Integer.parseInt(_queryFlatformId);
        }
        //获取devId
        Integer devId = ((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId();
        //获取总记录数
        int totalCount = 0;
        try {
            totalCount = appInfoService.getAppInfoCount(querySoftwareName, queryStatus,
                    queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3,
                    queryFlatformId, devId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //控制首页和尾页
        PageSupport pages = new PageSupport();
        pages.setPageSize(pageSize);
        pages.setCurrentPageNo(currentPageNo);;
        pages.setTotalCount(totalCount);
        //获取总页码
        int totalPageCount = pages.getTotalPageCount();
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }
        try {
            appInfoList = appInfoService.getAppInfoList(querySoftwareName,queryStatus,
                    queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,
                    queryFlatformId,devId,currentPageNo,pageSize);
            statusList = dataDictionaryService.getDataDictionaryList("APP_STATUS");
            flatFormList = dataDictionaryService.getDataDictionaryList("APP_FLATFORM");
            categoryLevel1List = appCategoryService.getCatagoryListByParentId(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //绑定参数 进行页面回显
        model.addAttribute("appInfoList",appInfoList);
        model.addAttribute("statusList",statusList);
        model.addAttribute("flatFormList",flatFormList);
        model.addAttribute("pages",pages);
        model.addAttribute("querySoftwareName",querySoftwareName);
        model.addAttribute("queryCategoryLevel1",queryCategoryLevel1);
        model.addAttribute("queryCategoryLevel2",queryCategoryLevel2);
        model.addAttribute("queryCategoryLevel3",queryCategoryLevel3);
        model.addAttribute("categoryLevel1List",categoryLevel1List);
        model.addAttribute("queryFlatformId",queryFlatformId);
        model.addAttribute("queryStatus",queryStatus);
        if(null != queryCategoryLevel2){
            categoryLevel2List = getCategoryList(queryCategoryLevel1);
            model.addAttribute("categoryLevel2List",categoryLevel2List);
        }
        if(null != queryCategoryLevel3){
            categoryLevel3List = getCategoryList(queryCategoryLevel2);
            model.addAttribute("categoryLevel3List",categoryLevel3List);
        }
        return "developer/appinfolist";
    }

    /**
     * 根据父类节点查找分类信息
     * @param pid
     * @return
     */
    public List<AppCategory> getCategoryList(Integer pid) {
        List<AppCategory> categoryLevelList = null;
        try {
            categoryLevelList =
                    appCategoryService.getCatagoryListByParentId(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(AppCategory app:categoryLevelList){
            logger.info("app==>"+app.getCategoryName());
        }
        return categoryLevelList;
    }

    /**
     * 异步请求:根据父节点id 获取子节点信息
     * @param pid
     * @return
     */
    @RequestMapping("/categorylevellist.json")
    @ResponseBody
    public List<AppCategory> getAppCategoryList(@RequestParam Integer pid){
        logger.info(" getAppCategoryList==:pid"+pid);
        return getCategoryList(pid);
    }

    /**
     * APP基础信息添加页面
     * @return
     */
    @RequestMapping(value="/appinfoadd")
    public String appinfoAdd(){
        logger.info("appinfoAdd===>");
        return "developer/appinfoadd";
    }

    /**
     * 保存app添加信息 appinfoaddsave
     * @param appInfo
     * @param request
     * @param session
     * @param attach
     * @return
     */
    @RequestMapping("/appinfoaddsave")
    public String appinfoAddSave(AppInfo appInfo, HttpServletRequest request, HttpSession session,
                                 @RequestParam(value="a_logoPicPath",required=false) MultipartFile attach){
        //LOGO图片URL路径
        String logoPicPath = null;
        //LOGO图片的服务器存储路径
        String logoLocPath = null;
        if(!attach.isEmpty()){
            //定义上传目标路径path(statics+File.separator：自适应的路径分隔符+uploadfiles)
            String path = request.getSession().getServletContext().
                    getRealPath("statics"+ File.separator+"uploadfiles");
            //获取原文件名oldName
            String oldName = attach.getOriginalFilename();
            //通过文件名  获取原文件后缀suffix、文件大小，并进行相应的判断比较
            String suffix = FilenameUtils.getExtension(oldName);
            int fileSize = 500000;
            if(attach.getSize() > fileSize){//文件大小不超过50k
                request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_4);
                return "developer/appinfoadd";
            }else if(suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("pneg")
                    || suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("jpeg") ){
                //若满足以上规定（文件大小、文件后缀），则可以进行文件上传操作
                //定义上传文件名 fileName：当前系统时间+随机数+“_Personal.jpg”
                String fileName = appInfo.getAPKName()+".jpg";
                //根据完整文件名 构建新文件targetFile
                File targetFile = new File(path, fileName);
                //若不存在 则创建
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                //将文件写入硬盘中(调用attach.transferTo(目标文件) )
                try {
                    attach.transferTo(targetFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
                    return "developer/appinfoadd";
                }
                //获取文件完整路径名(path/fileName) 用于保存到数据库
                logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
                logoLocPath = path + File.separator +fileName;
            }else{
                request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
                return "developer/appinfoadd";
            }
        }
        appInfo.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        appInfo.setDevId(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        appInfo.setCreationDate(new Date());
        appInfo.setLogoPicPath(logoPicPath);
        appInfo.setLogoLocPath(logoLocPath);
        appInfo.setStatus(1);
        try {
            if(appInfoService.add(appInfo)){
                //重定向到list页面
                return "redirect:/dev/flatform/app/list";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "developer/appinfoadd";
    }

    /**
     * 异步验证用户输入的APKName 是否存在
     * @param APKName
     * @return
     */
    @RequestMapping("/apkexist.json")
    @ResponseBody
    public Map<String,String> apkNameExist(@RequestParam String APKName ){
        Map<String,String> resultMap = new HashMap<String,String>();
        if(StringUtils.isNullOrEmpty(APKName)){
            resultMap.put("APKName", "empty");
        }else{
            AppInfo appInfo = null;
            try {
                appInfo = appInfoService.getAppInfo(null,APKName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(null != appInfo){
                resultMap.put("APKName", "exist");
            }else{
                resultMap.put("APKName", "noexist");
            }
        }
        return resultMap;
    }

    /**
     * 根据typeCode动态加载所属平台列表
     * @param tcode
     * @return
     */
    @RequestMapping("/datadictionarylist.json")
    @ResponseBody
    public List<DataDictionary> getDataDiclist(@RequestParam String tcode){
        return this.getDataDictionarylist(tcode);
    }

    /**
     * 根据typeCode查询出相应的数据字典列表
     * @param typeCode
     * @return
     */
    public List<DataDictionary> getDataDictionarylist(String typeCode){
        List<DataDictionary> dataDictionaryList = null;
        try {
            dataDictionaryList = dataDictionaryService.getDataDictionaryList(typeCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataDictionaryList;
    }

    /**
     * 根据id显示修改App信息
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/appinfomodify")
    public String appInfoModify(@RequestParam("id")String id,
                                @RequestParam(value="error",required=false) String fileUploadError,
                                Model model){
        AppInfo appInfo = null;
        //文件上传错误信息提示 回显至页面
        if(null != fileUploadError && "error2".equals(fileUploadError)){
            fileUploadError = Constants.FILEUPLOAD_ERROR_2;
        }else if(null != fileUploadError && "error3".equals(fileUploadError)){
            fileUploadError = Constants.FILEUPLOAD_ERROR_3;
        }else if(null != fileUploadError && "error4".equals(fileUploadError)){
            fileUploadError = Constants.FILEUPLOAD_ERROR_4;
        }
        try {
            appInfo = appInfoService.getAppInfo(Integer.parseInt(id), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("fileUploadError",fileUploadError);
        model.addAttribute("appInfo", appInfo);
        return "developer/appinfomodify";
    }

    /**
     * 修改操作时,删除apk文件/logo图片,根据id 异步删除图片
     * @param id
     * @return
     */
    @RequestMapping("/delfile.json")
    @ResponseBody
    public Map<String,String> delFile(@RequestParam(value="flag",required=false) String flag,
                          @RequestParam(value="id",required=false) String id){
        logger.info("flag==>"+flag);
        logger.info("id===>"+id);
        Map<String,String> resultMap = new HashMap<String,String>();
        String fileLocPath = null;
        if(null == flag || "".equals(flag) || null == id || "".equals(id)){
            resultMap.put("result", "failed");
        }else if("logo".equals(flag)){//删除logo 图片failed
            try {
                fileLocPath = (appInfoService.getAppInfo(Integer.parseInt(id), null)).getLogoLocPath();
                File file = new File(fileLocPath);
                if(file.exists()){
                    if(file.delete()){//删除服务器存储的物理文件
                        if(appInfoService.deleteAppLogo(Integer.parseInt(id))){
                            resultMap.put("result","success");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if("apk".equals(flag)){//删除apk文件（操作app_version）
            try {
                fileLocPath = appVersionService.getAppVersionById(Integer.parseInt(id)).getApkLocPath();
                File file = new File(fileLocPath);
                if(file.exists()){
                    if(file.delete()){//删除服务器存储的物理文件
                        //deleteAPKFile
                        if(appVersionService.deleteAPKFile(Integer.parseInt(id))){//更新表
                            resultMap.put("result", "success");
                        }
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    /**
     * 对于开发者用户修改的信息进行保存
     * @param appInfo
     * @param request
     * @param session
     * @param attach
     * @return
     */
    @RequestMapping("/appinfomodifysave")
    public String appInfoModifySave(AppInfo appInfo,HttpServletRequest request,
                                    HttpSession session,@RequestParam(value="attach",required=false)MultipartFile attach){
        String logoPicPath = null;
        String logoLocPath = null;//attach attach
        String APKName = appInfo.getAPKName();
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
            String oldFileName = attach.getOriginalFilename();
            String suffix = FilenameUtils.getExtension(oldFileName);//获取后缀
            int fileSize = 500000;//上传文件不得超过50k
            if(attach.getSize() > fileSize){
                return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
                        +"&error=error4";
            }else if(suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("pneg")
                    || suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("jpeg")){
                //文件格式正确
                String fileName = APKName + ".jpg";//上传LOGO图片命名:apk名称.apk
                File targetFile = new File(path,fileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
                            +"&error=error2";
                }
                logoPicPath = request.getContextPath()+"/statics/uploadfiles/" + fileName;
                logoLocPath = path + File.separator + fileName;
            }else{
                return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
                        +"&error=error3";
            }
        }
        appInfo.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        appInfo.setModifyDate(new Date());
        appInfo.setLogoPicPath(logoPicPath);
        appInfo.setLogoLocPath(logoLocPath);
        try {
            //修改需要注意status的值
            if(appInfoService.modify(appInfo)){
                return "redirect:/dev/flatform/app/list";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "developer/appinfomodify";
    }

    /**
     * 点击新增app版本信息 跳转appVersionadd页面
     * @param appId
     * @param appVersion
     * @param model
     * @return
     */
    @RequestMapping("/appversionadd")
    public String appVersionAdd(@RequestParam("id")Integer appId,
                                @RequestParam(value="error",required=false)String fileUploadError,
                                AppVersion appVersion,Model model){
        logger.info("appVersionAdd==appId:"+appId);
        //判断错误信息
        if(null != fileUploadError && "error1".equals(fileUploadError)){
            fileUploadError = Constants.FILEUPLOAD_ERROR_1;
        }else if(null != fileUploadError && "error2".equals(fileUploadError)){
            fileUploadError = Constants.FILEUPLOAD_ERROR_2;
        }else if(null != fileUploadError && "error3".equals(fileUploadError)){
            fileUploadError = Constants.FILEUPLOAD_ERROR_3;
        }
        appVersion.setAppId(appId);
        List<AppVersion> appVersionList = null;
        try {
            appVersionList = appVersionService.getAppVersionListByAppId(appId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("fileUploadError",fileUploadError);
        model.addAttribute("appVersion", appVersion);
        model.addAttribute("appVersionList",appVersionList);
        return "developer/appversionadd";
    }

    /**
     * 保存新增app版本信息 上传该版本的apk包
     * @param appVersion
     * @param request
     * @param session
     * @param attach
     * @return
     */
    @RequestMapping("/addversionsave")
    public String appVersionAddSave(AppVersion appVersion,HttpServletRequest request,HttpSession session,
                                    @RequestParam(value="a_downloadLink",required=false) MultipartFile attach){
        String downloadLink = null;
        String apkLocPath = null;
        String apkFileName = null;
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
            String oldFileName = attach.getOriginalFilename();
            String suffix = FilenameUtils.getExtension(oldFileName);
            if(suffix.equalsIgnoreCase("apk")){//apk文件
                String apkName = null;
                try {
                    apkName = appInfoService.getAppInfo(appVersion.getAppId(), null).getAPKName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(null == apkName || "".equals(apkName)){
                    return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
                            +"&error=error1";
                }
                apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
                File targetFile = new File(path,apkFileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
                            +"&error=error2";
                }
                //得到下载链接的地址
                downloadLink = request.getContextPath() + "/statics/uploadfiles/" + apkFileName;
                apkLocPath = path + File.separator +apkFileName;
            }else{
                return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
                        +"&error=error3";
            }
        }
//		appVersion.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
//		appVersion.setModifyDate(new Date());
        appVersion.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        appVersion.setCreationDate(new Date());
        appVersion.setApkFileName(apkFileName);
        appVersion.setApkLocPath(apkLocPath);
        appVersion.setDownloadLink(downloadLink);
        try {
            //新增操作 除了要新增app版本信息 还要更新appInfo中的versionId
            if(appVersionService.appsysAdd(appVersion)){
                //添加成功 重定向至list页面
                return "redirect:/dev/flatform/app/list";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId();
    }

    /**
     * 点击修改版本信息链接时 跳转到appVersion修改页面
     * @param versionId
     * @param fileUploadError
     * @param appId
     * @param model
     * @return
     */
    @RequestMapping("/appversionmodify")
    public String appVersionModify(@RequestParam("vid") Integer versionId,
                                   @RequestParam(value="error",required=false) String fileUploadError,
                                   @RequestParam("aid") Integer appId,Model model){
        AppVersion appVersion = null;
        List<AppVersion> appVersionList = null;
        //判断错误信息
        if(null != fileUploadError && "error1".equals(fileUploadError)){
            fileUploadError = Constants.FILEUPLOAD_ERROR_1;
        }else if(null != fileUploadError && "error2".equals(fileUploadError)){
            fileUploadError = Constants.FILEUPLOAD_ERROR_2;
        }else if(null != fileUploadError && "error3".equals(fileUploadError)){
            fileUploadError = Constants.FILEUPLOAD_ERROR_3;
        }
        try {
            appVersion = appVersionService.getAppVersionById(versionId);
            appVersionList = appVersionService.getAppVersionListByAppId(appId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("fileUploadError",fileUploadError);
        model.addAttribute("appVersion",appVersion);
        model.addAttribute("appVersionList",appVersionList);
        return "developer/appversionmodify";
    }

    /**
     * 修改app版本信息 注意判断文件上传的位置
     * @param appVersion
     * @param request
     * @param session
     * @param attach
     * @return
     */
    @RequestMapping("/appversionmodifysave")
    public String appVersionModifySave(AppVersion appVersion,HttpServletRequest request,
                                       HttpSession session,@RequestParam(value="attach",required=false)MultipartFile attach){
        String downloadLink = null;
        String apkLocPath = null;
        String apkFileName = null;
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
            String oldFileName = attach.getOriginalFilename();
            String suffix = FilenameUtils.getExtension(oldFileName);
            if(suffix.equalsIgnoreCase("apk")){
                String apkName = null;
                try {
                    apkName = appInfoService.getAppInfo(appVersion.getAppId(), null).getAPKName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(null == apkName || "".equals(apkName)){
                    return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()
                            +"&aid="+appVersion.getAppId()+"&error=error1";
                }
                apkFileName = apkName + "-" + appVersion.getVersionNo() + ".apk";
                File targetFile = new File(path,apkFileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "redirect:/dev/flatform/app/appversionmodify?vid="
                            +appVersion.getId()+"&aid="+appVersion.getAppId()+"&error=error2";
                }
                logger.info("oldFileName==>"+oldFileName);
                downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
                apkLocPath = path + File.separator + apkFileName;
            }else{
                return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()
                        +"&aid="+appVersion.getAppId()
                        +"&error=error3";
            }

        }
        appVersion.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        appVersion.setModifyDate(new Date());
        appVersion.setApkFileName(apkFileName);
        appVersion.setDownloadLink(downloadLink);
        appVersion.setApkLocPath(apkLocPath);
        try {
            if(appVersionService.modify(appVersion)){
                return "redirect:/dev/flatform/app/list";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "developer/appversionmodify";
    }

    @RequestMapping("/appview/{id}")
    public String appView(@PathVariable Integer id, Model model){
        AppInfo appInfo = null;
        List<AppVersion> appVersionList = null;
        try {
            appInfo = appInfoService.getAppInfo(id, null);
            appVersionList = appVersionService.getAppVersionListByAppId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("appInfo",appInfo);
        model.addAttribute("appVersionList",appVersionList);
        return "developer/appinfoview";
    }

    /**
     * 根据id异步删除appInfo信息
     * @param id
     * @return
     */
    @RequestMapping("/delapp.json")
    @ResponseBody
    public Map<String,String> deleteApp(@RequestParam("id") String id){
        logger.info("deleteApp==>id:"+id);
        Map<String,String> resultMap = new HashMap<String,String>();
        if(StringUtils.isNullOrEmpty(id)){
            resultMap.put("delResult", "notexist");
        }else{
            try {
                if(appInfoService.appsysDeleteAppInfoById(Integer.parseInt(id))){
                    resultMap.put("delResult","true");
                }else{
                    resultMap.put("delResult","false");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    @PutMapping("/{appId}/sale.json")
    @ResponseBody
    public Map<String,String> sale(@PathVariable Integer appId,HttpSession session){
        Map<String,String> resultMap = new HashMap<String,String>();
        logger.info("sale===:appId=="+appId);
        //存放默认值
        resultMap.put("errorCode","0");
        //resultMap.put("appId", appId.toString());
        if(null != appId && appId > 0){
            DevUser devUser = (DevUser)session.getAttribute(Constants.DEV_USER_SESSION);
            AppInfo appInfo = new AppInfo();
            appInfo.setId(appId);
            appInfo.setModifyBy(devUser.getId());
            try {
                if(appInfoService.appsysUpdateSaleStatusByAppId(appInfo)){
                    resultMap.put("resultMsg", "success");
                }else{
                    resultMap.put("resultMsg", "failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("errorCode", "exception000001");
            }
        }else{
            resultMap.put("errorCode", "param000001");
        }
        return resultMap;
    }
}
