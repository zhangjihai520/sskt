<template>
  <div class="main">
    <div class="content">
      <view-box>
        <router-view></router-view>
      </view-box>
    </div>
    <div v-if="showFoot" >
    <tabbar class="bottom" v-if="showFoot" v-model="curItem">
      <tabbar-item :link="`/${$store.state.BasePath}/Student/Curriculum`">
        <i slot="icon" class="iconfont home"></i>
        <span slot="label">首页</span>
      </tabbar-item>
      <tabbar-item :link="`/OpenClass/Index`">
        <i slot="icon" class="iconfont video"></i>
        <span slot="label">复习课</span>
      </tabbar-item>
      <tabbar-item :link="`/${$store.state.BasePath}/Student/Homework`">
        <i slot="icon" class="iconfont homework"></i>
        <span slot="label">作业练习</span>
      </tabbar-item>
      <tabbar-item :link="`/${$store.state.BasePath}/Student/PersonalCenter`">
        <i slot="icon" class="iconfont user"></i>
        <span slot="label">个人中心</span>
      </tabbar-item>
    </tabbar>
    </div>
  </div>
</template>
<script>
import { ViewBox, Tabbar, TabbarItem } from "vux";

export default {
  name: "StudentLayout",
  components: {
    ViewBox,
    Tabbar,
    TabbarItem
  },
  data() {
    return {
      curItem: 0,
      showFoot: true
    };
  },
  created() {
    this.fetchRoute();
  },
  methods: {
    fetchRoute() {
      let path = this.$route.path;
      this.showFoot = true;
      if (path.indexOf("/Curriculum") > -1) {
        this.curItem = 0;
      }else if (path.indexOf("/LiveStream") > -1||path.indexOf("/RecordStream") > -1) {
        this.curItem = 0;
        this.showFoot = false;
      } else if (path.indexOf("/OpenClass") > -1) {
        this.curItem = 1;
      }else if (path.indexOf("/PersonalCenter") > -1) {
        this.curItem = 3;
      }
      else {
        this.curItem = 2;
      }
    }
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    $route: "fetchRoute"
  }
};
</script>
<style lang="less" scoped>
</style>
