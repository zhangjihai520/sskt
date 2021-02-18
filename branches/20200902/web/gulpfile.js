/**
 * 自动构建
 * 开发环境、测试环境73
 */

const gulp = require("gulp");
const zip = require("gulp-zip");
const gulpSequence = require("gulp-sequence");
const shell = require("gulp-shell");
const del = require("del");
const GulpSSH = require("gulp-ssh");


/**
 * 清空临时文件
 */
gulp.task("clean", () => {
    return del(["./dist", "./dev.zip"]);
  });

/**
 * 执行build打包命令
 */
gulp.task("build", shell.task("npm run build"));

/**
 * 将dist目录里所有文件打成zip压缩包
 */
gulp.task("zip", () => {
    return gulp
        .src("dist/**/*")
        .pipe(zip('dev.zip'))
        .pipe(gulp.dest("./"));
});

/**
 * 初始化ssh实例
 */
const gulpSSH = new GulpSSH({
    ignoreErrors: false,
    sshConfig: {
        host: "10.0.2.231",
        port: 22,
        username: "root",
        password: "ruanyun@123456"
    }
});

/**
 * 清空站点文件夹
 */
gulp.task("clearFile", () => {
    return gulpSSH.shell([
        "cd /home/videoclassweb/",
        "rm -rf *"
    ]);
});

/**
 * 上传压缩包
 */
gulp.task("upload", ["clearFile"], () => {
    return gulp.src("./dev.zip").pipe(gulpSSH.dest("/home/videoclassweb/"));
});

/**
 * 解压、重启nginx
 */
gulp.task("unzip", () => {
    return gulpSSH.shell([
        "cd /home/videoclassweb/",
        "unzip dev.zip",
        "service nginx restart"
    ]);
});

/**
 * 开发环境部署
 * 清空dist目录 -> build -> 压缩 -> 清空站点文件夹 -> 上传 -> 解压 -> 重启nginx
 */
gulp.task("deploy_dev", done => {
    gulpSequence(
        "clean",
        "build",
        "zip",
        "upload",
        "unzip"
    )(done);
});