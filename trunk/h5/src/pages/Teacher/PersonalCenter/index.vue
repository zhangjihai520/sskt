<template>
  <div class="bg" :style="{ minHeight: windowHeight + 'px' }">
    <div class="content">
      <div class="headerDataWrap">
        <div>
          <div class="sclName">{{ personalInfo.SchoolName }}</div>
          <div class="username">{{ personalInfo.UserTrueName }}</div>
        </div>
        <img
          class="userAvator"
          :src="
            personalInfo.UserFace
              ? personalInfo.UserFace
              : '/static/images/common/teacher_default_avator.png'
          "
        />
      </div>
      <div class="descItem">
        <span>年级</span>
        <span>{{ personalInfo.GradeName }}</span>
      </div>
      <div class="descItem">
        <span>手机号</span>
        <span>{{ personalInfo.Phone }}</span>
      </div>
      <div class="descItem">
        <span>邮箱</span>
        <span>{{ personalInfo.Email }}</span>
      </div>
      <div class="descItem">
        <span>个人简介</span>
        <span class="wordlimit">{{ personalInfo.Comment }}</span>
      </div>
      <x-button class="logoutBtn" type="default" @click.native="loginOut"
        >退出登录</x-button
      >
      <div class="suggest" v-on:click="gotoSuggest">意见反馈</div>
    </div>
  </div>
</template>

<script>
import { XButton } from "vux";
import { getUserInfo } from "../../../services/getData";
import { clearAllCookieStorage } from "../../../utils/storage";
export default {
  name: "PersonalCenter",
  data() {
    return {
      personalInfo: {},
      windowHeight: window.innerHeight - 32 * 1.5625,
    };
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    async getUserInfo() {
      let res = await getUserInfo();
      if (res) {
        this.personalInfo = res;
      }
    },
    loginOut() {
      clearAllCookieStorage();
      this.$router.push({
        path: "/"
      });
    },
    gotoSuggest() {
      if (this.$store.state.CurUserSystem == 1) {
        location.href = config.suggestUrlTeacherOnline;
      } else {
        location.href = config.suggestUrlTeacherOffline;
      }
    }
  },
  components: {
    XButton
  }
};
</script>

<style lang="less" scoped>
@import "~vux/src/styles/1px.less";
.bg {
  background-image: url("/static/images/common/person_bg.jpg");
  width: 100%;
  background-size: cover;
  .content {
    width: 585px;
    margin: 0 auto;
    padding-top: 140px;
    .headerDataWrap {
      display: flex;
      align-items: flex-end;
      justify-content: space-between;
      padding-bottom: 44px;
      border-bottom: 1px dashed #e8e8e8;
      .sclName {
        font-size: 30px;
        color: #999;
      }
      .username {
        font-size: 48px;
        color: #333;
      }
      .userAvator {
        width: 130px;
        height: 130px;
      }
    }
    .descItem {
      height: 100px;
      display: flex;
      justify-content: space-between;
      font-size: 28px;
      color: #424242;
      padding: 0 10px;
      align-items: center;
      border-bottom: 1px solid #e8e8e8;
      .wordlimit {
        width: 400px;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        word-break: break-all;
        text-align: right;
      }
    }
    .logoutBtn {
      margin-top: 60px;
      border-radius: 38px;
      background-color: #6ab7ff;
      border: none;
      color: #fff;
      height: 76px;
      line-height: 76px;
      width: 400px;
      font-size: 28px;
    }
    .suggest {
      margin-top: 27px;
      color: #478dfe;
      background: none;
      border: none;
      text-align: center;
      padding-bottom: 100px;
      font-size: 28px;
    }
  }
}
</style>
