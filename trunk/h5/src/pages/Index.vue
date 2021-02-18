<template>
  <div class="main" v-if="false">
    <p>
      <img alt="logo" src="/static/images/common/logo.png">
    </p>
    <p>欢迎使用南昌市双师课堂系统，请选择：</p>
    <p>
      <router-link :to="`/${this.$store.state.BasePath}/Student/Curriculum`">学生</router-link>
    </p>
    <p>
      <router-link :to="`/${this.$store.state.BasePath}/Teacher/Curriculum`">老师</router-link>
    </p>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "Index",
  computed: mapState([
    "BasePath"
  ]),
  created() {
    /* switch (process.env.NODE_ENV) {
      case "development":
        if (config.homeIndexDev) location.href = config.homeIndexDev;
        break;
      case "test":
        if (config.homeIndexTest) location.href = config.homeIndexTest;
        break;
      case "production":
        if (config.homeIndexProd) location.href = config.homeIndexProd;
        break;
    } */
    this.$router.push(this.BasePath + '/Login');
  }
};
</script>
<style scoped>
.main {
  padding-top: 20px;
  background-color: #147ef6;
  height: 100vh;
  text-align: center;
  color: #fff;
}
p {
  padding: 10px 0;
}
a,
a:hover,
a:visited {
  color: #fff;
  text-decoration: underline;
}
</style>
