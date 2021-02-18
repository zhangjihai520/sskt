// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import FastClick from 'fastclick';
import VueRouter from 'vue-router';
import App from './App';
import router from './router';
import { ToastPlugin, LoadingPlugin } from 'vux';
import store from './store';
import moment from "moment";
import "moment/locale/zh-cn";
import Mint from 'mint-ui';
import { emojiArr } from "./config/const";
import 'mint-ui/lib/style.css';
import 'jquery';
import wx from 'weixin-js-sdk';
//路由配置
Vue.use(VueRouter);
//添加toast插件
Vue.use(ToastPlugin);
// 添加loading插件
Vue.use(LoadingPlugin);
/**
 * 转换成图片表情
 */
function toEmotion(text, isNoGif){
  if (!text) {
      return text;
  }

  text = text.replace(/(\[[\u4E00-\u9FA5]{1,3}\]|(\[OK\])|(\[NO\]))/gi, function(word){
      var newWord = word.replace(/\[|\]/gi,'');
      var index = emojiArr.indexOf(newWord);
      var backgroundPositionX = -index * 24
      var imgHTML = '';
      if(index<0){
          return word;
      }

      if (isNoGif) {
          if(index>emojiArr.length){
              return word;
          }
          imgHTML = `<i class="static-emotion" style="background:url(https://res.wx.qq.com/mpres/htmledition/images/icon/emotion/default218877.gif) ${backgroundPositionX}px 0;"></i>`
      } else {
          var path = index>emojiArr.length ? '/img' : 'https://res.wx.qq.com/mpres/htmledition/images/icon';
          imgHTML = `<img class="static-emotion-gif" style="vertical-align: middle" src="${path}/emotion/${index}.gif">`
      }
      return imgHTML;
  });
  return text;
}


Vue.directive('emotion', {
  bind: function (el, binding) {
      el.innerHTML = toEmotion(binding.value)
  }
});
FastClick.attach(document.body);
Vue.use(Mint);
Vue.config.productionTip = false;

moment.locale("zh-cn");

/* eslint-disable no-new */
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app-box');
