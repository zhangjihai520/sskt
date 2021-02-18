<template>
  <div>{{messsage}}</div>
</template>

<script>
import { getTokenByUId, getUserBaseInfo } from "../../services/getData";
import { setToken } from "../../utils/storage";
import { roleEnum } from "../../config/const";
import Vuex, { mapState } from "vuex";

/**
 * 云平台登录验证
 */
export default {
  name: "Login",
  data() {
    return {
      messsage: "正在登录，请稍后......"
    };
  },
  components: {},
  computed: mapState([
    // 映射 this.userInfo 为 store.state.userInfo
    "userInfo"
  ]),
  created() {
    if (this.$route.query.uid) {
      this.getTokenByUId(this.$route.query.uid, this.$route.query.IsTest);
    }
  },
  methods: {
    async getTokenByUId(uId, isTest) {
      let param = { UId: uId, IsTest: isTest };
      if (!isTest) {
        param = { UId: uId };
      } else {
        param = { UId: uId, IsTest: isTest };
      }
      let res = await getTokenByUId(param);
      if (res && res.LoginStaus === 1) {
        setToken(res.Token);
        this.messsage = "登录成功，正在跳转......";
        let user = await this.$store.dispatch("getUserInfo", true);
        if (user && user.UserRoles) {
          let userRoles = user.UserRoles.split(",");
          if (userRoles.length === 1) {
            this.goIndex(parseInt(userRoles[0]));
          } else if (userRoles.indexOf(roleEnum.teacher.toString()) > -1) {
            this.goIndex(roleEnum.teacher);
          } else {
            this.messsage = "暂不支持该角色";
          }
        } else {
          this.messsage = "用户信息获取失败";
        }
      } else {
        this.messsage = "登录失败！";
      }
    },
    /**
     * 根据当前角色跳转
     * @param {*} role 角色
     */
    goIndex(role) {
      switch (role) {
        case roleEnum.student:
          this.$router.push(
            `/${this.$store.state.BasePath}/Student/Curriculum`
            //'/OpenClass/Index'
          );
          break;
        case roleEnum.teacher:
          this.$router.push(
            `/${this.$store.state.BasePath}/Teacher/Curriculum`
            //'/OpenClass/Index'
          );
          break;
        default:
          this.messsage = "暂不支持该角色";
          break;
      }
    }
  }
};
</script>

<style lang="less" >
</style>

