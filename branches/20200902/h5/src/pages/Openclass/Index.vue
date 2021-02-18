<template>
  <div class="main">
    <div class="content">
      <view-box>
        <router-view v-if="isRouterAlive"></router-view>
      </view-box>
    </div>
    <tabbar class="bottom" v-model="curItem" v-if="!isTeacher">
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
    <tabbar class="bottom" v-model="curItem" v-else>
      <tabbar-item :link="`/OpenClass/Index`">
        <i slot="icon" class="iconfont video"></i>
        <span slot="label">复习课</span>
      </tabbar-item>
      <tabbar-item :link="`/${$store.state.BasePath}/Teacher/Curriculum`">
        <i slot="icon" class="iconfont home"></i>
        <span slot="label">课表</span>
      </tabbar-item>
      <tabbar-item :link="`/${$store.state.BasePath}/Teacher/LearningAnalysis`">
        <i slot="icon" class="iconfont homework"></i>
        <span slot="label">学情监控</span>
      </tabbar-item>
      <tabbar-item :link="`/${$store.state.BasePath}/Teacher/PersonalCenter`">
        <i slot="icon" class="iconfont user"></i>
        <span slot="label">个人中心</span>
      </tabbar-item>
    </tabbar>
  </div>
</template>

<script>
import { ViewBox, Tabbar, TabbarItem } from "vux";
import store from '../../store';
import { roleEnum } from '../../config/const';
import { getUserInfo } from '../../services/getData';

export default {
  name: 'OpenClass',
  components: {
    ViewBox,
    Tabbar,
    TabbarItem
  },
  provide () {
    return {
      reload: this.reload
    }
  },
  data () {
    return {
      isRouterAlive: true,
      curItem: 0,
      isTeacher: 0,
    }
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    async getUserInfo() {
        const userInfo = await store.dispatch('getUserInfo');
        if (userInfo.UserRoles.indexOf(roleEnum.teacher.toString()) == -1) {
          this.isTeacher = 0
          this.fetchRoute();
        } else {
          this.isTeacher = 1
          this.fetchTeacherRoute();
        }
    },
    fetchRoute() {
      let path = this.$route.path;
      if (path.indexOf("/Curriculum") > -1) {
        this.curItem = 0;
      } else if (path.indexOf("/OpenClass") > -1) {
        this.curItem = 1;
      } else if (path.indexOf("/PersonalCenter") > -1) {
        this.curItem = 3;
      } else {
        this.curItem = 2;
      }
    },
    fetchTeacherRoute() {
      let path = this.$route.path;
      if (path.indexOf("/OpenClass") > -1) {
        this.curItem = 0;
      } else if (path.indexOf("/Curriculum") > -1) {
        this.curItem = 1;
      } else if (path.indexOf("/PersonalCenter") > -1) {
        this.curItem = 3;
      } else {
        this.curItem = 2;
      }
    },
    reload () {
      this.isRouterAlive = false
      this.$nextTick(function () {
        this.isRouterAlive = true
      })
    }
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    $route: function() {
      if (this.isTeacher) {
        this.fetchTeacherRoute();
      } else {
        this.fetchRoute()
      }
    }
  }
}
</script>
