<template>
  <div>
    <card class="cardStyle">
      <div slot="header" class="header">
        <img
          :src="personalInfo.UserFace?personalInfo.UserFace:'/static/images/common/img_user_default.png'"
        >
        <p>{{personalInfo.UserTrueName}}</p>
      </div>
      <div slot="content" class="card-padding" v-if="pageName=='StudentPersonalCenter'">
        <p class="subNum">课程数：{{personalInfo.SubjectCount}}</p>
        <p class="homeworkNum">作业数：{{personalInfo.ExamCount}}</p>
      </div>
    </card>
    <div class="cardCon" ref="cardCon">
      <card>
        <div slot="content">
          <p class="vux-1px-b">
            学校：
            <span>{{personalInfo.SchoolName}}</span>
          </p>
          <p class="vux-1px-b">
            年级：
            <span>{{personalInfo.GradeName}}</span>
          </p>
          <p class="vux-1px-b">
            手机号：
            <span>{{personalInfo.Phone}}</span>
          </p>
          <p class="vux-1px-b">
            邮箱：
            <span>{{personalInfo.Email}}</span>
          </p>
          <p class="vux-1px-b">
            个人简介：
            <span>{{personalInfo.Comment}}</span>
          </p>
          <p style="padding:10px 10px 0 10px;">
            <x-button type="default" @click.native="gotoSuggest">意见反馈</x-button>
          </p>
          <p style="padding:10px;">
            <x-button type="warn" @click.native="loginOut">退出登录</x-button>
          </p>
        </div>
      </card>
    </div>
  </div>
</template>

<script>
import { Card, XButton } from "vux";
import { getUserInfo } from "../../services/getData";
import { clearAllCookieStorage } from "../../utils/storage";
export default {
  name: "PersonalCenter",
  data() {
    return {
      /**个人信息 */
      personalInfo: {},
      pageName: "" //TeacherPersonalCenter|StudentPersonalCenter
    };
  },
  created() {
    this.getUserInfo();
    this.pageName = this.$route.name;
  },
  methods: {
    async getUserInfo() {
      await getUserInfo().then(res => {
        if (res) {
          this.personalInfo = res;
        }
      });
    },
    loginOut() {
      clearAllCookieStorage();
      this.$router.push({
        path: "/"
      });
    },
    gotoSuggest() {
      if (this.pageName == "StudentPersonalCenter") {
        if (this.$store.state.CurUserSystem == 1) {
          location.href = config.suggestUrlTeacherOnline;
        } else {
          location.href = config.suggestUrlTeacherOffline;
        }
      } else {
        if (this.$store.state.CurUserSystem == 1) {
          location.href = config.suggestUrlStudentOnline;
        } else {
          location.href = config.suggestUrlStudentOffline;
        }
      }
    }
  },
  components: {
    Card,
    XButton
  }
};
</script>

<style lang="less" scoped>
@import "~vux/src/styles/1px.less";

.cardStyle {
  text-align: center;
  font-size: 14px;
  margin-bottom: 10px;
  .header {
    margin-top: 20px;
    img {
      width: 80px;
      height: 80px;
      line-height: 80px;
      border-radius: 50%;
      vertical-align: middle;
    }
    p {
      margin-top: 3px;
      margin-bottom: 20px;
      font-size: 20px;
    }
  }
  .card-padding {
    display: flex;
    align-items: center;
    margin: 10px 0;
    .subNum {
      margin-left: 20px;
      flex: 1;
      text-align: left;
    }
    .homeworkNum {
      margin-right: 20px;
      flex: 1;
      text-align: right;
    }
  }
}

.cardCon {
  overflow: auto;
  p {
    line-height: 42px;
    font-size: 16px;
    padding: 0 15px;
  }
}
</style>
