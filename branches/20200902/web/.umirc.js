//import routes from './src/routes.config';

const path = require('path');

export default {
    history: 'browser',
    exportStatic: true,
    hash: true,
    proxy: {
        "/api": {
            //target: "http://10.0.2.231:3333/mock/228/",
            //target: "http://10.0.2.213:30978/",
            //target: "http://10.0.3.8:30978/",
            //target: "https://sskt.nceduc.cn/",
           // target: "http://10.0.2.73:10635/",
           target: "http://192.168.50.17:8080/",
          // target: "http://172.17.2.159:30978/",
            changeOrigin: true
        },
        "/SubjectFile": {
            //target: "http://10.0.2.231:3333/mock/228/",
            //target: "http://10.0.2.213:30978/",
            //target: "http://10.0.3.8:30978/",
            //target: "https://sskt.nceduc.cn/",
            target: "http://10.0.2.73:10635/",
            changeOrigin: true
        },
        /* ,
        // 用于转发云平台验证登录的接口
        "/core/customlogin":{
            target: "http://sso.nceduc.cn",
            changeOrigin: true
        } */
    },
    routes: [
        { path: '/', component: './NoLogin/MinLesson/index.js' },
        { path: '/AdminOnline', component: './Admin/AdminIndex.js' },
        { path: '/AdminOffline', component: './Admin/AdminIndex.js' },
        { path: '/AdminOnline/', exact: false, component: '../layouts/AdminLayout/index.js' },
        { path: '/AdminOffline/', exact: false, component: '../layouts/AdminLayout/index.js' },
        { path: '/TeaOnline', component: './Teacher/TeacherIndex.js' },
        { path: '/TeaOffline', component: './Teacher/TeacherIndex.js' },
        { path: '/TeaOnline/', exact: false, component: '../layouts/TeacherLayout/index.js' },
        { path: '/TeaOffline/', exact: false, component: '../layouts/TeacherLayout/index.js' },
        { path: '/StudentOnline', exact: false, component: '../layouts/StudentLayout/index.js' },
        { path: '/StudentOffline', exact: false, component: '../layouts/StudentLayout/index.js' },
        { path: '/AccessOnline/Login', component: './Access/Login.js' },
        { path: '/AccessOnline/Logout', component: './Access/Logout.js' },
        { path: '/AccessOffline/Login', component: './Access/Login.js' },
        { path: '/AccessOffline/Logout', component: './Access/Logout.js' },
        { path: '/Access/ExamSetRedirect', component: './Access/ExamSetRedirect.js' },
        { path: '/AccessOnline/AntiVirusPublicClass', component: './NoLogin/HomePage/antiVirus.js' }, //抗击疫情页面（首页）
        { path: '/AccessOnline/Home', component: './NoLogin/HomePage/index.js' },  //线上课程管理
        { path: '/AccessOnline/MinLesson', component: './NoLogin/MinLesson/index.js' },  //线上微课
        // { path: '/Access/PlatformLogin', component: './Access/PlatformLogin.js' },
    ],
    disableRedirectHoist: true,
    plugins: [
        [
            'umi-plugin-react', { antd: true, dva: true ,locale: {
              enable: true, // default false
              default: 'zh-CN', // default zh-CN
            }}
        ],
    ],
    alias: {
        components: path.resolve('src/components'),   //组件文件路径
        layouts: path.resolve('src/layouts'),         //布局文件路径
        assets: path.resolve('src/assets'),           //资源文件路径
        config: path.resolve('src/config'),           //配置文件路径
        utils: path.resolve('src/utils'),             //工具文件路径
    },
    extraBabelPlugins: [
        ["import", { libraryName: "antd", style: true }]
    ],
    theme: "src/config/theme.js",
    targets: {
        ie: 10, chrome: 49, firefox: 45, safari: 10, edge: 13
    },
    define: {
        "process.env.UMIS_ENV": process.env.UMIS_ENV,  //test:测试环境, prod:线上环境, dev:本地环境
    }
};
