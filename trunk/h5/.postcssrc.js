// https://github.com/michael-ciniawsky/postcss-load-config

module.exports = {
  "plugins": {
    // to edit target browsers: use "browserslist" field in package.json
    "postcss-import": {},
    "autoprefixer": {
      browsers: ['Android >= 4.0', 'iOS >= 7']
    },
    "postcss-pxtorem":{
      rootValue: 32,
      propList: ['*'],
      selectorBlackList: [
      	".ig-",
      	".vux-",
      	".weui-",
      	".scroller-",
      	".dp-",
      	".mt-",
      	".mint-",
      	".range",
        ".calendar",
        ".inline-calendar"
      ],
      replace: true,
      mediaQuery: false,
      unitPrecision: 5, // 最小精度，小数点位数
      minPixelValue:2 // 替换的最小像素值
    }
  }
}
