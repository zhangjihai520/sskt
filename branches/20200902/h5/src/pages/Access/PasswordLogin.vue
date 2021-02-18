<template>
  <div>
    <img src="/static/images/common/bg.jpg" style="width:100%">
    <h1 style="text-align:center;font-size:1.3em;color:#005198">双师互动平台</h1>
    <group>
      <x-input placeholder="请输入账号" v-model="userName">
        <img
          slot="label"
          style="padding-right:10px;display:block;"
          src="/static/images/common/icon_phone.png"
          width="24"
          height="24"
        >
      </x-input>
      <x-input type="password" placeholder="请输入密码" v-model="password">
        <img
          slot="label"
          style="padding-right:10px;display:block;"
          src="/static/images/common/icon_psd.png"
          width="24"
          height="24"
        >
      </x-input>
    </group>
    <div style="padding:15px;">
      <x-button type="primary" @click.native="submit" style="background-color:#4452f1">登 录</x-button>
    </div>
  </div>
</template>

<script>
import { loginWithPassword, getUserBaseInfo } from "../../services/getData";
import { setToken } from "../../utils/storage";
import { roleEnum } from "../../config/const";
import Vuex, { mapState } from "vuex";
import { XInput, Group, XButton } from "vux";

/**
 * 登录验证
 */
export default {
  name: "PasswordLogin",
  data() {
    return {
      userName: "",
      password: ""
    };
  },
  components: { XInput, Group, XButton },
  computed: mapState([
    // 映射 this.userInfo 为 store.state.userInfo
    "userInfo"
  ]),
  methods: {
    async loginWithPassword(name, psd) {
      let param = { UserName: name, Password: psd };
      let res = await loginWithPassword(param);
      if (res && res.LoginStaus === 1) {
        setToken(res.Token);
        this.$vux.toast.show({
          type: "success",
          text: "登录成功"
        });
        let user = await this.$store.dispatch("getUserInfo", true);
        if (user && user.UserRoles) {
          let userRoles = user.UserRoles.split(",");
          if (userRoles.length === 1) {
            this.goIndex(parseInt(userRoles[0]));
          } else if (userRoles.indexOf(roleEnum.teacher.toString()) > -1) {
            this.goIndex(roleEnum.teacher);
          } else {
            this.$vux.toast.show({ type: "warn", text: "暂不支持该角色" });
          }
        } else {
          this.$vux.toast.show({ type: "warn", text: "用户信息获取失败" });
        }
      } else {
        this.$vux.toast.show({ type: "warn", text: "登录失败！" });
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
           // '/OpenClass/Index'
          );
          break;
        case roleEnum.teacher:
          this.$router.push(
             `/${this.$store.state.BasePath}/Teacher/Curriculum`
           // '/OpenClass/Index'
          );
          break;
        default:
            this.$vux.toast.show({ type: "warn", text: "暂不支持该角色" });
          break;
      }
    },
    submit() {
      this.loginWithPassword(this.userName, this.password);
    }
  }
};
</script>

<style lang="less" >
</style>

