/**
 * 主题设置(区分线上线下)
 */
var SYSTEM_CONFIG = {
    online: {
        homeImg: '/images/config/online/img_home_logo.png',
        stuImg: '/images/config/online/main-logo-min.png',
        teaImg: '/images/config/online/main-logo-min.png',
        adminImg: '/images/config/online/main-logo-min.png',
        bannerImg:'/images/config/online/banner_logo.png',
        teaPath: 'https://jinshuju.net/f/LF3UMK ',   //线上老师反馈信息跳转
        stuPath: 'https://jinshuju.net/f/4kv5JE',   //线上学生反馈信息跳转
        homeTitle: '双师直播.课堂',
        titleSuffix:"教辅双师课堂 -",
        homeDesc: '全国一线名师面授课',
        copyrightTextTitle: '主办：南昌市现代教育技术中心',
        copyrightText:'赣ICP备05007513号-2    Copyright ©2020 南昌市教育局版权所有',
        productName: '最好的课堂直播学习', //配置产品名称
        logoutLocation: ''
    },
    offline: {
        homeImg: '/images/config/offline/img_home_logo.png',
        stuImg: '/images/config/offline/stu-logo.png',
        teaImg: '/images/config/offline/tea-logo.png',
        adminImg: '/images/config/offline/tea-logo.png',
        bannerImg:'/images/home/banner_logo.png',
        teaPath: 'https://jinshuju.net/f/LF3UMK ',   //线下老师反馈信息跳转
        stuPath: 'https://jinshuju.net/f/4kv5JE',   //线下学生反馈信息跳转
        homeTitle: '在线互动实验室- 南昌市现代教育技术中心',
        titleSuffix:"在线互动实验室 - ",
        homeDesc: '全国一线名师面授课',
        copyrightTextTitle:'主办：南昌市学生课余教学辅导志愿服务中心      主管：南昌市现代教育技术中心',
        copyrightText: '赣ICP备05007513号-2    Copyright ©2020 南昌市教育局版权所有',
        productName: '最好的课堂直播学习',  //配置产品名称
        logoutLocation: ''
    },
    onlineLogoutLink: {
        prod: '/AccessOnline/MinLesson',  // 线上环境()
        test: '/AccessOnline/MinLesson',   //测试环境
        dev: '/AccessOnline/MinLesson'
    },
    offlineLogoutLink: {
        prod: '',  // 线上环境(待加入)
        test: 'http://devpt.dledc.com',   //开发环境 http://devpt.dledc.com
        dev: ''
    },
    /**
     * 与硬件方对接的上传接口相关数据
     */
    upload: {
        baseUrl: "https://111.75.205.169:12030",
        getTokenUrl: "https://111.75.205.169:12030/AvconRestService/tokens/login",
        delTokenUrl: "https://111.75.205.169:12030/AvconRestService/tokens/logout",
        uploadUrl: "https://111.75.205.169:12030/AvconRestService/api/upload/springUpload",
        // getTokenUrl: "http://10.0.2.231:3333/mock/228/AvconRestService/tokens/login",
        // delTokenUrl: "http://10.0.2.231:3333/mock/228/AvconRestService/tokens/logout",
        // uploadUrl: "http://10.0.2.231:3333/mock/228/AvconRestService/api/upload/springUpload",
        userInfo: {
            uname: "adm",
            passwd: "123",
        },
        filepath: "/smalllesson/",
        savepath:"http://111.75.205.169:8888" //视频保存路径域名
    },
    cloudPlatformLink: {  //【新增】登录后跳转到云平台的路径配置
        onlineUrl: "http://www.nceduc.cn/",  //线上地址
        offlineUrl: "http://www.nceduc.cn/"  //线下地址
    }
}
